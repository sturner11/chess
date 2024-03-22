package services;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;

import java.sql.SQLException;

public class UserService {
    final UserDAO userDAO;
    final AuthDAO authDAO;

    public UserService() throws DataAccessException {
        this.userDAO = new UserDAO();
        this.authDAO = new AuthDAO();
    }

    public void clear() {
         this.userDAO.clear();
         this.authDAO.clear();
    }
    public String register(String username, String password, String email) throws DataAccessException, SQLException {
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

    public String login(String username, String password) throws DataAccessException, SQLException {
        if (this.userDAO.userExists(username) && this.userDAO.authenticate(username, password))  {
            return this.authDAO.createAuth(username);
        } else {
            throw new DataAccessException("error", 401);
        }
    }

    public void logout(String authToken) throws DataAccessException {
        if (!authDAO.logOutUser(authToken)){
            throw new DataAccessException("error", 401);
        }

    }

    public String checkAuth(String authToken) throws DataAccessException, SQLException {
        var userName = authDAO.checkAuth(authToken);
        if (userName != null) {
            return userName;
        }else {
            throw new DataAccessException("error", 401);
        }
    }
}
