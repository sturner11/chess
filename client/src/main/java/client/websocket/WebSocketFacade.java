package client.websocket;

import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import spark.Spark;
import webSocketMessages.userCommands.UserGameCommand;

@WebSocket
public class WebSocketFacade {
    public static void main(String[] args) {
        Spark.port(8080);
        Spark.webSocket("/connect", WebSocketFacade.class);
        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws Exception {
        UserGameCommand command = readJson(msg, UserGameCommand.class);
        var conn = getConnection(command.authToken, session);
        if (conn != null){
            switch (command.commandType) {
                case JOIN_PLAYER -> join(conn, msg);
                case JOIN_OBSERVER -> observe(conn, msg);
                case MAKE_MOVE -> move(conn, msg);
                case LEAVE -> leave(conn, msg);
                case RESIGN -> resign(conn, msg);
            }
        }
    }

    private UserGameCommand readJson(String msg, Class<UserGameCommand> userGameCommandClass) {
        return new UserGameCommand()
    }
}