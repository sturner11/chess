package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPosition[][] board = new ChessPosition[8][8];
    public ChessBoard() {
        for (int i = 0; i < 8; i++) {
            ChessPosition[] row = new ChessPosition[8];
            for (int j = 0; j < 8; j++) {
                ChessPosition temp = new ChessPosition(i, j);
                row[j] = temp;
            }
            board[i] = row;
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        if (position.hasPiece) {
            throw new Error("Piece not added, position already filled");
        }
        position.hasPiece = true;
        position.piece = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        if (position.hasPiece) {
            return position.piece;
        }
        return null;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
    }

}
