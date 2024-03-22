package models;


import java.util.Objects;
import com.google.gson.internal.LinkedTreeMap;

public class Game {
    final String gameName;
    final Integer gameID;


    @Override
    public String toString() {
        return "Game{" +
                "gameName='" + gameName + '\'' +
                ", gameID=" + gameID +
                ", whiteUsername='" + whiteUsername + '\'' +
                ", blackUsername='" + blackUsername + '\'' +
                '}';
    }

    String whiteUsername;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(gameName, game.gameName) && Objects.equals(gameID, game.gameID) && Objects.equals(whiteUsername, game.whiteUsername) && Objects.equals(blackUsername, game.blackUsername);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameName, gameID, whiteUsername, blackUsername);
    }

    String blackUsername;

    public Game(String gameName, Integer gameID){
        this.gameID = gameID;
        this.gameName = gameName;
        this.whiteUsername = null;
        this.blackUsername = null;
    }





    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }



    public Integer gameID() {
        return gameID;
    }
    public String gameName() {return gameName;}
    public String whiteUsername() {return whiteUsername;}
    public String blackUsername() { return blackUsername;}




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
