package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage{

private String errorMessage;


    public ErrorMessage(ServerMessageType type, String game, String errorMessage) {
        super(type, game);
        this.errorMessage = errorMessage;

    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
