package webSocketMessages.userCommands;

import chess.ChessMove;

public class MoveCommand extends UserGameCommand {

    ChessMove move;
    public MoveCommand(String authToken, String gameID, ChessMove move) {
        super(authToken, null, gameID);
        this.move = move;

    }

    public ChessMove getMove() {
        return move;
    }
}
