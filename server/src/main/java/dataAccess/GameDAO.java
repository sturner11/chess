package dataAccess;
import models.Game;
import models.Games;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameDAO {
    Map<String, Games> localDB;
    public GameDAO() {
        localDB = new HashMap<>();
    }
    Integer currentID = 1;

    public Integer createGame(String authToken, String gameName) throws DataAccessException {
        if (localDB.get(gameName) == null) { // TODO add user?
            Map<String, String> data = new HashMap<>();
            data.put("gameID", currentID.toString());
            data.put("whiteUsername", null);
            data.put("blackUsername", null);
            data.put("gameName", gameName);
            localDB.put(currentID.toString(), data);
            Integer gameID = currentID;
            currentID ++;
            return gameID;
        } else{
            throw new DataAccessException("error", 401);
        }
    }

    public void joinGame(String authToken, String playerColor, String gameID, String userName) throws DataAccessException {
        if (localDB.get(gameID) != null && localDB.get(gameID).get(playerColor) == null){
            localDB.get(gameID).put(playerColor, userName);
        } else {
            throw new DataAccessException("error", 400);
        }
    }

    public ArrayList<Map<String, String>> listGames() {
        ArrayList<Map<String, String>> games = new ArrayList<>();
        for (Map.Entry<String, Map<String,String>> entry : localDB.entrySet()){
            Games game =
                    new Games(entry.getValue().get("gameName"), Integer.parseInt(entry.getValue().get("gameID")),
                            entry.getValue().get("whiteUserName"), entry.getValue().get("blackUserName"));
        }
        return games;
    }
}
