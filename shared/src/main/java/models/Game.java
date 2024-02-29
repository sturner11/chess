package models;

import chess.exceptions.DataAccessException;

import java.util.Objects;

public class Game {
    String gameName;
    Integer gameID;
    String whiteUsername;
    String blackUsername;

    public Game(String gameName, Integer gameID){
        this.gameID = gameID;
        this.gameName = gameName;
        this.whiteUsername = null;
        this.blackUsername = null;
    }

    public String gameName() {
        return gameName;
    }

    public String whiteUsername() {
        return whiteUsername;
    }
    public String blackUsername() {
        return blackUsername;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public Integer gameID() {
        return gameID;
    }




    public boolean isAvailable(String playerColor) {
        if (Objects.equals(playerColor, "White")){
            return whiteUsername == null;
        } else {
            return blackUsername == null;
        }
    }

    public void setColor(String playerColor, String userName) {
        if (Objects.equals(playerColor, "WHITE")){
            whiteUsername = userName;
        } else if (Objects.equals(playerColor, "BLACK")){
            blackUsername = userName;
        }
    }
}
