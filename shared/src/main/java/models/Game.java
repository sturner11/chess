package models;

public record Game(String gameName, String gameID) {
    @Override
    public String gameName() {
        return gameName;
    }
    @Override
    public String gameID() {
        return gameID;
    }

}
