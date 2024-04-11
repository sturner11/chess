package client;

import client.websocket.NotificationHandler;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.Objects;
import java.util.Scanner;

import static java.awt.Color.RED;

public class Repl implements NotificationHandler {

    private final ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(8080, serverUrl, this);
    }

    public void run() {
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

    @Override
    public void notify(ServerMessage message) {
        try {
            switch (message.getServerMessageType()){
                case ServerMessage.ServerMessageType.NOTIFICATION:
                    System.out.println(message.getMessage());
                    break;
                case ServerMessage.ServerMessageType.LOAD_GAME:
                    client.gamePlayUI(message.getGame(), message.getPlayerColor());
                    client.setChessGame( message.getGame());
                    break;
                case ServerMessage.ServerMessageType.ERROR:
                    System.out.println( ((ErrorMessage) message).getErrorMessage());
            }

        } catch(Error e){
            System.out.println(e);
        }
        System.out.println(">>>");
    }


}
