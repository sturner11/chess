package client;

import dataAccess.DataAccessException;
import ui.ChessBoardDisplay;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class ServerFacade {
    private final String URL;
    private  String auth;
    private  String[] curlArgs;
    private  boolean loggedIn = false;

    public ServerFacade(int port, String serverUrl) {
        URL = serverUrl + port +"/";

    }

    public  void run(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = null;
        String command = null;

        while (!Objects.equals(command, "quit")) {
            line = scanner.nextLine();
            var userArgs = line.split(" ");
            command = userArgs[0];
            if (!loggedIn) {
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
            } catch (DataAccessException | URISyntaxException | IOException e) {
            System.out.println("Could not observe game. Please try again.");
            }
        } else {
            System.out.println("Please enter the correct amount of arguments for command: " + userArgs[0]);
        }    
    }

    public  void viewGame(Map<String, String> body) throws URISyntaxException, IOException, DataAccessException {
        curlArgs = new String[]{"PUT", auth, URL + "game", body.toString()};
        Map resp = ClientCurl.makeReq(curlArgs);
        assert resp != null;
        ChessBoardDisplay.main((String[]) resp.get("gameBoard"), "WHITE");
        ChessBoardDisplay.main((String[]) resp.get("gameBoard"), "BLACK");
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
                double gameID = (double) game.get("gameID");
                System.out.print(i+1);
                String white = game.get("whiteUsername") != null ? (String) game.get("whiteUsername") : "AVAILABLE";
                String black = game.get("blackUsername") != null ? (String) game.get("blackUsername") : "AVAILABLE";
                System.out.println(". GameName:" + game.get("gameName")+ " WHITE:" +
                white + " BLACK:" + black);
            }
        } catch (DataAccessException | URISyntaxException | IOException e) {
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
                System.out.println("User ogged in! Please type help to continue");
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
        if (!loggedIn){
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

    public String getAuth(){
        return this.auth;
    }

    public void clearAuth() {
        auth = null;
        loggedIn = false;
    }
}
