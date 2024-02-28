package dataAccess;
import java.util.HashMap;
import java.util.Map;

public class GameDAO {
    Map<String, Integer> localDB;
    public GameDAO() {
        localDB = new HashMap<>();
    }
    Integer currentID = 1;

    public Integer createGame(String authToken, String gameName) throws DataAccessException {
        if (localDB.get(gameName) == null) {
            localDB.put(gameName, currentID);
            Integer gameID = currentID;
            currentID ++;
            return gameID;
        } else{
            throw new DataAccessException("error", 401);
        }
    }

    public void joinGame(String authToken, String gameName, String gameID, String userName) {
    }
}
