package dataAccess;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserDAO implements DAO{
    Map<String, Map<String, String>> localDB;
    public UserDAO() {
        localDB = new HashMap<>();
    }

    public boolean clear(){
        localDB = new HashMap<>();
        return true;
    }

    public boolean userExists(String user) throws DataAccessException {
        return this.localDB.get(user) != null;
    }

    public void createUser(String username, String password, String email) {
        Map<String, String> data = new HashMap<>();
        data.put("email", email);
        data.put("password", password);
        localDB.put(username, data);
    }

    public boolean authenticate(String username, String password) {
        boolean check = Objects.equals(this.localDB.get(username).get("password"), password);
        return check;
    }
}
