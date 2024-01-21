package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    public boolean hasPiece = false;
    public ChessPiece piece;
    private int row;
    private int col;
    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return this.col;
    }

    public ChessPiece setPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.piece = new ChessPiece(pieceColor, type);
        return this.piece;
    }
    @Override
    public boolean equals(Object o){
        if (o == null){
            return false;
        }
        if (this == o){
            return true;
        }
        if (this.getClass() != o.getClass()){
            return false;
        }
        ChessPosition other = (ChessPosition)o;
        return this.hasPiece == other.hasPiece && this.col == other.col && this.row == other.row;
    }
}
