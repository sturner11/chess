package server.websocket;


import chess.ChessMove;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.Notification;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER:
                join(command.getUsername(), session, command.getPlayerColor(), command.getGameID());
                break;
            case MAKE_MOVE:
                move(command.getUsername(), command.getMove(), session, command.getPlayerColor(), command.getGameID());
        }
    }

    private void join(String username, Session session, String playerColor, String gameID) throws IOException {
        connections.add(username, session);
        var message = username + " has joined the game as " + (playerColor != null ? playerColor : "an observer");
        var notification = new Notification(Notification.Type.JOIN, message, playerColor, username, gameID);
        connections.broadcast(username, notification);
    }

    private void move(String username, ChessMove move, Session session, String playerColor, String gameID) throws IOException {
        var message = username + " moves to " + move.toString();
        var notification = new Notification(Notification.Type.MOVE, message, playerColor, username, gameID);
        connections.broadcast(username, notification);
    }

//    private String getChessBoard
}
