package webSocketMessages;

import com.google.gson.Gson;

public record Notification(Type type, String message, String playerColor, String username, String gameID) {

    public Notification(Type type, String message, String playerColor, String username, String gameID){
        this.type = type; this.message = message; this.playerColor = playerColor; this.username = username; this.gameID = gameID;
    }

    public Notification(Type type, String message) {
        this(type, message, null, null, null);
    }

    public enum Type {
        JOIN,
        MOVE,
        LEAVE,
        RESIGN
    }

    public String toString() { return new Gson().toJson(this);}
}
