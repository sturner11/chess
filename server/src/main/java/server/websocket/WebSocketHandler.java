package server.websocket;


import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
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
                case RESIGN:
                    resign(session, command);
                    break;
                case LEAVE:
                leave(session, command);
                    break;
            }
            type = null; username = null; playerColor = null;

        } catch (Exception e){
//            var test = e;
//            System.out.println(e);
        }

    }


    private void resign(Session session, UserGameCommand command) throws IOException {
        try {
            getValidData(command.getAuthString(), command.getGameID());
            if (isObserver(command.getGameID(), username)){
                throw new Exception();
            }
            this.playerColor = command.getPlayerColor();
            ChessGame game =  new Gson().fromJson(gameService.getBoard(Integer.parseInt(command.getGameID())), ChessGame.class);
            game.resign();
            gameService.updateBoard(new Gson().toJson(game), command.getGameID());
            String gameString = gameService.getBoard(Integer.parseInt(command.getGameID()));
            var message = username + " has resigned. Coward. Thanks for playing!";
            var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message,  gameString);
            connections.sendAll(notification, command.getGameID());
        } catch (Exception e) {
            var message = "Could not Resign. Keep Fighting!";
            sendError(session, message, command.getGameID());
        }
    }


    private void joinPlayer(Session session, String playerColor, String gameID, String auth) throws IOException {
        try {
            this.playerColor = playerColor;
            getValidData(auth, gameID);
            var message = username + " has joined the game as " + (playerColor != null ? playerColor : "an observer");
            connections.add(username, session, gameID);
            String gameString =  gameService.getBoard(Integer.parseInt(gameID));
            sendMessage(gameString, message, gameID);
        } catch (Exception e) {
            var message = "Could not join game";
           sendError(session, message, gameID);
        }
    }

    private void move(Session session, ChessMove move, String gameID, String auth) throws IOException {
        try {
            getValidData(auth, gameID);
            ChessGame game =  new Gson().fromJson(gameService.getBoard(Integer.parseInt(gameID)), ChessGame.class);
            if (isValidMove(move, gameID, playerColor, game)) {
                game.makeMove(move);
                gameService.updateBoard(new Gson().toJson(game), gameID);
                String message = username + " moves " + move.getStartPosition().getRow() + "," + move.getStartPosition().getColumn() + " to " + move.getEndPosition().getRow() + "," + move.getEndPosition().getColumn();
                String gameString =  gameService.getBoard(Integer.parseInt(gameID));
                var loadGame = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameString, message, playerColor);
                connections.sendAll(loadGame, gameID);
//                var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, gameID, message);
                var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, gameID,  message);
                connections.broadcast(username, notification, gameID);
            } else {
                throw new Exception();
            }

        } catch (Exception e) {
            var message = "Invalid move";
            sendError(session, message, gameID);
        }
    }


    private boolean isValidMove(ChessMove move, String gameID, String playerColor, ChessGame game) throws SQLException {
        return game.validMoves(move.getStartPosition(), ChessGame.TeamColor.valueOf(playerColor)).contains(move);
    }

    private void leave(Session session, UserGameCommand command) throws Exception {
        try {
            getValidData(command.getAuthString(), command.getGameID());
            var message = username + " has left the game";
//        var notification = new Notification(Notification.Type.LEAVE, message, null, username, gameID);
            var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message, command.getGameID());
            connections.broadcast(username, notification, command.getGameID());
            connections.remove(username, command.getGameID());
            gameService.removeUser(command.getGameID(), command.getPlayerColor());
        } catch (Exception e) {
            var message = "Failed to leave. Please try again";
            sendError(session, message, command.getGameID());
        }
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
        connections.send(username, serverMessage, gameID);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, gameID, message);
        connections.broadcast(username, notification, gameID);
    }

    private void sendError(Session session, String message, String gameID) throws IOException {
        connections.add(username, session, gameID);
        var serverMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, message);
        connections.send(username, serverMessage,gameID);
        connections.remove(username, gameID);
    }



}
