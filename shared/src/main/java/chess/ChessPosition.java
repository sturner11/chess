package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private final int row;
    private final int col;
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


    @Override
    public int hashCode() {
//        return Objects.hash(hasPiece, piece, row, col);
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "ChessPosition{" + "\n" +
                ", row=" + row + "\n" +
                ", col=" + col + "\n" +
                '}' + "\n";
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
        return this.col == other.col && this.row == other.row;


    }
}
