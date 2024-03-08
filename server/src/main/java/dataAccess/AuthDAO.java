package dataAccess;

import models.Game;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static dataAccess.DatabaseManager.configureDatabase;

public class AuthDAO implements DAO{
    Boolean isInitialized = true;
    public AuthDAO() throws DataAccessException {
        configureDatabase();
    }

    public void clear(){
        if (isInitialized) {
            try (var conn = DatabaseManager.getConnection()) {
                try (var preparedStatement = conn.prepareStatement
                        ("DROP TABLES auth")) {
                    var rs = preparedStatement.execute();
                }
            } catch (SQLException | DataAccessException e) {
                throw new RuntimeException(e);
            }
            isInitialized = false;
        }
    }

    public String createAuth(String username) throws DataAccessException, SQLException {
        String authToken = UUID.randomUUID().toString();
        if (!isInitialized){
            configureDatabase();
            isInitialized = true;
        }
        String sql = "INSERT INTO auth (username, authToken) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement
                    (sql)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, authToken);
                var rs = preparedStatement.executeUpdate();
                if (rs != 1){
                    throw new SQLException("createAuth");
                }
            }
        }
        return authToken;
    }


    public boolean logOutUser(String authToken) {
        String sql = "DELETE FROM auth WHERE authToken = ?" ;
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement
                    (sql)) {
                preparedStatement.setString(1, authToken);
                var rs = preparedStatement.executeUpdate();
                return rs == 1;
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public String checkAuth(String authToken) throws SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement
                    ("SELECT username FROM auth WHERE authToken = " + "'" + authToken + "'")) {
                var rs = preparedStatement.executeQuery();
                rs.next();
                String username =rs.getString("username");
                if (!Objects.equals(username, "")){
                    return username;
                } else {
                    return null;
                }
            }
        } catch (SQLException | DataAccessException e) {
            return null;
        }

    }
}

