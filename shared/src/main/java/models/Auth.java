package models;

public record Auth(String username, String authToken) {
    @Override
    public String username() {
        return username;
    }

    @Override
    public String authToken() {
        return authToken;
    }

}