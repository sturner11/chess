package dataAccess;

import models.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameDAO {
    Map<Integer, Game> localDB;
    public GameDAO() {
        localDB = new HashMap<>();
    }
    Integer currentID = 1;

    public Game createGame(String authToken, String gameName) throws DataAccessException {
            Game game = new Game(gameName, currentID);
            localDB.put(currentID, game);
            Integer gameID = currentID;
            currentID ++;
            return game;
    }

    public Game joinGame(String authToken, String playerColor, Integer gameID, String userName) throws DataAccessException {
        if (localDB.get(gameID) != null && localDB.get(gameID).isAvailable(playerColor)){
            localDB.get(gameID).setColor(playerColor, userName);
            return localDB.get(gameID);
        } else {
            throw new DataAccessException("error", 403);
        }
    }

    public ArrayList<Game> listGames() {
        ArrayList<Game> games = new ArrayList<>();
        for (Map.Entry<Integer, Game> entry : localDB.entrySet()){
            games.add(entry.getValue());
        }
        return games;
    }
}
