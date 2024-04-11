package webSocketMessages.userCommands;

import chess.ChessMove;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    private String playerColor;
    private String username;
    private ChessMove move;
    private String gameID;

    public UserGameCommand(String authToken, String username, String playerColor) {
        this.authToken = authToken;
    }

    public UserGameCommand(String authToken, String username, ChessMove move) {
        this.authToken = authToken;
        this.username = username;
        this.move = move;
    }

    public UserGameCommand(String authToken, String username, String playerColor, String gameID) {
        this.authToken = authToken;
        this.username = username;
        this.playerColor = playerColor;
        this.gameID = gameID;
    }

    public String getUsername() {
        return username;
    }

    public ChessMove getMove() {
        return this.move;
    }

    public String getGameID() { return this.gameID; }

    public enum CommandType {
        JOIN_PLAYER,
        JOIN_OBSERVER,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    protected CommandType commandType;

    private final String authToken;

    public String getAuthString() {
        return authToken;
    }

    public CommandType getCommandType() {
        return this.commandType;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    public String getPlayerColor() { return playerColor; }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserGameCommand))
            return false;
        UserGameCommand that = (UserGameCommand) o;
        return getCommandType() == that.getCommandType() && Objects.equals(getAuthString(), that.getAuthString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthString());
    }
}
