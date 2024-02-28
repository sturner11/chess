package models;

public record ErrorMessage(String message) {
    @Override
    public String message() {
        return message;
    }


}