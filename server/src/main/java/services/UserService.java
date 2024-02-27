package services;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;

public class UserService {
    UserDAO userDAO;
    AuthDAO authDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

//    public AuthData register(UserData user) {}
//    public AuthData login(UserData user) {}
//    public void logout(UserData user) {}

    public boolean clear() {
        return this.userDAO.clear();
    }
    public String register(String username, String password, String email) {
        try {
            this.userDAO.getUser();
            this.userDAO.createUser(username, password, email);
            return this.authDAO.createAuth(username);
        } catch (DataAccessException e){
            return e.getMessage();
        }

    }

}
