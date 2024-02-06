package chess.Pieces;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KingMoveCalculator implements PieceMoveCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        HashSet<ChessMove> moves = new HashSet<>();
        int oRow = position.getRow();
        int oCol = position.getColumn();
        ChessPiece[][] myBoard = board.getBoard();
        ChessPiece piece = myBoard[oRow - 1][oCol - 1];
        int row = oRow-1;
        int col = oCol;
        for (int i = oCol - 1; i < oCol + 2; i++){
            ChessPosition next = new ChessPosition(i, oCol);
            if (i > 1 && i < 9 && row > 1 && (myBoard[row-1][i-1] == null || myBoard[row-1][i-1].getTeamColor() != piece.getTeamColor())) {
                ChessMove move = new ChessMove(position, new ChessPosition(row, i), null);
                moves.add(move);
            }
        }
        row = oRow +1 ;
        for (int i = oCol - 1; i < oCol + 2; i++){
            ChessPosition next = new ChessPosition(i, oCol);
            if (i > 1 && i < 9 && row < 9 && (myBoard[row-1][i-1] == null || myBoard[row-1][i-1].getTeamColor() != piece.getTeamColor())) {
                ChessMove move = new ChessMove(position, new ChessPosition(row, i), null);
                moves.add(move);
            }
        }
        row = oRow;
        for (int i = oCol - 1; i < oCol + 2; i++){
            ChessPosition next = new ChessPosition(i, oCol);
            if (i > 0 && i < 9 && row < 9 && (myBoard[row-1][i-1] == null || myBoard[row-1][i-1].getTeamColor() != piece.getTeamColor())) {
                ChessMove move = new ChessMove(position, new ChessPosition(row, i), null);
                moves.add(move);
            }
        }

        return moves;
    }
}
