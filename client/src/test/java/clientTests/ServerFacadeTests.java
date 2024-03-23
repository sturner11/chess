package clientTests;
import client.ServerFacade;
import clientResources.DataChecks;
import org.junit.jupiter.api.*;
import server.Server;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {
    static ServerFacade facade;
    private static Server server;
    private static int portString = 0;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port, "http://localhost:");
        portString = port;
    }

    @BeforeEach
    public void clear() {
        server.clear();
        facade.clearAuth();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    @DisplayName("RegisterSuccess")
    void registerSuccess() throws Exception {
        facade.user(new String[]{"register", "player1", "password", "p1@email.com"});
        assertNotNull(facade.getAuth());
    }

    @Test
    @DisplayName("RegisterFailure")
    void registerFail() throws Exception {
        facade.user(new String[]{"player1", "password", "p1@email.com"});
        assertNull(facade.getAuth());
    }

    @Test
    @DisplayName("LoginSuccess")
    void loginSuccess() throws Exception {
        facade.user(new String[]{"register", "player1", "password", "p1@email.com"});
        facade.login(new String[]{"login", "player1", "password"});
        assertNotNull(facade.getAuth());
    }

    @Test
    @DisplayName("LoginFailure")
    void loginFail() throws Exception {
        facade.user(new String[]{"login", "player1", "password"});
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
        assertEquals(actualOutput,DataChecks.getNoAuthHelp());
    }

    @Test
    @DisplayName("HelpPostLogin")
    void helpPostLogin() throws Exception {
        facade.user(new String[]{"register", "player1", "password", "p1@email.com"});
        facade.login(new String[]{"login", "player1", "password"});
        PrintStream output = System.out;
        ByteArrayOutputStream capturedOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream((capturedOut)));
        facade.help();
        System.setOut(output);
        String actualOutput = capturedOut.toString();
        assertEquals(actualOutput, DataChecks.getAuthHelp());
    }

    @Test
    @DisplayName("CreateGameSuccess")
    void createGameSuccess() throws Exception {
        facade.user(new String[]{"register", "player1", "password", "p1@email.com"});
        facade.user(new String[]{"login", "player1", "password"});
        PrintStream output = System.out;
        ByteArrayOutputStream capturedOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream((capturedOut)));
        facade.createGame(new String[]{"create", "name"});
        System.setOut(output);
        String actualOutput = capturedOut.toString();
        assertEquals(DataChecks.getCreateGameResp().trim().replace("\r", ""),actualOutput.trim().replace("\r", ""));
    }

    @Test
    @DisplayName("createGameFailure")
    void createGameFail() throws Exception {
        facade.user(new String[]{"register", "player1", "password", "p1@email.com"});
        facade.login(new String[]{"login", "player1", "password"});
        PrintStream output = System.out;
        ByteArrayOutputStream capturedOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream((capturedOut)));
        facade.createGame(new String[]{"create"});
        System.setOut(output);
        String actualOutput = capturedOut.toString();
        assertEquals("Please enter the correct amount of arguments for command: create\n".trim().replace("\r", ""),actualOutput.trim().replace("\r", ""));
    }

    @Test
    @DisplayName("JoinGameSuccess")
    void joinGameSuccess() throws Exception {
        facade.user(new String[]{"register", "player1", "password", "p1@email.com"});
        facade.login(new String[]{"login", "player1", "password"});
        facade.createGame(new String[]{"create", "name"});
        PrintStream output = System.out;
        ByteArrayOutputStream capturedOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream((capturedOut)));
        facade.joinGame(new String[]{"join", "1", "WHITE"});
        System.setOut(output);
        String actualOutput = capturedOut.toString();
        assertEquals(DataChecks.getJoinGameResp().trim().replace("\r", ""),actualOutput.trim().replace("\r", ""));
    }

    @Test
    @DisplayName("joinGameFailure")
    void joinGameFail() throws Exception {
        facade.user(new String[]{"register", "player1", "password", "p1@email.com"});
        facade.login(new String[]{"login", "player1", "password"});
        facade.createGame(new String[]{"create"});
        facade.joinGame(new String[]{"join", "1", "WHITE"});
        PrintStream output = System.out;
        ByteArrayOutputStream capturedOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream((capturedOut)));
        facade.joinGame(new String[]{"join", "1", "WHITE"});
        System.setOut(output);
        String actualOutput = capturedOut.toString();
        assertEquals(actualOutput.trim().replace("\r", ""), DataChecks.getJoinGameFail().trim().replace("\r", ""));
    }

    @Test
    @DisplayName("observeGameSuccess")
    void observeGameSuccess() throws Exception {
        facade.user(new String[]{"register", "player1", "password", "p1@email.com"});
        facade.login(new String[]{"login", "player1", "password"});
        facade.createGame(new String[]{"create", "name"});
        PrintStream output = System.out;
        ByteArrayOutputStream capturedOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream((capturedOut)));
        facade.observeGame(new String[]{"observe", "1"});
        System.setOut(output);
        String actualOutput = capturedOut.toString();
        assertNotNull(actualOutput);
    }

    @Test
    @DisplayName("observeGameFailure")
    void observeGameFail() throws Exception {
        facade.user(new String[]{"register", "player1", "password", "p1@email.com"});
        facade.login(new String[]{"login", "player1", "password"});
        PrintStream output = System.out;
        ByteArrayOutputStream capturedOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream((capturedOut)));
        facade.observeGame(new String[]{"observe", "2"});
        System.setOut(output);
        String actualOutput = capturedOut.toString();
        assertEquals(actualOutput.trim().replace("\r", ""), DataChecks.getObserveGame(portString).trim().replace("\r", ""));
    }

    @Test
    @DisplayName("LogoutSuccess")
    void logoutSuccess() throws Exception {
        facade.user(new String[]{"register", "player1", "password", "p1@email.com"});
        facade.login(new String[]{"login", "player1", "password"});
        facade.logout();
        assertNull(facade.getAuth());
    }

    @Test
    @DisplayName("LogoutFailure")
    void logoutFailure() throws Exception {
        PrintStream output = System.out;
        ByteArrayOutputStream capturedOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream((capturedOut)));
        facade.logout();
        System.setOut(output);
        String actualOutput = capturedOut.toString();
        assertEquals(actualOutput.trim().replace("\r", ""), DataChecks.getLogoutFail().trim().replace("\r", ""));
    }
}









