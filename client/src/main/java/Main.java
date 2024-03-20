import chess.*;

import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: Please Enter a command or type help to get started");
        Scanner scanner = new Scanner(System.in);
        String line = null;
        String command = null;
        Boolean loggedIn = false;
        while (!Objects.equals(command, "quit")) {
            line = scanner.nextLine();
            var userArgs = line.split(" ");
            command = userArgs[0];
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
                        // register endpoint
                        break;
                    case "login":
                        // call login endpoint
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
                        // call create game endpoint
                        break;
                    case "list":
                        //list game endpoint
                        break;
                    case "join":
                        // join endpoint
                        break;
                    case "observe":
                        // observe a game
                        break;
                    case "logout":
                        //logout ep
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