package services;

import dataAccess.UserDAO;

public class UserService {
    UserDAO dao;

    public UserService() {
        this.dao = new UserDAO();
    }

//    public AuthData register(UserData user) {}
//    public AuthData login(UserData user) {}
//    public void logout(UserData user) {}

    public boolean clear() {
        return this.dao.clear();
    }
}
