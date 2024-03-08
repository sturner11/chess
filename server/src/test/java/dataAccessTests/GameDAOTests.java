package dataAccessTests;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import models.Game;
import org.junit.jupiter.api.*;
import services.GameService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class GameDAOTests {
    private static GameDAO gameDAO;
    private static Game game;

    @BeforeEach
    public void init() throws DataAccessException {
        gameDAO = new GameDAO();
        gameDAO.clear();
    }
    @Test
    @DisplayName("createSuccess")
    public void createGame() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> gameDAO.createGame("abcd", "gameName"));
    }

    @Test
    @DisplayName("createFail")
    public void createGameSuccess2() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> gameDAO.createGame("abcd", "gameName"));
        Assertions.assertDoesNotThrow(() -> gameDAO.createGame("abcd", "gameName"));
    }


    @Test
    @DisplayName("Success")
    public void joinGameSuccess() throws Exception {
        Game game = gameDAO.createGame("abcd", "gameName");
        gameDAO.joinGame("abcd", "WHITE", game.gameID(), "useraname");
    }

    @Test
    @DisplayName("Already there")
    public void joinGameFail() throws Exception {
        Game game = gameDAO.createGame("abcd", "gameName");
        gameDAO.joinGame("abcd", "WHITE", game.gameID(), "useraname");
        assertThrows(DataAccessException.class, () -> gameDAO.joinGame("abcd", "WHITE", game.gameID(), "useraname") );
    }

    @Test
    @DisplayName("Success")
    public void listGameSuccess() throws Exception {
        gameDAO.createGame("abcd", "gameName");
        ArrayList<Game> games = gameDAO.listGames();
        ArrayList<Game> test = new ArrayList<>();
        test.add(new Game("gameName", 1));
        Assertions.assertEquals(test, games);
    }

    @Test
    @DisplayName("Empty")
    public void listGameEmpty() throws Exception {
        gameDAO.clear();
        ArrayList<Game> games = gameDAO.listGames();
        ArrayList<Game> test = new ArrayList<>();
        Assertions.assertEquals(games, test);
    }

    @Test
    @DisplayName("Success")
    public void clearSuccess() throws Exception {
        gameDAO.createGame("abcd", "helo");
        gameDAO.clear();
        ArrayList<Game> games = gameDAO.listGames();
        ArrayList<Game> test = new ArrayList<>();
        Assertions.assertEquals(games, test);
    }




}
