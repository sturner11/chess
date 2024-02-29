package chess.Pieces;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class DiagMovement {

    public static Collection<ChessMove> move(ChessBoard board, ChessPosition position) {
        HashSet<ChessMove> moves = new HashSet<ChessMove>() ;
        int oriRow = position.getRow();
        int oriCol = position.getColumn();
        int row = oriRow;
        int col = oriCol;
        ChessMove move = null;
        ChessPiece currentPiece = board.getBoard()[row-1][col-1];
        ChessPiece otherPiece = null;
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
        row = oriRow;
        col = oriCol;
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
        row = oriRow;
        col = oriCol;
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
        row = oriRow;
        col = oriCol;
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
