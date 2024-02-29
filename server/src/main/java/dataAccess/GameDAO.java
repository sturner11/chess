package dataAccess;

import models.Game;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GameDAO {
    Map<Integer, Game> localDB;
    public GameDAO() {
        localDB = new HashMap<>();
    }
    Integer currentID = 1;

    public Game createGame(String authToken, String gameName) {
            Game game = new Game(gameName, currentID);
            localDB.put(currentID, game);
            Integer gameID = currentID;
            currentID ++;
            return game;
    }

    public Game joinGame(String authToken, String playerColor, Integer gameID, String userName) throws DataAccessException {
        if (localDB.get(gameID) != null ){
            if (localDB.get(gameID).isAvailable(playerColor)) {
                localDB.get(gameID).setColor(playerColor, userName);
                return localDB.get(gameID);
            } else {
                throw new DataAccessException("error", 403);
            }
        } else {
            throw new DataAccessException("error", 400);
        }
    }

    public ArrayList<Game> listGames() {
        ArrayList<Game> games = new ArrayList<>();
        for (Map.Entry<Integer, Game> entry : localDB.entrySet()){
            games.add(entry.getValue());
        }
        return games;
    }

    public void clear() {
        localDB = new HashMap<>();
    }
}
