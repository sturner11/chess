package services;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import models.Games;

import java.util.ArrayList;
import java.util.Map;

public class GameService {

    GameDAO gameDAO;

    public GameService() {
        this.gameDAO = new GameDAO();
    }
    public Games createGame(String authToken, String gameName) throws DataAccessException {
            return new Games(gameName, gameDAO.createGame(authToken, gameName), null, null);
        }

    public void joinGame(String authToken, String playerColor, String gameID, String userName) throws DataAccessException {
        gameDAO.joinGame(authToken, playerColor, gameID, userName);
    }

    public ArrayList<Map<String, String>> listGames() {
        return gameDAO.listGames();
    }
}

