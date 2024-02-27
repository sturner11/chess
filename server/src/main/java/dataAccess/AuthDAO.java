package dataAccess;

import java.util.Map;
import java.util.UUID;

public class AuthDAO implements DAO{
    Map<String, String> localDB;
    public AuthDAO() {
    }

    public boolean clear(){
        localDB = null;
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

