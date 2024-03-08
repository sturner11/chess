package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import models.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserDAOTests {
    private static UserDAO userDAO;
    private static Game game;

    @BeforeEach
    public void init() throws DataAccessException {
        userDAO = new UserDAO();
        userDAO.clear();
    }


    @Test
    @DisplayName("ClearSuccess")
    public void clearSuccess() throws Exception {
        userDAO.createUser("abcd", "helo", "email");
        userDAO.clear();
        Assertions.assertDoesNotThrow(() -> userDAO.createUser("abcd", "helo", "email"));
    }
    @Test
    @DisplayName("ClearSuccess")
    public void clearMultiple() throws Exception {
        userDAO.createUser("abcd", "helo", "email");
        userDAO.createUser("abcd2", "helo", "email");
        userDAO.createUser("abcd3", "helo", "email");
        userDAO.clear();
        userDAO.clear();
        Assertions.assertDoesNotThrow(() -> userDAO.createUser("abcd", "helo", "email"));
    }

    @Test
    @DisplayName("Fail1")
    public void createFailure() throws Exception {
        userDAO.createUser("abcd", "helo", "email");
        assertThrows(SQLException.class, () -> userDAO.createUser("abcd", "helo", "email"));

    }

    @Test
    @DisplayName("Success2")
    public void createSuccess() throws Exception {
        Assertions.assertDoesNotThrow(() -> userDAO.createUser("abcd", "helo", "email"));
    }

    @Test
    @DisplayName("Fail2")
    public void userExistFailure() throws Exception {
        Assertions.assertFalse(userDAO.userExists("EHllo"));

    }

    @Test
    @DisplayName("Success3.5")
    public void userExistSuccess() throws Exception {
        userDAO.createUser("abcd", "helo", "email");
        Assertions.assertTrue(userDAO.userExists("abcd"));
    }
}


