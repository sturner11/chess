package chess.Pieces;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class PawnMoveCalculator implements PieceMoveCalculator{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        HashSet<ChessMove> moves = new HashSet<>();
        int oRow = position.getRow();
        int oCol = position.getColumn();
        ChessPiece[][] myBoard = board.getBoard();
        ChessPiece piece = myBoard[oRow - 1][oCol - 1];
        // WHITE
        if (myBoard[oRow - 1][oCol - 1].getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (oCol - 2 >= 0 && myBoard[oRow][oCol - 2] != null && myBoard[oRow][oCol - 2].getTeamColor() != piece.getTeamColor()) {
                if (oRow + 1 == 8) {
                    ChessMove move = new ChessMove(position, new ChessPosition(oRow + 1, oCol - 1), ChessPiece.PieceType.ROOK);
                    moves.add(move);
                    move = new ChessMove(position, new ChessPosition(oRow + 1, oCol - 1), ChessPiece.PieceType.KNIGHT);
                    moves.add(move);
                    move = new ChessMove(position, new ChessPosition(oRow + 1, oCol - 1), ChessPiece.PieceType.BISHOP);
                    moves.add(move);
                    move = new ChessMove(position, new ChessPosition(oRow + 1, oCol - 1), ChessPiece.PieceType.QUEEN);
                    moves.add(move);

                } else {
                    ChessMove move = new ChessMove(position, new ChessPosition(oRow + 1, oCol - 1), null);
                    moves.add(move);
                }
            }
            if (myBoard[oRow][oCol - 1] == null) {
                if (oRow == 2 && myBoard[oRow + 1][oCol - 1] == null) {
                    ChessMove move = new ChessMove(position, new ChessPosition(oRow + 2, oCol), null);
                    moves.add(move);
                }
                if (oRow + 1 == 8) {
                    ChessMove move = new ChessMove(position, new ChessPosition(oRow + 1, oCol), ChessPiece.PieceType.ROOK);
                    moves.add(move);
                    move = new ChessMove(position, new ChessPosition(oRow + 1, oCol), ChessPiece.PieceType.KNIGHT);
                    moves.add(move);
                    move = new ChessMove(position, new ChessPosition(oRow + 1, oCol), ChessPiece.PieceType.BISHOP);
                    moves.add(move);
                    move = new ChessMove(position, new ChessPosition(oRow + 1, oCol), ChessPiece.PieceType.QUEEN);
                    moves.add(move);
                } else {
                    ChessMove move = new ChessMove(position, new ChessPosition(oRow + 1, oCol), null);
                    moves.add(move);
                }
            }
            if (oCol + 2 < 8 && myBoard[oRow][oCol] != null && myBoard[oRow][oCol].getTeamColor() != piece.getTeamColor()) {
                if (oRow + 1 == 8) {
                    ChessMove move = new ChessMove(position, new ChessPosition(oRow, oCol + 1), ChessPiece.PieceType.ROOK);
                    moves.add(move);
                    move = new ChessMove(position, new ChessPosition(oRow + 1, oCol + 1), ChessPiece.PieceType.KNIGHT);
                    moves.add(move);
                    move = new ChessMove(position, new ChessPosition(oRow + 1, oCol + 1), ChessPiece.PieceType.BISHOP);
                    moves.add(move);
                    move = new ChessMove(position, new ChessPosition(oRow + 1, oCol + 1), ChessPiece.PieceType.QUEEN);
                    moves.add(move);

                } else {
                    ChessMove move = new ChessMove(position, new ChessPosition(oRow, oCol + 1), null);
                    moves.add(move);
                }
            }
        } else { //BLACK
            {
                if (oCol - 2 >= 0 && myBoard[oRow - 2][oCol - 2] != null && myBoard[oRow - 2][oCol - 2].getTeamColor() != piece.getTeamColor()) {
                    if (oRow - 1 == 1) {
                        ChessMove move = new ChessMove(position, new ChessPosition(oRow - 1, oCol - 1), ChessPiece.PieceType.ROOK);
                        moves.add(move);
                        move = new ChessMove(position, new ChessPosition(oRow - 1, oCol - 1), ChessPiece.PieceType.KNIGHT);
                        moves.add(move);
                        move = new ChessMove(position, new ChessPosition(oRow - 1, oCol - 1), ChessPiece.PieceType.BISHOP);
                        moves.add(move);
                        move = new ChessMove(position, new ChessPosition(oRow - 1, oCol - 1), ChessPiece.PieceType.QUEEN);
                        moves.add(move);

                    } else {
                        ChessMove move = new ChessMove(position, new ChessPosition(oRow - 1, oCol - 1), null);
                        moves.add(move);
                    }
                }
                if (myBoard[oRow - 2][oCol - 1] == null) {
                    if (oRow == 7 && myBoard[oRow - 3][oCol - 1] == null) {
                        ChessMove move = new ChessMove(position, new ChessPosition(oRow - 2, oCol), null);
                        moves.add(move);
                    }
                    if (oRow - 1 == 1) {
                        ChessMove move = new ChessMove(position, new ChessPosition(oRow - 1, oCol), ChessPiece.PieceType.ROOK);
                        moves.add(move);
                        move = new ChessMove(position, new ChessPosition(oRow - 1, oCol), ChessPiece.PieceType.KNIGHT);
                        moves.add(move);
                        move = new ChessMove(position, new ChessPosition(oRow - 1, oCol), ChessPiece.PieceType.BISHOP);
                        moves.add(move);
                        move = new ChessMove(position, new ChessPosition(oRow - 1, oCol), ChessPiece.PieceType.QUEEN);
                        moves.add(move);
                    } else {
                        ChessMove move = new ChessMove(position, new ChessPosition(oRow - 1, oCol), null);
                        moves.add(move);
                    }
                }
                if (oCol + 2 < 8 && myBoard[oRow - 2][oCol] != null && myBoard[oRow - 2][oCol].getTeamColor() != piece.getTeamColor()) {
                    if (oRow - 1 == 1) {
                        ChessMove move = new ChessMove(position, new ChessPosition(oRow - 1, oCol + 1), ChessPiece.PieceType.ROOK);
                        moves.add(move);
                        move = new ChessMove(position, new ChessPosition(oRow - 1, oCol + 1), ChessPiece.PieceType.KNIGHT);
                        moves.add(move);
                        move = new ChessMove(position, new ChessPosition(oRow - 1, oCol + 1), ChessPiece.PieceType.BISHOP);
                        moves.add(move);
                        move = new ChessMove(position, new ChessPosition(oRow - 1, oCol + 1), ChessPiece.PieceType.QUEEN);
                        moves.add(move);

                    } else {
                        ChessMove move = new ChessMove(position, new ChessPosition(oRow - 1, oCol + 1), null);
                        moves.add(move);
                    }
                }
            }
        }

        return moves;
    }
}
