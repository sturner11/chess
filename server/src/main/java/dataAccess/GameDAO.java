package dataAccess;

import models.Game;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static dataAccess.DatabaseManager.configureDatabase;

public class GameDAO implements DAO{
    Integer currentId = 1;
    Boolean isInitialized= true;
    public GameDAO() throws DataAccessException {
        configureDatabase();
    }

    public void clear(){
        if (isInitialized) {
            try (var conn = DatabaseManager.getConnection()) {
                try (var preparedStatement = conn.prepareStatement
                        ("DROP TABLES games")) {
                    var rs = preparedStatement.execute();
                }
            } catch (SQLException | DataAccessException e) {
                throw new RuntimeException(e);
            }
            isInitialized = false;
            currentId = 1;
        }
    }

    public Game createGame(String authToken, String gameName) throws DataAccessException {
        if (!isInitialized){
            configureDatabase();
            isInitialized = true;
        }
        String sql ="INSERT INTO games (gameId, gameName) VALUES (NULL, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement
                    (sql); ) {
                preparedStatement.setString(1, gameName);
                var rs = preparedStatement.executeUpdate();
                if (rs != 1){
                    throw new SQLException("Game creation failed");
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        Game game = new Game(gameName, currentId);
        Integer gameID = currentId;
        currentId ++;
        return game;
    }

    public Game joinGame(String authToken, String playerColor, Integer gameId, String username) throws DataAccessException, SQLException {

        if (gameExists(gameId)){
            Game game = getGame(gameId);
            if (playerColor != null) {
                if (teamAvailable(playerColor, gameId)) {
                    game.setColor(playerColor, username);
                    setColor(gameId, username, playerColor);
                    return game;
                } else {
                    throw new DataAccessException("error", 403);
                }
            }
            else {
                return game;
            }
        } else {
            throw new DataAccessException("error", 400);
        }
    }

    private boolean teamAvailable(String playerColor, Integer gameId) throws SQLException, DataAccessException {
        boolean isAvailable;
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement
                    ("SELECT " + playerColor + " FROM games WHERE gameId = " + "'" + gameId + "'")) {
                var rs = preparedStatement.executeQuery();
                rs.next();
                isAvailable = rs.getString(playerColor) == null;
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        if (isAvailable){
            return true;
        } else {
            throw new DataAccessException("error", 403);
        }
    }

    private void setColor(Integer gameId, String username, String playerColor) throws DataAccessException {
        String sql = "UPDATE games SET " + playerColor + "=" + "'" + username + "'" + "WHERE gameId =   " + gameId;
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement
                    (sql)) {
                var rs = preparedStatement.executeUpdate();
                if (rs != 1){
                    throw new SQLException("User insert failed");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Game getGame(Integer gameId) throws SQLException {
        if (!isInitialized){
            throw new SQLException("Game Doesn't Exist");
        }
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement
                    ("SELECT * FROM games WHERE gameId = " + "'" + gameId + "'")) {
                var rs = preparedStatement.executeQuery();
                rs.next();
                return new Game(rs.getString("gameName"), rs.getInt("gameId"));
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean gameExists(Integer gameId) {
        if (!isInitialized){
            return false;
        }
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement
                    ("SELECT gameId FROM games WHERE gameId = " + "'" + gameId + "'")) {
                var rs = preparedStatement.executeQuery();
                if( rs.next()) {
                    return true;
                } else {
                    return false;
                }

            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Game> listGames() throws SQLException {
        ArrayList<Game> games = new ArrayList<>();
        if (isInitialized) {
            try (var conn = DatabaseManager.getConnection()) {
                try (var preparedStatement = conn.prepareStatement
                        ("SELECT * FROM games")) {
                    var rs = preparedStatement.executeQuery();
                    while (rs.next()) {
                        Game game = new Game(rs.getString("gameName"), rs.getInt("gameId"));
                        game.setColor("WHITE", rs.getString("WHITE"));
                        game.setColor("BLACK", rs.getString("BLACK"));
                        games.add(game);
                    }
                }
            } catch (SQLException | DataAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return games;
    }


}
