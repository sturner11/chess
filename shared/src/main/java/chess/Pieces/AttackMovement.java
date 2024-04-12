package chess.Pieces;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.HashSet;

public class AttackMovement {

    public static HashSet<ChessMove> movement(ChessPosition position, ChessBoard board) {
        HashSet<ChessMove> moves = new HashSet<>();
        int oriRow = position.getRow();
        int oriCol = position.getColumn();

        int row = oriRow;
        int col = oriCol;

        ChessPiece piece = board.getPiece(position);
        ChessMove move;
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
        row = oriRow;
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
        row = oriRow;
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

        col = oriCol;
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
