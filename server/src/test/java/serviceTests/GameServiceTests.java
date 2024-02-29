package serviceTests;
import dataAccess.DataAccessException;
import models.Game;
import org.junit.jupiter.api.*;
import services.GameService;
import services.UserService;

import java.util.ArrayList;


public class GameServiceTests {
    private static GameService gameService;
    private static Game game;

    @BeforeAll
    public static void init() {
        gameService = new GameService();
    }
    @BeforeEach
    public void gameSetup() throws DataAccessException {
        game = gameService.createGame("authToken", "gameName");
    }

    @Test
    @DisplayName("Success")
    public void createGameSuccess() throws Exception {
        Game test = new Game("gameName", game.gameID());
        Assertions.assertEquals(game, test);
    }

    @Test
    @DisplayName("Game name already exists")
    public void createGameFail() throws Exception {
         Assertions.assertThrows(DataAccessException.class, () -> gameService.createGame("authToken", null));
    }

    @Test
    @DisplayName("Success")
    public void joinGameSuccess() throws Exception {
        Game game1 = gameService.joinGame("authToken", "WHITE", game.gameID(), "username");
        Game test = new Game("gameName", game1.gameID());
        test.setWhiteUsername("username");
        Assertions.assertEquals(test, game1);
    }

    @Test
    @DisplayName("COLOR DOESN'T EXIT")
    public void joinGameFail() throws Exception {
        Game game1 = gameService.joinGame("authToken", "GREEN", game.gameID(), "username");
        Game test = new Game("gameName", game1.gameID());
        test.setWhiteUsername("username");
        Assertions.assertEquals(test, game1);
    }

    @Test
    @DisplayName("Success")
    public void listGameSuccess() throws Exception {
        ArrayList<Game> games = gameService.listGames();
        ArrayList<Game> test = new ArrayList<>();
        test.add(new Game("gameName", game.gameID()));
        Assertions.assertEquals(test, games);
    }

    @Test
    @DisplayName("Empty")
    public void listGameEmpty() throws Exception {
        gameService.clear();
        ArrayList<Game> games = gameService.listGames();
        ArrayList<Game> test = new ArrayList<>();
        Assertions.assertEquals(games, test);
    }


}
