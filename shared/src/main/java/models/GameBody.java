package models;

public record GameBody(String playerColor, Integer gameID, String gameName) {
    public String playerColor() {
        return playerColor;
    }
    @Override
    public Integer gameID() {
        return gameID;
    }
    public String gameName() {
        return gameName;
    }
}
