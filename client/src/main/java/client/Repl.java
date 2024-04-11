package client;

import client.websocket.NotificationHandler;
import client.websocket.WebSocketFacade;
import com.google.gson.Gson;
import ui.ChessBoardDisplay;
import webSocketMessages.Notification;
import webSocketMessages.userCommands.UserGameCommand;

import java.util.Objects;
import java.util.Scanner;

import static java.awt.Color.GREEN;
import static java.awt.Color.RED;
import static org.glassfish.grizzly.Interceptor.RESET;

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
    public void notify(Notification notification) {
        try {
            System.out.println(RED + notification.message());
            client.gamePlayUI(notification.gameID(), notification.playerColor());
        } catch(Error e){
            System.out.println(e);
        }
    }


}
