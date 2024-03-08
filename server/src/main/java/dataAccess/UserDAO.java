package dataAccess;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static dataAccess.DatabaseManager.configureDatabase;

public class UserDAO implements DAO{
    DatabaseManager dbManager;
    public UserDAO() throws DataAccessException {
        configureDatabase();
    }

    public void clear(){
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement
                    ("DROP TABLES users")) {
                var rs = preparedStatement.execute();


            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean userExists(String user) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement
                    ("SELECT username FROM users WHERE username = " + "'" + user + "'")) {
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

    public void createUser(String username, String password, String email) throws DataAccessException, SQLException {
        String encryptedPass = encryptUserPassword(password);
        String sql = "INSERT INTO users (username, password, email) VALUES (" + "'" + username + "', '" + encryptedPass + "' ,'" + email + "')";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement
                    (sql)) {
                var rs = preparedStatement.executeUpdate();
                if (rs != 1){
                    throw new SQLException("User insert failed");
                }
            }
        }
    }

    public boolean authenticate(String username, String password) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement
                    ("SELECT password FROM users WHERE username = " + "'" + username + "'")) {
                var rs = preparedStatement.executeQuery();
                return rs.next() && passwordMatch(password, rs.getString("password"));

            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }






}
