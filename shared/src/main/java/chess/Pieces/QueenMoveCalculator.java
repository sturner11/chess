package chess.Pieces;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import java.util.Collection;
import java.util.HashSet;


public class QueenMoveCalculator implements PieceMoveCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        HashSet<ChessMove> moves = AttackMovement.movement(position, board);
        HashSet<ChessMove> bishopMoves = (HashSet<ChessMove>) DiagMovement.move(board, position);

        moves.addAll(bishopMoves);
        return moves;
    }
}
