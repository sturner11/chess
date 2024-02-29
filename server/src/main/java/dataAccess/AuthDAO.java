package dataAccess;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthDAO implements DAO{
    Map<String, String> localDB;
    public AuthDAO() {
        localDB = new HashMap<>();
    }

    public void clear(){
        localDB = new HashMap<>();
    }

    public String createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        localDB.put(authToken, username);
        return authToken;
    }


    public boolean logOutUser(String authToken) {
        return localDB.remove(authToken) != null;
    }

    public String checkAuth(String authToken) {
        return localDB.get(authToken);
    }
}

