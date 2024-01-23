package chess.Pieces;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.*;

public class BishopMoveCalculator implements PieceMoveCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        HashSet<ChessMove> moves = new HashSet<ChessMove>() ;
        int ori_row = position.getRow();
        int ori_col = position.getColumn();
        int row = ori_row;
        int col = ori_col;
        ChessMove move = null;
        ChessPiece currentPiece = board.getBoard()[row-1][col-1];
        ChessPiece otherPiece;
        // NE
        while (row < 8 && col < 8) {
            row ++;
            col ++;
            otherPiece = board.getBoard()[row-1][col-1];
            if (otherPiece != null && otherPiece.getTeamColor() == currentPiece.getTeamColor()){
                break;
            }
            move = new ChessMove(position, new ChessPosition(row,col), null);
            moves.add(move);
            if (otherPiece != null){
                break;
            }
        }
        row = ori_row;
        col = ori_col;
        // NW TODO Check this? All row > 1
        while (row > 1 && col < 8) {
            row --;
            col ++;
            otherPiece = board.getBoard()[row-1][col-1];
            if (otherPiece != null && otherPiece.getTeamColor() == currentPiece.getTeamColor()){
                break;
            }
            move = new ChessMove(position, new ChessPosition(row,col), null);
            moves.add(move);
            if (otherPiece != null){
                break;
            }
        }
        row = ori_row;
        col = ori_col;
        // SE
        while (col > 1 && row < 8) {
            row ++;
            col--;
            otherPiece = board.getBoard()[row-1][col-1];
            if (otherPiece != null && otherPiece.getTeamColor() == currentPiece.getTeamColor()){
                break;
            }
            move = new ChessMove(position, new ChessPosition(row,col), null);
            moves.add(move);
            if (otherPiece != null){
                break;
            }
        }
        row = ori_row;
        col = ori_col;
        // SW
        while (col > 1 && row > 1) {
            row--;
            col--;
            otherPiece = board.getBoard()[row-1][col-1];
            if (otherPiece != null && otherPiece.getTeamColor() == currentPiece.getTeamColor()){
                break;
            }
            move = new ChessMove(position, new ChessPosition(row,col), null);
            moves.add(move);
            if (otherPiece != null){
                break;
            }
        }
        return moves;
    }
}
