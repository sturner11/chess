package models;

public record Games(String gameName, Integer gameID, String whiteUsername, String blackUsername) {
    @Override
    public String gameName() {
        return gameName;
    }

    @Override
    public Integer gameID() {
        return gameID;
    }

}