package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.DataAccessException;
import models.*;
import services.GameService;
import services.UserService;
import spark.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class Server {
    UserService userService;
    GameService gameService;
//    AuthService authService;

    public Server(){
        this.userService = new UserService();
        this.gameService = new GameService();
//        this.authService = new AuthService();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.get("/game", this::listGame);
        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object listGame(Request request, Response response) {
        try {
            var userName = userService.checkAuth(request.headers("Authorization"));
            ArrayList<Game> games = gameService.listGames();
            response.status(200);
            return new Gson().toJson(Map.of("games",games));
        } catch(DataAccessException e){
            response.status(e.getStatus());
            return new Gson().toJson(new ErrorMessage(e.getMessage()));
        }
    }

    private Object joinGame(Request request, Response response) {
        try {
            var body = new Gson().fromJson(request.body(), GameBody.class);
            var userName = userService.checkAuth(request.headers("Authorization"));
            Game game = gameService.joinGame(request.headers("Authorization"), body.playerColor(), body.gameID(), userName);
            response.status(200);
            return new Gson().toJson(Map.of("gameID", body.gameID()));
        } catch(DataAccessException e){
            response.status(e.getStatus());
            return new Gson().toJson(new ErrorMessage(e.getMessage()));
        }
    }

    private Object createGame(Request request, Response response) {
        try {
            var body = new Gson().fromJson(request.body(), GameBody.class);
            userService.checkAuth(request.headers("Authorization"));
            Game game = gameService.createGame(request.headers("Authorization"), body.gameName());
            response.status(200);
            return new Gson().toJson(game);
        } catch(DataAccessException e){

            response.status(e.getStatus());
            return new Gson().toJson(new ErrorMessage(e.getMessage()));
        }
    }

    private Object logout(Request request, Response response) {
        try {
            userService.logout(request.headers("Authorization"));
            response.status(200);
            return new Gson().toJson(Map.of("authToken", request.headers("Authorization")));
        } catch(DataAccessException e){
            response.status(e.getStatus());
            return new Gson().toJson(new ErrorMessage(e.getMessage()));
        }
    }

    private Object login(Request request, Response response)  {
        try {
            var body = new Gson().fromJson(request.body(),  User.class);
            String authToken = userService.login(body.username(), body.password());
            response.type("application/json");
            response.status(200);
            return new Gson().toJson(new Auth(body.username(), authToken));
        } catch(DataAccessException e) {
            response.status(e.getStatus());
            return new Gson().toJson(new ErrorMessage(e.getMessage()));
        }
    }

    private Object register(Request request, Response response) {
        try {
            var body = new Gson().fromJson(request.body(), User.class);
            String authToken = userService.register(body.username(), body.password(), body.email());
            response.type("application/json");
            return new Gson().toJson(new Auth(body.username(), authToken));
        } catch(DataAccessException e) {
            int check = e.getStatus();
            response.status(e.getStatus());
            return new Gson().toJson(new ErrorMessage(e.getMessage()));
        }
    }


    private Object clear(Request request, Response response) {
        boolean userCleared = userService.clear();
        response.status();
        JsonObject resp = new JsonObject();
        resp.addProperty("Status", 200);
//        boolean gamesCleared = gameService.clear();
//        boolean authCleared = authService.clear();
        return resp;
//                && gamesCleared && authCleared;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
