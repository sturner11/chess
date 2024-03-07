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

    }

    public boolean userExists(String user) {
        return true;
    }

    public void createUser(String username, String password, String email) throws DataAccessException, SQLException {
        String encryptedPass = encryptUserPassword(password);
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement
                    ("INSERT INTO users (username, password, email) VALUES (" + username + ", '" + encryptedPass + "', " + email + ")")) {
                var rs = preparedStatement.executeUpdate();
                System.out.println(rs);
            }
        }
    }

    public boolean authenticate(String username, String password) {
        return true;
    }






}
