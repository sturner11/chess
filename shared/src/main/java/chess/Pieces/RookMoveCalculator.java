package chess.Pieces;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class RookMoveCalculator implements PieceMoveCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        int ori_row = position.getRow();
        int ori_col = position.getColumn();
        int row = ori_row;
        int col = ori_col;

        ChessPiece piece = board.getPiece(position);
        ChessPiece otherPiece;
        ChessMove move = null;
        // Front/Back
        while (row < 9){
            if (board.getBoard()[row-1][col-1] == null){
                move = new ChessMove(position, new ChessPosition(row, col), null);
                moves.add(move);
            } else if (board.getBoard()[row-1][col-1].getTeamColor() != piece.getTeamColor()) {
                move = new ChessMove(position, new ChessPosition(row, col), null);
                moves.add(move);
                break;
            } else if (board.getBoard()[row-1][col-1] != piece){ break;}
            row ++;
        }
        row = ori_row;
        while (row > 0 ){
            if (board.getBoard()[row-1][col-1] == null){
                move = new ChessMove(position, new ChessPosition(row, col), null);
                moves.add(move);
            } else if (board.getBoard()[row-1][col-1].getTeamColor() != piece.getTeamColor()) {
                move = new ChessMove(position, new ChessPosition(row, col), null);
                moves.add(move);
                break;
            } else if (board.getBoard()[row-1][col-1] != piece){ break;}
            row --;
        }
        // Sides
        row = ori_row;
        while (col < 9){
            if (board.getBoard()[row-1][col-1] == null){
                move = new ChessMove(position, new ChessPosition(row, col), null);
                moves.add(move);
            } else if (board.getBoard()[row-1][col-1].getTeamColor() != piece.getTeamColor()) {
                move = new ChessMove(position, new ChessPosition(row, col), null);
                moves.add(move);
                break;
            } else if (board.getBoard()[row-1][col-1] != piece){ break;}
            col ++;
        }

        col = ori_col;
        while (col > 0){
            if (board.getBoard()[row-1][col-1] == null){
                move = new ChessMove(position, new ChessPosition(row, col), null);
                moves.add(move);
            } else if (board.getBoard()[row-1][col-1].getTeamColor() != piece.getTeamColor()) {
                move = new ChessMove(position, new ChessPosition(row, col), null);
                moves.add(move);
                break;
            } else if (board.getBoard()[row-1][col-1] != piece){ break;}
            col --;
        }
        return moves;
    }
}
