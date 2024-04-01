package chess;

import chess.Pieces.*;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor color;
    private final ChessPiece.PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        color = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    @Override
    public String toString() {
        if (color == ChessGame.TeamColor.WHITE){
            if (type == PieceType.KNIGHT){
                return type.toString().substring(1,2);
            }
            return type.toString().substring(0,1);
        } else {
            if (type == PieceType.KNIGHT){
                return type.toString().substring(1,2).toLowerCase();
            }
            return type.toString().substring(0,1).toLowerCase();
        }
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        if (piece == null){
            return null;
        }

        switch(piece.type){
            case ChessPiece.PieceType.BISHOP:
                BishopMoveCalculator calcBishop = new BishopMoveCalculator();
                return calcBishop.pieceMoves(board, myPosition);
            case ChessPiece.PieceType.KING:
                KingMoveCalculator calcKing = new KingMoveCalculator();
                return calcKing.pieceMoves(board, myPosition);
            case ChessPiece.PieceType.KNIGHT:
                KnightMoveCalculator calcKnight = new KnightMoveCalculator();
                return calcKnight.pieceMoves(board, myPosition);
            case PieceType.PAWN:
                PawnMoveCalculator calcPawn = new PawnMoveCalculator();
                return calcPawn.pieceMoves(board, myPosition);
            case PieceType.ROOK:
                RookMoveCalculator calcRook = new RookMoveCalculator();
                return calcRook.pieceMoves(board, myPosition);
            case PieceType.QUEEN:
                QueenMoveCalculator calcQueen = new QueenMoveCalculator();
                return calcQueen.pieceMoves(board,myPosition);

        }
        return null;

    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
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
        ChessPiece other = (ChessPiece)o;
        return this.color.equals(other.color) && this.type.equals(other.type);
    }
}
