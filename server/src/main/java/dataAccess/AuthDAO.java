package dataAccess;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthDAO implements DAO{
    Map<String, String> localDB;
    public AuthDAO() {
        localDB = new HashMap<>();
    }

    public boolean clear(){
        localDB = new HashMap<>();
        return true;
    }

    public void getUser() {
    }

    public String createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        localDB.put(username, authToken);
        return authToken;
    }
}

