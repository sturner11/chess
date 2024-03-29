import chess.*;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import server.Server;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws DataAccessException, SQLException {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
        Server server = new Server();
        server.run(8080);

    }
}