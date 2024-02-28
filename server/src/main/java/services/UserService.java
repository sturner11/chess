package services;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;

public class UserService {
    UserDAO userDAO;
    AuthDAO authDAO;

    public UserService() {
        this.userDAO = new UserDAO();
        this.authDAO = new AuthDAO();
    }

//    public AuthData register(UserData user) {}
//    public AuthData login(UserData user) {}
//    public void logout(UserData user) {}

    public boolean clear() {
        return this.userDAO.clear();
    }
    public String register(String username, String password, String email) throws DataAccessException {
            if (!this.userDAO.userExists(username)) {
                this.userDAO.createUser(username, password, email);
                return this.authDAO.createAuth(username);
            } else {
                throw new DataAccessException("User already exists");
        }

    }

}
