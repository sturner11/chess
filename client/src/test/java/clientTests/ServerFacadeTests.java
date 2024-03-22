package clientTests;
import client.ServerFacade;
import org.junit.jupiter.api.*;
import server.Server;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {
    static ServerFacade facade;
    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port, "http://localhost:");
    }
    @BeforeEach
    public void clear(){
        server.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    @DisplayName("RegisterSuccess")
    void registerSuccess() throws Exception {
        facade.user( new String[]{"register", "player1", "password", "p1@email.com"});
        assertNotNull(facade.getAuth());
    }

    @Test
    @DisplayName("RegisterFailure")
    void registerFail() throws Exception {
        facade.user( new String[]{"player1", "password", "p1@email.com"});
        assertNull(facade.getAuth());
    }

    @Test
    @DisplayName("LoginSuccess")
    void loginSuccess() throws Exception {
        facade.user( new String[]{"register", "player1", "password", "p1@email.com"});
        facade.user( new String[]{"login", "player1", "password"});
        assertNotNull(facade.getAuth());
    }

    @Test
    @DisplayName("LoginFailure")
    void loginFail() throws Exception {
        facade.user( new String[]{"login","player1", "password"});
        assertNull(facade.getAuth());
    }

    @Test
    @DisplayName("HelpSuccess")
    void helpSuccess() throws Exception {
        PrintStream output = System.out;
        ByteArrayOutputStream capturedOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream((capturedOut)));
        facade.help();
        System.setOut(output);
        String actualOutput = capturedOut.toString();
        assert(actualOutput.contains("register <USERNAME> <PASSWORD> <EMAIL> - to create an account\n" +
                "login <USERNAME> <PASSWORD> - to play chess\n" +
                "quit -playing chess\n" +
                "help - with possible commands"));
    }

    @Test
    @DisplayName("HelpPostLogin")
    void helpPostLogin() throws Exception {
        facade.user( new String[]{"register", "player1", "password", "p1@email.com"});
        facade.user( new String[]{"login","player1", "password"});
        PrintStream output = System.out;
        ByteArrayOutputStream capturedOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream((capturedOut)));
        facade.help();
        System.setOut(output);
        String actualOutput = capturedOut.toString();
        assert(actualOutput.contains("""
                        create <NAME> - a game
                        list - games
                        join <ID> [WHITE|BLACK|<empty>} - a game
                        observe <ID> - a game
                        logout - when you are done
                        quit -playing chess
                        help - with possible commands
                        """));
    }
}


