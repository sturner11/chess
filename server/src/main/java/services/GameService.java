package services;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import models.Game;

import java.util.ArrayList;
import java.util.Map;

public class GameService {

    GameDAO gameDAO;

    public GameService() {
        this.gameDAO = new GameDAO();
    }
    public Game createGame(String authToken, String gameName) throws DataAccessException {
            return gameDAO.createGame(authToken, gameName);
        }

    public Game joinGame(String authToken, String playerColor, Integer gameID, String userName) throws DataAccessException {
        return gameDAO.joinGame(authToken, playerColor, gameID, userName);
    }

    public ArrayList<Game> listGames() {
        return gameDAO.listGames();
    }
}

