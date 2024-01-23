package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    private ChessPosition startPosition;
    private ChessPosition endPosition;
    private ChessPiece.PieceType promotionPiece;
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    public void setPromotionPiece(ChessPiece.PieceType promotionPiece){this.promotionPiece = promotionPiece;}
    @Override
    public boolean equals(Object o){
        if (o == null){
            return false;
        }
        if (o == this) {
            return true;
        }
        if (this.getClass() != o.getClass()){
            return false;
        }
        ChessMove other = (ChessMove) o;
//TODO Add promotion piece
        if (this.promotionPiece != null && other.promotionPiece != null) {
            return endPosition.equals(other.endPosition) && startPosition.equals(other.startPosition) && promotionPiece.equals(other.promotionPiece);
        } else if (this.promotionPiece == null && other.promotionPiece == null){ return endPosition.equals(other.endPosition) && startPosition.equals(other.startPosition);
        } else { return false ;}
    }

    @Override
    public String toString() {
        return "ChessMove{" + "\n" +
                "startPosition=" + startPosition + "\n" +
                ", endPosition=" + endPosition + "\n" +
                ", promotionPiece=" + promotionPiece + "\n" +
                '}' + "\n";
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, promotionPiece);
    }
}


