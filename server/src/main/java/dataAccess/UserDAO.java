package dataAccess;

import java.util.HashMap;
import java.util.Map;

public class UserDAO implements DAO{
    Map<String, Map<String, String>> localDB;
    public UserDAO() {
    }

    public boolean clear(){
        localDB = null;
        return true;
    }

    public String getUser(String user) throws DataAccessException {
        if (localDB.get(user) != null) {
            return localDB.get(user).toString();
        } else {
            throw new DataAccessException("No Such User in DB");
        }
    }

    public void createUser(String username, String password, String email) {
        Map<String, String> data = new HashMap<>();
        data.put("email", email);
        data.put("password", password);
        localDB.put(username, data);
    }
}
