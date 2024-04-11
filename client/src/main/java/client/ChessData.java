package client;

import chess.ChessBoard;

public record ChessData(String playerColor, String message, String username) {

    @Override
    public String playerColor() {
        return playerColor;
    }

//    @Override
//    public String chessBoard() {
//        return chessBoard;
//    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public String username() {
        return username;
    }
}
