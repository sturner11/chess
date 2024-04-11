package client.websocket;

import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
//import webSocketMessages.userCommands.JoinPlayerCommand;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.MoveCommand;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import static webSocketMessages.userCommands.UserGameCommand.CommandType.*;


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
                    ServerMessage serverMessage = new Gson().fromJson(s, ServerMessage.class);
                    ServerMessage.ServerMessageType type = serverMessage.getServerMessageType();
                    switch (type){
                        case LOAD_GAME, NOTIFICATION -> notificationHandler.notify(serverMessage);
                        case ERROR -> notificationHandler.notify(new Gson().fromJson(s, ErrorMessage.class));
                    }
                    notificationHandler.notify(serverMessage);
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



    public void resign(String gameID, String playerColor, String auth) {
        try {
            var action = new UserGameCommand(auth, playerColor, gameID);
            action.setCommandType(RESIGN);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            var test = ex;
//            throw new Exception(500, ex.getMessage()); TODO
        }
    }

    public void leave(String gameID, String playerColor, String auth){
        try {
            var action = new UserGameCommand(auth, playerColor, gameID);
            action.setCommandType(RESIGN);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            var test = ex;
//            throw new Exception(500, ex.getMessage()); TODO
        }
    }

    public void joinPlayer(String gameID, String playerColor, String auth) throws IOException {
        try {
        var action = new UserGameCommand(auth, playerColor, gameID);
        action.setCommandType(JOIN_PLAYER);
        this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            var test = ex;
//            throw new Exception(500, ex.getMessage()); TODO
        }
    }

    public void makeMove(String piecePosition, String desiredPosition, String auth, String gameId) {
        try {
            Integer[] piecePositionInts = positionConverter(piecePosition);
            Integer[] desiredPositionInts = positionConverter(desiredPosition);
            ChessPosition startPosition = new ChessPosition(piecePositionInts[0], piecePositionInts[1]);
            ChessPosition endPosition = new ChessPosition(desiredPositionInts[0], desiredPositionInts[1]);

            ChessMove chessMove = new ChessMove(startPosition, endPosition, null);
            var action = new MoveCommand(auth, gameId, chessMove);
            action.setCommandType(MAKE_MOVE);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
//            throw new Exception(500, ex.getMessage()); TODO
        }
    }

    private Integer[] positionConverter(String piecePosition) {
        HashMap<Character, Integer> letterNumberMap = new HashMap<>();

        // Loop through the first 10 letters (A-J) and add them to the map
        for (char ch = 'a'; ch <= 'j'; ch++) {
            int number = ch - 'a' + 1; // Calculate corresponding number (1-based)
            letterNumberMap.put(ch, number);
        }
        return new Integer[]{letterNumberMap.get(piecePosition.charAt(0)), Integer.parseInt(piecePosition.substring(1))};
    }

}