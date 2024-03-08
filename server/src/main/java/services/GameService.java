package services;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import models.Game;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class GameService {

    GameDAO gameDAO;

    public GameService() throws DataAccessException {
        this.gameDAO = new GameDAO();
    }
    public Game createGame(String authToken, String gameName) throws DataAccessException {
        if (gameName != null) {
            return gameDAO.createGame(authToken, gameName);
        } else {
                throw new DataAccessException("error", 403);
            }
        }

    public Game joinGame(String authToken, String playerColor, Integer gameID, String userName) throws DataAccessException, SQLException {
        return gameDAO.joinGame(authToken, playerColor, gameID, userName);
    }

    public ArrayList<Game> listGames() throws SQLException {
        return gameDAO.listGames();
    }

    public void clear() {
        gameDAO.clear();
    }
}

