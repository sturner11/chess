package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.DataAccessException;
import models.*;
import server.websocket.WebSocketHandler;
import services.GameService;
import services.UserService;
import spark.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class Server {
    UserService userService;
    GameService gameService;
    private final WebSocketHandler webSocketHandler;


    public Server()  {
        try {
            this.userService = new UserService();
            this.gameService = new GameService();
        } catch(DataAccessException e){
            return; // TODO: FIX Error
        }
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
        Spark.webSocket("/connect", webSocketHandler); // THis will take gamecommand and have the switch cases TODO: Return here and build out Handler



        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object listGame(Request request, Response response) {
        try {
            // TODO: Make Game IDS integers
            var userName = userService.checkAuth(request.headers("Authorization"));
            ArrayList<Game> games = gameService.listGames();
            response.status(200);
            return new Gson().toJson(Map.of("games",games));
        } catch(DataAccessException e){
            response.status(e.getStatus());
            return new Gson().toJson(new ErrorMessage(e.getMessage()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private Object clear(Request request, Response response) {
        userService.clear();
        gameService.clear();
        return new JsonObject();

    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public void clear(){
        userService.clear();
        gameService.clear();
    }
}
