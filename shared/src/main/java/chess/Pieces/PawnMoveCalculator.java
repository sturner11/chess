package chess.Pieces;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class PawnMoveCalculator implements PieceMoveCalculator{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        int ori_row = position.getRow();
        int ori_col = position.getColumn();
        ChessPiece currentPiece = board.getBoard()[ori_row-1][ori_col-1];
        int row = (currentPiece.getTeamColor()  == ChessGame.TeamColor.WHITE ? ori_row + 1 : ori_row - 1); // Black is going down
        int col = ori_col - 1;
        ChessPiece otherPiece = board.getBoard()[row-1][col-1];
        ChessMove move = null;
        boolean frontClear = false;
        boolean firstMove = ((currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE && ori_row == 2) || (currentPiece.getTeamColor() == ChessGame.TeamColor.BLACK && ori_row == 7));
        // Forward  Left Diag
        if ((otherPiece != null && otherPiece.getTeamColor() != currentPiece.getTeamColor())){
            move = new ChessMove(position, new ChessPosition(row, col), null);
            moves.add(move);
        }
        col++;
        otherPiece = board.getBoard()[row-1][col-1];
        //Forward
        if (otherPiece == null){
            move = new ChessMove(position, new ChessPosition(row, col), null);
            moves.add(move);
            frontClear = true;
        }
        col ++;
        otherPiece = board.getBoard()[row-1][col-1];
        if ((otherPiece != null && otherPiece.getTeamColor() != currentPiece.getTeamColor())){
            move = new ChessMove(position, new ChessPosition(row, col), null);
            moves.add(move);
        }
        // Double movement
        col--;
        row = (currentPiece.getTeamColor()  == ChessGame.TeamColor.WHITE ? row + 1 : row - 1); //Black moves down
        otherPiece = board.getBoard()[row-1][col-1];

        if (otherPiece == null && frontClear && firstMove){
            move = new ChessMove(position, new ChessPosition(row, col), null);
            moves.add(move);
        }
        return moves;
    }
}
