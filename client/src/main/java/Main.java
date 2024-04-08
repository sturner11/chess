import client.ChessClient;
import client.Repl;


public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("♕ 240 Chess Client: Please Enter a command or type help to get started");
        Repl facade = new Repl("http://localhost:");
        facade.run();
    }
}