package webSocketMessages.userCommands;

public class MoveCommand extends UserGameCommand {

    private String piecePosition;
    private String desiredPosition;
    public MoveCommand(String authToken, String username, String piecePosition, String desiredPosition) {
        super(authToken, username);
        this.piecePosition = piecePosition;
        this.desiredPosition = desiredPosition;
    }

    @Override
    public String getPiecePosition() {
        return piecePosition;
    }

    @Override
    public String getDesiredPosition() {
        return desiredPosition;
    }
}
