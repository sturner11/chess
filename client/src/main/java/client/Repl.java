package client;

import client.websocket.WebSocketFacade;

import java.util.Objects;
import java.util.Scanner;

public class Repl {

    private final ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(8080, serverUrl);
    }

    public void run() {
        System.out.println("♕ 240 Chess Client: Please Enter a command or type help to get started");
        Scanner scanner = new Scanner(System.in);
        String line;
        String command = null;
        var output = "";
        while (!Objects.equals(command, "quit")) {
            line = scanner.nextLine();

            try {
                client.eval(line);
                System.out.print(output);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

}
