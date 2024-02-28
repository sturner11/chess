package services;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import models.Games;

public class GameService {

    GameDAO gameDAO;

    public GameService() {
        this.gameDAO = new GameDAO();
    }
    public Games createGame(String authToken, String gameName) throws DataAccessException {
            return new Games(gameName, gameDAO.createGame(authToken, gameName));
        }

    public Object   joinGame(String authToken, String gameName, String gameID, String userName) {
        gameDAO.joinGame(authToken, gameName, gameID, userName);
        return null;
    }
}

