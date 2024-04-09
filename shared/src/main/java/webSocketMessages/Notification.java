package webSocketMessages;

import com.google.gson.Gson;

public record Notification(Type type, String message) {

    public enum Type {
        JOIN,
        MOVE,
        LEAVE,
        RESIGN
    }

    public String toString() { return new Gson().toJson(this);}
}
