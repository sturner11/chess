package chess.Pieces;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class KnightMoveCalculator implements PieceMoveCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        int oriRow = position.getRow();
        int oricol = position.getColumn();
        int row = oriRow + 2;
        int col = oricol + 1;
        int rowCount = 0;
        int colCount = 0;
        ChessMove move = null;
        ChessPiece currentPiece = board.getBoard()[oriRow - 1][oricol - 1];
        // North moves
        if (row < 8 && col < 8 && otherTeam(row, col, board, currentPiece.getTeamColor())) {
            move = new ChessMove(position, new ChessPosition(row, col), null);
            moves.add(move);
        }
        col = col - 2;
        if (col > 0 && otherTeam(row, col, board, currentPiece.getTeamColor())){
            move = new ChessMove(position, new ChessPosition(row, col), null);
            moves.add(move);
        }


        // South Moves
        row = row -4;
        if (row > 0 && col > 0 && otherTeam(row, col, board, currentPiece.getTeamColor())){
            move = new ChessMove(position, new ChessPosition(row, col), null);
            moves.add(move);
        }
        col = col + 2;
        if (col < 8 && otherTeam(row, col, board, currentPiece.getTeamColor())){
            move = new ChessMove(position, new ChessPosition(row, col), null);
            moves.add(move);
        }

        // East Moves
        row++;
        col++;
        if (row < 8 && row > 0 && col < 8 && otherTeam(row, col, board, currentPiece.getTeamColor())){
            move = new ChessMove(position, new ChessPosition(row, col), null);
            moves.add(move);
        }
        row = row + 2;
        if (row < 8 && otherTeam(row, col, board, currentPiece.getTeamColor())){
            move = new ChessMove(position, new ChessPosition(row, col), null);
            moves.add(move);
        }

        //West Moves
        col = col - 4;
        if (row < 8 && row > 0 && col > 0 && otherTeam(row, col, board, currentPiece.getTeamColor())){
            move = new ChessMove(position, new ChessPosition(row, col), null);
            moves.add(move);
        }
        row = row - 2;
        if (row > 0 && otherTeam(row, col, board, currentPiece.getTeamColor())){
            move = new ChessMove(position, new ChessPosition(row, col), null);
            moves.add(move);
        }
        return moves;
    }

    private boolean otherTeam(int row, int col, ChessBoard board, ChessGame.TeamColor teamColor) {
        if (row > 0 && row < 8 && col > 0 && col < 9) {
            return board.getBoard()[row-1][col-1] == null || board.getBoard()[row-1][col-1].getTeamColor() != teamColor;
        }
        return false;
    }
}
