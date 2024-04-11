package client;

import chess.ChessGame;
import client.websocket.NotificationHandler;
import client.websocket.WebSocketFacade;
import com.google.gson.Gson;
import ui.ChessBoardDisplay;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static java.awt.Color.GREEN;

import static org.glassfish.grizzly.Interceptor.RESET;

public class ChessClient {
    private final String URL;
    private  String auth;
    private  String[] curlArgs;
    private  boolean loggedIn = false;


    private String playerColor;

    private WebSocketFacade ws;
    private final NotificationHandler notificationHandler;
    private boolean chessUI = false;
    private String gameID;

    private String chessGameString;

    public ChessClient(int port, String serverUrl, NotificationHandler notificationHandler) {
        this.notificationHandler = notificationHandler;
        URL = serverUrl + port +"/";

    }

    public  void eval(String line) {
        var userArgs = line.split(" ");
        var command = userArgs[0];
        if (chessUI){
            switch (command) {
                case "help":
                    help();
                    break;
                case "redraw":
                    gamePlayUI(chessGameString, playerColor);
                    break;
                case "leave":
                    leave();
                    break;
                case "move":
                    move(userArgs);
                    break;
                case "resign":
                    resign();
                    break;
                case "highlight":
//                    highlight(userArgs);
                    break;
            }

        } else if (!loggedIn) {
            switch (command) {
                case "help":
                    help();
                    break;
                case "register":
                    user(userArgs);
                    break;
                case "login":
                    login(userArgs);
                    break;
                case "quit":
                    break;
                default:
                    System.out.println("Command not recognized, please authenticate or try again");
            }
        } else {
            switch (userArgs[0]) {
                case "help":
                    help();
                    break;
                case "create":
                    createGame(userArgs);
                    break;
                case "list":
                    listGames();
                    break;
                case "join":
                    joinGame(userArgs);
                    break;
                case "observe":
                    observeGame(userArgs);
                    break;
                case "logout":
                    logout();
                    break;
                case "quit":
                    break;
                default:
                    System.out.println("Command not recognized, please try again");

            }
        }
        System.out.println();
    }

    private void resign() {
        ws.resign(gameID, playerColor, auth);
    }

    private void move(String[] userArgs) {
        System.out.println(playerColor);
        if (userArgs.length == 3) {
                ws.makeMove(userArgs[1], userArgs[2], auth, gameID, playerColor);

        } else {
            System.out.println("Please enter the correct amount of arguments for command: " + userArgs[0]);
        }

        // redraw chess board
    }

    private void leave() {
        ws.leave(auth, playerColor, gameID);
        playerColor = null;
        gameID = null;
        chessUI = false;
    }

    public void logout() {
        try {
            curlArgs = new String[]{"DELETE", auth, URL + "session"};
            ClientCurl.makeReq(curlArgs);
            loggedIn = false;
            auth = null;
        } catch (Exception e) {
            System.out.println("User Logout failed. Please try again");
        }
    }

      public void joinGame(String[] userArgs) {
        Map<String, String> body = createBody(userArgs, new String[] {"gameID", "playerColor"});
        if ( body != null && !body.isEmpty()){
            try {
                playerColor = body.get("playerColor");
                viewGame(body);

            } catch (Exception e) {
                System.out.println("Could not join game. Please try again");
            }
        } else {
            System.out.println("Please enter the correct amount of arguments for command: " + userArgs[0]);
        }
    }

    public  Map<String, String> createBody(String[] userArgs, String[] bodyParams) {
        if (userArgs.length - 1 != bodyParams.length){
            return null;
        }
        Map<String, String> body = new HashMap<>();
        for (int i = 1; i < bodyParams.length + 1; ++i){
             body.put(bodyParams[i-1], userArgs[i]);
        }
        return body;
    }

    public  void observeGame(String[] userArgs){
        Map<String, String> body = createBody(userArgs, new String[] {"gameID"});
        if ( body != null && !body.isEmpty()){
            try {
                viewGame(body);
            } catch ( URISyntaxException | IOException e) {
            System.out.println("Could not observe game. Please try again.");
            }
        } else {
            System.out.println("Please enter the correct amount of arguments for command: " + userArgs[0]);
        }    
    }

    public void viewGame(Map<String, String> body) throws URISyntaxException, IOException {
        curlArgs = new String[]{"PUT", auth, URL + "game", body.toString()};
        Map resp = ClientCurl.makeReq(curlArgs);
        assert resp != null;
        gameID = body.get("gameID");
        ws = new WebSocketFacade(URL, notificationHandler);
        ws.joinPlayer(body.get("gameID"), body.get("playerColor"), auth);
    }

