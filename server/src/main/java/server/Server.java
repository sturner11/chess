package server;

import services.UserService;
import spark.*;

public class Server {
    UserService userService;
//    GameService gameService;
//    AuthService authService;

    public Server(){
        this.userService = new UserService();
//        this.gameService = new GameService();
//        this.authService = new AuthService();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/clear", this::clear);
        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object clear(Request request, Response response) {
        boolean userCleared = userService.clear();
//        boolean gamesCleared = gameService.clear();
//        boolean authCleared = authService.clear();
        return userCleared ;
//                && gamesCleared && authCleared;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
