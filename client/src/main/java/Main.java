import client.ServerFacade;


public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("♕ 240 Chess Client: Please Enter a command or type help to get started");
        ServerFacade facade = new ServerFacade(8080, "http://localhost:");
        facade.run(args);
    }
}