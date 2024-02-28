package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.DataAccessException;
import models.User;
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
        Spark.post("/user", this::register);
        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object register(Request request, Response response) throws DataAccessException {
        try {
            var body = new Gson().fromJson(request.body(), User.class);
            String authToken = userService.register(body.username(), body.password(), body.email());
            response.status(200);
            response.body(authToken);
            JsonObject resp = new JsonObject();
            resp.addProperty("Auth_Token", authToken);
            resp.addProperty("Status", 200);
            return resp;
        } catch(DataAccessException e) {
            JsonObject resp = new JsonObject();
            resp.addProperty("Error_Message", e.getMessage());
            resp.addProperty("Status", 404);
            return resp;
        }
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
