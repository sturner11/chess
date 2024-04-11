package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    ChessBoard board;
    TeamColor teamTurn;
    public ChessGame() {
        this.board = new ChessBoard();
        this.teamTurn = null;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {this.teamTurn = team;}

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    public Collection<ChessMove> validMoves(ChessPosition startPosition, ChessGame.TeamColor color) {

        Collection<ChessMove> validMoves = new HashSet<ChessMove>();
        if (color != teamTurn){
            return validMoves;
        }
        ChessPiece piece = this.board.getBoard()[startPosition.getRow() - 1][startPosition.getColumn() - 1];
        if (color == null){ // in Some test they forget to set Whose turn it is.
            color = piece.getTeamColor();
        }
        if (piece != null && piece.getTeamColor() == color) {
            Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
            for (ChessMove move : moves) {
                ChessBoard test = new ChessBoard(this.board);
                test.setPiece(move);
                boolean addToMoves = true;
                for (int i = 1; i < 9; i++){
                    for (int j = 1; j < 9; j++){
                        ChessPiece testPiece = test.getBoard()[i-1][j-1];
                        if (testPiece != null &&
                                testPiece.getTeamColor() != color){
                            ChessMove kingMove = new ChessMove(new ChessPosition(i,j), test.getKingPos(color),
                                    null);
                            Collection<ChessMove> testMoves = piece.pieceMoves(test, new ChessPosition(i,j));
                            if (testMoves.contains(kingMove)){
                                addToMoves = false;
                            }
                        }
                    }
                }
                if (addToMoves){
                    validMoves.add(move);
                }
            }
        }
        return validMoves;
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) { // Parameter Overloading because they won't
        // let us change headers
        return validMoves(startPosition, this.teamTurn);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> moves = validMoves(move.getStartPosition());
        if (moves.contains(move)){
            this.board.setPiece(move);
            if (this.teamTurn == TeamColor.WHITE){
                this.teamTurn = TeamColor.BLACK;
            } else{
                this.teamTurn = TeamColor.WHITE;
            }
        }
        else {
            throw new InvalidMoveException();
        }
    }

    @Override
    public String toString() {
        return  board +
                ", teamTurn=" + teamTurn +
                '\n';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                ChessPiece piece = board.getBoard()[i-1][j-1];
                if ( piece != null && piece.getTeamColor() != teamColor){
                    if (piece.getPieceType() == ChessPiece.PieceType.PAWN){
                        if ((piece.getTeamColor() == TeamColor.BLACK && i == 2) ||
                                (piece.getTeamColor() == TeamColor.WHITE && i == 7)){
                            ChessMove kingMove =
                                    new ChessMove(new ChessPosition(i, j), this.board.getKingPos(teamColor), ChessPiece.PieceType.QUEEN);
                            Collection<ChessMove> moves = piece.pieceMoves(board, new ChessPosition(i,j));
                            if (moves.contains(kingMove)){
                                return true;
                            }
                        }

                    } else {
                        ChessMove kingMove = new ChessMove(new ChessPosition(i, j), this.board.getKingPos(teamColor),
                                null);
                        Collection<ChessMove> moves = piece.pieceMoves(board, new ChessPosition(i,j));
                        if (moves.contains(kingMove)){
                            return true;
                        }
                    }

                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && noSafeMoves(teamColor);
    }

    private boolean noSafeMoves(TeamColor teamColor) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessBoard test = new ChessBoard(this.board);
                ChessPiece testPiece = test.getBoard()[i - 1][j - 1];
                if (testPiece != null && testPiece.getTeamColor() == teamColor){
                    Collection<ChessMove> moves = validMoves(new ChessPosition(i,j));
                    if (moves != null){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return !possibleToMove(teamColor);
        }
       return false;
    }

    public boolean possibleToMove(TeamColor teamColor){
        boolean possibleToMove = false;
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                ChessPiece piece = board.getBoard()[i-1][j-1];
                if (piece != null &&
                        piece.getTeamColor() == teamColor){
                    ChessPosition piecePosition = new ChessPosition(i, j);
                    Collection<ChessMove> moves = validMoves(piecePosition, teamColor);
                    if (!moves.isEmpty()){
                        possibleToMove = true;
                    }
                }
            }
        }
        return possibleToMove;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
