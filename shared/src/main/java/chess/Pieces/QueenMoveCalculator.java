package chess.Pieces;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

import static chess.Pieces.DiagMovement.move;

public class QueenMoveCalculator implements PieceMoveCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        int oriRow = position.getRow();
        int oriCol = position.getColumn();
        int row = oriRow;
        int col = oriCol;

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
        oriRow = position.getRow();
        oriCol = position.getColumn();
        row = oriRow;
        col = oriCol;
        move = null;
        ChessPiece currentPiece = board.getBoard()[row-1][col-1];
        otherPiece = null;
        HashSet<ChessMove> bishopMoves = (HashSet<ChessMove>) DiagMovement.move(board, position);

        moves.addAll(bishopMoves);
        return moves;
    }
}
