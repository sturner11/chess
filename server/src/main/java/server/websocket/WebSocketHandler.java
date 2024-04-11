package server.websocket;


import chess.ChessMove;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import services.GameService;
import services.UserService;
import webSocketMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.MoveCommand;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.sql.SQLException;

@WebSocket
public class WebSocketHandler {
    GameService gameService = new GameService();
    UserService userService = new UserService();

    private final ConnectionManager connections = new ConnectionManager();

    public WebSocketHandler() throws DataAccessException {
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER:
                join(command.getUsername(), session, command.getPlayerColor(), command.getGameID(), command.getAuthString());
                break;
            case LEAVE:
                leave(command.getUsername(), command.getGameID(), command.getPlayerColor());
                break;
            case MAKE_MOVE:
                MoveCommand move = new Gson().fromJson(message, MoveCommand.class);
                move(command.getUsername(), command.getPiecePosition(), command.getDesiredPosition());
        }

    }

    private void leave(String username, String gameID, String playerColor) throws IOException {
        var message = username + " has left the game";
//        var notification = new Notification(Notification.Type.LEAVE, message, null, username, gameID);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message,  gameID);
        connections.broadcast(username, notification);
        connections.remove(username);
        gameService.removeUser(gameID, playerColor);
    }

    private void join(String username, Session session, String playerColor, String gameID, String auth) throws IOException {
        if (username == null){
            try {
                username = userService.getUser(auth);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
            String player = gameService.getUser(gameID, playerColor);
        if (player.equals(username)){
            connections.add(username, session);
            var message = username + " has joined the game as " + (playerColor != null ? playerColor : "an observer");
            var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameID, message);
            connections.send(username, serverMessage);
            var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, gameID, message);
            connections.broadcast(username, notification);
        } else {
            connections.add(username, session);
            var message = "Could not join game";
            var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, message);
            connections.send(username, serverMessage);
            connections.remove(username);
        }
    }

    private void move(String username, String piecePosition, String desiredPosition) throws IOException {
        var message = username + " moves " + piecePosition + " to " + desiredPosition;
        var notification = new Notification(Notification.Type.MOVE, message);
//        connections.broadcast(username, notification);
    }

}
