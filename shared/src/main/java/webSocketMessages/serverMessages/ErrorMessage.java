package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage{

private String errorMessage;


    public ErrorMessage(ServerMessageType type, String errorMessage) {
        super(type, null, errorMessage, null);
        this.errorMessage = errorMessage;

    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
