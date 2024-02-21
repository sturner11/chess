package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final ChessPiece[][] board = new ChessPiece[8][8];
    private ChessPosition blackKingPos;
    private ChessPosition whiteKingPos;

    public ChessBoard() {
        for (int i = 0; i < 8; i++) {
            ChessPiece[] row = new ChessPiece[8];
            for (int j = 0; j < 8; j++) {
                row[j] = null;
            }
            board[i] = row;
        }
    }

    public ChessBoard(ChessBoard another) {
        this.whiteKingPos = another.whiteKingPos;
        this.blackKingPos = another.blackKingPos;
        for (int i = 0; i < 8; i++) {
            for (int j= 0; j < 8; j++) {
                this.board[i][j] = another.getBoard()[i][j];
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder boardString = new StringBuilder();
        for (int i = 7; i >= 0; i--){
            ChessPiece[] row = this.board[i];
            for (ChessPiece piece: row){
                boardString.append("|");
                if (piece != null) {
                    boardString.append(piece);
                }
                else {
                    boardString.append(" ");
                }
            }
            boardString.append("|\n");
        }
        return boardString.toString();
    }

    public ChessPiece[][] getBoard(){
        return board;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        if (board[position.getRow()-1][position.getColumn()-1] != null) {
            throw new Error("Piece not added, position already filled");
        }
        board[position.getRow()-1][position.getColumn()-1] = piece;
        if (piece.getPieceType() == ChessPiece.PieceType.KING){
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                this.whiteKingPos = position;
            } else{
                this.blackKingPos = position;
            }
        }
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()-1][position.getColumn()-1];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return toString().equals(that.toString());
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // Pawns
        for (int i =0; i < board.length; i++){
            board[1][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            board[6][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }
        // Rooks
        board[0][0] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        board[0][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        board[7][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        board[7][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        //Knights
        board[0][1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        board[0][6] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        board[7][1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        board[7][6] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        //Bishops
        board[0][2] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        board[0][5] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        board[7][2] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        board[7][5] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        //Kings and Queens
        board[0][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        board[0][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        board[7][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        board[7][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        this.whiteKingPos = new ChessPosition(1,5);
        this.blackKingPos = new ChessPosition(8,5);
    }

    public void setPiece(ChessMove move) {
        int startRow = move.getStartPosition().getRow();
        int startCol = move.getStartPosition().getColumn();
        int endRow = move.getEndPosition().getRow();
        int endCol = move.getEndPosition().getColumn();
        this.board[endRow - 1][endCol-1] = this.board[startRow - 1][startCol-1];
        this.board[startRow - 1][startCol-1] = null;
        if (this.board[endRow - 1][endCol-1] != null &&
                this.board[endRow - 1][endCol-1].getPieceType() == ChessPiece.PieceType.KING){
            switch (this.board[endRow - 1][endCol-1].getTeamColor()){
                case ChessGame.TeamColor.WHITE:
                    this.whiteKingPos = new ChessPosition(endRow, endCol);
                case ChessGame.TeamColor.BLACK:
                    this.blackKingPos = new ChessPosition(endRow, endCol);
            }
        }
        if (this.board[endRow - 1][endCol-1] != null && move.getPromotionPiece() != null){
            this.board[endRow - 1][endCol-1] =
                    new ChessPiece(this.board[endRow - 1][endCol-1].getTeamColor(), move.getPromotionPiece());
        }
    }

    public ChessPosition getKingPos(ChessGame.TeamColor teamColor) {
        return switch (teamColor) {
            case ChessGame.TeamColor.WHITE -> this.whiteKingPos;
            case ChessGame.TeamColor.BLACK -> this.blackKingPos;
        };
    }
}