    public void gamePlayUI(String game, String playerColor) {
        ChessGame chessGame = new Gson().fromJson(game, ChessGame.class);
        ChessBoardDisplay.draw(chessGame.getBoard().toString(), this.playerColor != null ? playerColor : "WHITE");

        printPrompt();
        chessUI = true;
    }

    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + GREEN);
    }
    public  void listGames() {
        try {
            curlArgs = new String[]{"GET", auth, URL + "game"};
            Map<String, ArrayList<Map<String, Object>>> resp =  ClientCurl.makeReq(curlArgs);
            assert resp != null;
            ArrayList<Map<String, Object>> games = resp.get("games");
            System.out.println("Games:");
            for (int i = 0; i < games.size(); ++i){
                Map<String, Object> game = games.get(i);
                System.out.print(i+1);
                String white = game.get("whiteUsername") != null ? (String) game.get("whiteUsername") : "AVAILABLE";
                String black = game.get("blackUsername") != null ? (String) game.get("blackUsername") : "AVAILABLE";
                System.out.println(". GameName:" + game.get("gameName")+ " WHITE:" +
                white + " BLACK:" + black);
            }
        } catch (URISyntaxException | IOException e) {
            System.out.println("Could not list Game");
        }
    }

    public  void createGame(String[] userArgs) {
        Map<String, String> body = createBody(userArgs, new String[] {"gameName"});
        if ( body != null && !body.isEmpty()){
            try {
                curlArgs = new String[]{"POST", auth, URL + "game", body.toString()};
                ClientCurl.makeReq(curlArgs);
                System.out.println("Game successfully made. Use join command to start game");
            } catch (Exception e) {
                System.out.println("Could not create game. Please try again");
            }
        } else {
            System.out.println("Please enter the correct amount of arguments for command: " + userArgs[0]);
        }
    }

    public  void login(String[] userArgs) {
        Map<String, String> body = createBody(userArgs, new String[] {"username", "password"});
        if ( body != null && !body.isEmpty()){
            curlArgs = new String[]{"POST", null, URL + "session", body.toString()};
            try {
                Map resp = ClientCurl.makeReq(curlArgs);
                assert resp != null;
                System.out.println("User logged in! Please type help to continue");
                auth = (String) resp.get("authToken");
                loggedIn = true;
            } catch (Exception e) {
                System.out.println("User login failed. Please try again.");
            }
        } else {
            System.out.println("Please enter the correct amount of arguments for command: " + userArgs[0]);
        }
    }

    public void user( String[] userArgs) {
        Map<String, String> body = createBody(userArgs, new String[] {"username", "password", "email"});
        if ( body != null && !body.isEmpty()){
            curlArgs = new String[]{"POST", null, URL + "user", body.toString()};
            try {
                Map resp = ClientCurl.makeReq(curlArgs);
                System.out.println("User Registered! Please type help to continue");
                assert resp != null;
                auth = (String) resp.get("authToken");
                loggedIn = true;
            } catch (Exception e) {
                System.out.println("Unable to register User, please try again");
            }
        } else {
            System.out.println("Please enter the correct amount of arguments for command: " + userArgs[0]);
        }
    }

    public  void help() {
        String help;
        if (chessUI){
            help = """
                    redraw - redraw chess board
                    leave - leave the current game
                    move <PIECE POSITION> <DESIRED SQUARE> <PROMOTION PIECE>[q,r,k,b] (Defaults to null)- moves piece in PIECE POSITION to DESIRED SQUARE
                    resign - forfeit game (must still leave the game)
                    highlight <PIECE POSITION> - Show all legal moves for piece in PIECE POSITION
                    """;
        } else if (!loggedIn){
            help = """
                        register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                        login <USERNAME> <PASSWORD> - to play chess
                        quit -playing chess
                        help - with possible commands
                        """;
        } else {
            help = """
                        create <NAME> - a game
                        list - games
                        join <ID> [WHITE|BLACK|<empty>} - a game
                        observe <ID> - a game
                        logout - when you are done
                        quit -playing chess
                        help - with possible commands
                        """;
        }
        System.out.print(help);
    }

    public void setChessGame( String game) {
        chessGameString = game;
    }

    public String getPlayerColor() {
        return this.playerColor;
    }
}
