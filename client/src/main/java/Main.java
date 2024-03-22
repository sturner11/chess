import chess.*;
import dataAccess.DataAccessException;

import java.util.*;


public class Main {
    public static void main(String[] args) throws Exception {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        String url = "http://localhost:8080/";
        System.out.println("♕ 240 Chess Client: Please Enter a command or type help to get started");
        Scanner scanner = new Scanner(System.in);
        String line = null;
        String command = null;
        boolean loggedIn = false;
        String auth = null;
        while (!Objects.equals(command, "quit")) {
            line = scanner.nextLine();
            var userArgs = line.split(" ");
            Map<String, String> body = new HashMap<>();
            String[] curlArgs = new String[]{};

            if (!loggedIn) {
                switch (userArgs[0]) {
                    case "help":
                        var help = """
                                register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                                login <USERNAME> <PASSWORD> - to play chess
                                quit -playing chess
                                help - with possible commands
                                """;
                        System.out.print(help);
                        break;
                    case "register":
                        body.put("username", userArgs[1]);
                        body.put("password", userArgs[2]);
                        body.put("email", userArgs[3]);
                        curlArgs = new String[]{"POST", null, url + "user", body.toString()};
                        try {
                            Map resp = ClientCurl.makeReq(curlArgs);
                            assert resp != null;
                            auth = (String) resp.get("authToken");
                        } catch (DataAccessException ignored) {
                            // CHeck for errors?
                        }
                        break;
                    case "login":
                        body.put("username", userArgs[1]);
                        body.put("password", userArgs[2]);
                        curlArgs = new String[]{"POST", null, url + "session", body.toString()};
                        try {
                            Map resp = ClientCurl.makeReq(curlArgs);
                            assert resp != null;
                            auth = (String) resp.get("authToken");
                            loggedIn = true;
                        } catch (DataAccessException ignored) {
                            // CHeck for errors?
                        }
                        loggedIn = true;
                        break;
                    case "quit":
                        break;
                    default:
                        System.out.println("Command not recognized, please try again");
                }
            } else {
                switch (userArgs[0]) {
                    case "help":
                        var help = """
                                create <NAME> - a game
                                list - games
                                join <ID> [WHITE|BLACK|<empty>} - a game
                                observe <ID> - a game
                                logout - when you are done
                                quit -playing chess
                                help - with possible commands
                                """;
                        System.out.print(help);
                        break;
                    case "create":
                        body.put("gameName", userArgs[1]);
                        curlArgs = new String[]{"POST", auth, url + "game", body.toString()};
                        ClientCurl.makeReq(curlArgs);
                        break;
                    case "list":
                        curlArgs = new String[]{"GET", url + "game"};
                        ClientCurl.makeReq(curlArgs);
                        break;
                    case "join":
                        body.put("gameID", userArgs[1]);
                        body.put("playerColor", userArgs[2]);
                        curlArgs = new String[]{"PUT", url + "user", body.toString()};
                        ClientCurl.makeReq(curlArgs);
                        break;
                    case "observe":
                        body.put("gameID", userArgs[1]);
                        curlArgs = new String[]{"PUT", url + "user", body.toString()};
                        ClientCurl.makeReq(curlArgs);
                        break;
                    case "logout":
                        curlArgs = new String[]{"POST", url + "user"};
                        ClientCurl.makeReq(curlArgs);
                        break;
                    case "quit":
                        break;
                    default:
                        System.out.println("Command not recognized, please try again");

                }
            }

        }
    }
}