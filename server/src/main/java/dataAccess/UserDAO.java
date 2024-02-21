package dataAccess;

import java.util.Map;

public class UserDAO implements DAO{
    Map<String, Map<String, String>> localDB;
    public UserDAO() {
    }

    public boolean clear(){
        localDB = null;
        return true;
    }
}
