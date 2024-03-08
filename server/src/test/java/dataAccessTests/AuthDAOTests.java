package dataAccessTests;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;

import models.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthDAOTests {
    private static AuthDAO authDAO;
    private static Game game;

    @BeforeEach
    public void init() throws DataAccessException {
        authDAO = new AuthDAO();
        authDAO.clear();
    }
    @Test
    @DisplayName("Success1")
    public void clear() throws Exception {
        authDAO.createAuth("user");
        authDAO.clear();
        Assertions.assertDoesNotThrow(() -> authDAO.createAuth("user"));
    }
    @Test
    @DisplayName("Success1.5")
    public void multiClear() throws Exception {
        authDAO.createAuth("user");
        authDAO.createAuth("user2");
        authDAO.createAuth("user3");
        authDAO.clear();
        authDAO.clear();
        Assertions.assertDoesNotThrow(() -> authDAO.createAuth("user"));
    }
    @Test
    @DisplayName("Success2")
    public void createSuccess2() throws Exception {
        authDAO.createAuth("user");
        Assertions.assertDoesNotThrow( () -> authDAO.createAuth("user"));
    }
    @Test
    @DisplayName("Success2")
    public void createSuccess1() throws Exception {
        Assertions.assertDoesNotThrow( () -> authDAO.createAuth("user"));
    }
    @Test
    @DisplayName("Success3")
    public void logoutSuccess() throws Exception {
        assertTrue(authDAO.logOutUser(authDAO.createAuth("user")));
    }
    @Test
    @DisplayName("Fail3")
    public void logoutFailure() throws Exception {
        authDAO.createAuth("user");
        Assertions.assertFalse(authDAO.logOutUser("name"));
    }
    @Test
    @DisplayName("Success4")
    public void checkAuthSuccess() throws Exception {
        Assertions.assertEquals(authDAO.checkAuth(authDAO.createAuth("user")), "user");



    }
    @Test
    @DisplayName("Fail4")
    public void checkAuthFailure() throws Exception {
        Assertions.assertNotEquals(authDAO.checkAuth(authDAO.createAuth("user")), "user1");
    }
}
