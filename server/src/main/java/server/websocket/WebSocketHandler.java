package server.websocket;


import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import services.GameService;
import services.UserService;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.MoveCommand;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    GameService gameService = new GameService();
    UserService userService = new UserService();
    private String username;
    private String playerColor;
    private UserGameCommand.CommandType type;

    private final ConnectionManager connections = new ConnectionManager();

    public WebSocketHandler() throws DataAccessException {
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        try {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
            type = command.getCommandType();
            switch (type) {
                case JOIN_PLAYER:
                case JOIN_OBSERVER:
                    joinPlayer(session, command.getPlayerColor(), command.getGameID(), command.getAuthString());
                    break;
                case MAKE_MOVE:
                    MoveCommand move = new Gson().fromJson(message, MoveCommand.class);
                    move(session, move.getMove(), command.getGameID(), command.getAuthString());
                    break;
                case LEAVE:
//                leave(command.getGameID(), command.getPlayerColor());
                    break;
            }

        } catch (Exception e){
//            var test = e;
//            System.out.println(e);
        }

    }

//    @OnWebSocketError
//    public void onError(Session session, Throwable cause){
//        System.out.println(cause.getMessage());
//    }

    private void joinPlayer(Session session, String playerColor, String gameID, String auth) throws IOException {
        try {
            this.playerColor = playerColor;
            getValidData(auth, gameID);
            var message = username + " has joined the game as " + (playerColor != null ? playerColor : "an observer");
            connections.add(username, session);
            String gameString =  gameService.getBoard(Integer.parseInt(gameID));
            sendMessage(gameString, message, gameID);
        } catch (Exception e) {
           sendError(session);
        }
    }

    private void move(Session session, ChessMove move, String gameID, String auth) {
        try {
            getValidData(auth, gameID);
            ChessGame game =  new Gson().fromJson(gameService.getBoard(Integer.parseInt(gameID)), ChessGame.class);
            if (isValidMove(move, gameID, playerColor, game)) {
                game.makeMove(move);
                gameService.makeMove(game, gameID);
                String message = username + " moves " + move.getStartPosition().toString() + " to " + move.getEndPosition().toString();
                String gameString = new Gson().toJson(game);
                sendMessage(gameString, message, gameID);
            }

        } catch (Exception e) {
//            var test = e;
//            System.out.println(e);
        }
    }


    private boolean isValidMove(ChessMove move, String gameID, String playerColor, ChessGame game) throws SQLException {
        return game.validMoves(move.getStartPosition(), ChessGame.TeamColor.valueOf(playerColor)).contains(move);
    }

    private void leave(String username, String gameID, String playerColor) throws IOException {
        var message = username + " has left the game";
//        var notification = new Notification(Notification.Type.LEAVE, message, null, username, gameID);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message,  gameID);
        connections.broadcast(username, notification);
        connections.remove(username);
        gameService.removeUser(gameID, playerColor);
    }

    private void getValidData(String auth, String gameID) throws Exception {
        username = userService.getUser(auth);
        if (username == null) {
            username = "null";
            throw new Exception();
        }
        if(!isObserver(gameID, username)){
                username = checkUser(username, gameID);
                if (Objects.equals(username, "null")) {
                    throw new Exception();
                }
        } else if (type == UserGameCommand.CommandType.JOIN_PLAYER){
            throw new Exception();
        }
        gameService.getGame(gameID);
    }

    private String checkUser(String username, String gameID) throws Exception {
        if (this.playerColor == null) {
            playerColor = gameService.getPlayerColor(username, gameID);
        }
        String player = gameService.getUser(gameID, playerColor);
            if (!player.equals(username)) {
                return "null";
            }
        return username;
    }

    private boolean isObserver(String gameID, String username) {
        return gameService.isObserver(gameID, username);
    }


    private void sendMessage(String game, String message, String gameID) throws IOException {
        var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, game, message, playerColor);
        connections.send(username, serverMessage);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, gameID, message);
        connections.broadcast(username, notification);
    }

    private void sendError(Session session) throws IOException {
        connections.add(username, session);
        var message = "Could not join game";
        var serverMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, null, message);
        connections.send(username, serverMessage);
        connections.remove(username);
    }



}
