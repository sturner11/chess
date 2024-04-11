package webSocketMessages.serverMessages;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 *
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    private String game;
    private String message;


    public ServerMessage(ServerMessageType type, String game) {
        this.serverMessageType = type;
        this.game = game;
    }


    public ServerMessage(ServerMessageType type, String game, String message){
        this.serverMessageType = type;
        this.game = game;
        this.message = message;
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }
    public String getMessage() {return message; }
    public String getGame() {return game; }



    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ServerMessage))
            return false;
        ServerMessage that = (ServerMessage) o;
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}