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

    public void clear() {
         this.userDAO.clear();
         this.authDAO.clear();
    }
    public String register(String username, String password, String email) throws DataAccessException {
        if (username != null && password != null) {
            if (!this.userDAO.userExists(username)) {
                this.userDAO.createUser(username, password, email);
                return this.authDAO.createAuth(username);
            } else {
                throw new DataAccessException("error", 403);
            }
        } else {
            throw new DataAccessException("error", 400);
        }
    }

    public String login(String username, String password) throws DataAccessException {
        if (this.userDAO.userExists(username) && this.userDAO.authenticate(username, password))  {
            return this.authDAO.createAuth(username);
        } else {
            throw new DataAccessException("error", 401);
        }
    }

    public void logout(String authToken) throws DataAccessException {
        if (!authDAO.logOutUser(authToken)){
            throw new DataAccessException("error", 401);
        };
    }

    public String checkAuth(String authToken) throws DataAccessException {
        var userName = authDAO.checkAuth(authToken);
        if (userName != null) {
            return userName;
        }else {
            throw new DataAccessException("error", 401);
        }
    }
}
