package models;

public record Games(String gameName, Integer gameID) {
    @Override
    public String gameName() {
        return gameName;
    }

    @Override
    public Integer gameID() {
        return gameID;
    }

}