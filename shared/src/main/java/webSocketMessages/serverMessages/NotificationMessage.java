package webSocketMessages.serverMessages;

public class NotificationMessage extends ServerMessage{
    public NotificationMessage(ServerMessageType type, String game, String message) {
        super(type, game, message);
    }
}
