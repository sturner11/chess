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
        HashSet<ChessMove> moves = new HashSet<ChessMove>() ;
        int ori_row = position.getRow();
        int ori_col = position.getColumn();
        int row = ori_row - 1;
        int col = ori_col - 1;
        int rowCount = 0;
        int colCount = 0;
        ChessMove move = null;
        ChessPiece currentPiece = board.getBoard()[ori_row-1][ori_col-1];

        //TODO: Change Row Count and otherPiece grabbing
        // NE
        while (row < 9 && rowCount < 3){
            col = ori_col - 1;
            colCount = 0;
            if (row < 0){
                continue;
            }
            while (col < 9 && colCount < 3) {
                if (col < 0){ continue;}
                ChessPiece otherPiece = board.getBoard()[row-1][col-1];
                if ((otherPiece != null && otherPiece.getTeamColor() == currentPiece.getTeamColor())){
                    col++;
                    colCount++;
                    continue;
                }
                move = new ChessMove(position, new ChessPosition(row, col), null);
                moves.add(move);
                col++;
                colCount++;
            }
            row++;
            rowCount++;
        }
        return moves;
    }
}
