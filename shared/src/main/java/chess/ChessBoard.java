package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPosition[][] board = new ChessPosition[8][8];
    public ChessBoard() {
        for (int i = 0; i < 8; i++) {
            ChessPosition[] row = new ChessPosition[8];
            for (int j = 0; j < 8; j++) {
                ChessPosition temp = new ChessPosition(i+1, j+1);
                row[j] = temp;
            }
            board[i] = row;
        }
//        resetBoard();
        //TODO Should we always start with a fresh board?
    }

    public ChessPosition[][] getBoard(){
        return board;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
//        if (position.hasPiece) {
//            throw new Error("Piece not added, position already filled");
//        }
//        position.hasPiece = true;
//        position.piece = piece;
        board[position.getRow()-1][position.getColumn()-1] = position;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()-1][position.getColumn()-1].piece;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (ChessPosition[] row: this.board){
            switch(row[0].getRow()){
                case 0:
                    setBackRow(row, ChessGame.TeamColor.WHITE);
                    break;
                case 1:
                    for (ChessPosition position: row) { // Add White Pawns
                        position.piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
                        break;
                    }
                case 2:
                case 3:
                case 4:
                case 5:
                    for (ChessPosition position: row){
                        position.hasPiece = false;
                        position.piece = null;
                    }
                    break;
                case 6:
                    for (ChessPosition position: row) { // Add White Pawns
                        position.piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
                        break;
                    }
                case 7:
                    setBackRow(row, ChessGame.TeamColor.BLACK);
                    break;
            }
        }
    }

    private void setBackRow(ChessPosition[] row, ChessGame.TeamColor color) {
        for (ChessPosition position: row){
            switch (position.getColumn()) {
                case 0:
                    position.hasPiece = true;
                    position.piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);

                    break;
                case 1:
                    position.hasPiece = true;
                    position.piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
                    break;
                case 2:
                    position.hasPiece = true;
                    position.piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
                    break;
                case 3:
                    position.hasPiece = true;
                    position.piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
                    break;
                case 4:
                    position.hasPiece = true;
                    position.piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
                    break;
                case 5:
                    position.hasPiece = true;
                    position.piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
                    break;
                case 6:
                    position.hasPiece = true;
                    position.piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
                    break;
                case 7:
                    position.hasPiece = true;
                    position.piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
                    break;
            }
        }

    }

}
