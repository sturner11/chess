package client.websocket;

import chess.ChessMove;
import com.google.gson.Gson;
//import webSocketMessages.userCommands.JoinPlayerCommand;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import static webSocketMessages.userCommands.UserGameCommand.CommandType.JOIN_PLAYER;


public class WebSocketFacade extends Endpoint {
    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "connect");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String s) {
                    webSocketMessages.Notification notification = new Gson().fromJson(s, webSocketMessages.Notification.class);
                    notificationHandler.notify(notification);
                }
            });
        } catch (URISyntaxException | RuntimeException | IOException | DeploymentException e) {
            throw new Error(e);
        }
    }



    @Override
    public void onOpen(javax.websocket.Session session, EndpointConfig endpointConfig) {
        System.out.println("WebSocket Opened!");
    }

    public void joinObserver(Integer gameID){}

    public void makeMove(Integer gameID, ChessMove move){}

    public void leave(String authToken, String username) {
        try {
            var action = new UserGameCommand( authToken, username);
            action.setCommandType(UserGameCommand.CommandType.LEAVE);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
            this.session.close();
        } catch (IOException ex) {
//            throw new Exception(500, ex.getMessage()); TODO: FIX EXCEPTIONS
        }
    }



    public void resign (Integer gameID) {}

    public void joinPlayer(String gameID,  String username, String playerColor, String auth) throws IOException {
        try {
        var action = new UserGameCommand(auth, username, playerColor, gameID);
        action.setCommandType(JOIN_PLAYER);
        this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
//            throw new Exception(500, ex.getMessage()); TODO
        }
    }




//    public static void main(String[] args) {
//        Spark.port(8080);
//        Spark.webSocket("/connect", WebSocketFacade.class);
//        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
//    }
//    @OnWebSocketMessage
//    public void onMessage(Session session, String msg) throws Exception {
//        UserGameCommand command = readJson(msg, UserGameCommand.class);
//        var conn = getConnection(command.authToken, session);
//        if (conn != null){
//            switch (command.commandType) {
//                case JOIN_PLAYER -> join(conn, msg);
//                case JOIN_OBSERVER -> observe(conn, msg);
//                case MAKE_MOVE -> move(conn, msg);
//                case LEAVE -> leave(conn, msg);
//                case RESIGN -> resign(conn, msg);
//            }
//        }
//    }
//
//    private UserGameCommand readJson(String msg, Class<UserGameCommand> userGameCommandClass) {
//        return new UserGameCommand()
//    }
}