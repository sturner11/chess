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
        Spark.delete("/db", this::clear);
        Spark.put("/user", this::register);
        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object register(Request request, Response response) {
        userService.register(response.username, response.password, response.email);
        response.status();
    }


    private Object clear(Request request, Response response) {
        boolean userCleared = userService.clear();
        response.status();
//        boolean gamesCleared = gameService.clear();
//        boolean authCleared = authService.clear();
        return userCleared;
//                && gamesCleared && authCleared;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
