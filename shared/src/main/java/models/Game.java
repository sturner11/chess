package models;

public record Game(String playerColor, String gameID) {
    @Override
    public String playerColor() {
        return playerColor;
    }
    @Override
    public String gameID() {
        return gameID;
    }

}
