package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.DataAccessException;
import models.*;
import server.websocket.WebSocketHandler;
import services.GameService;
import services.UserService;
import spark.*;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class Server {
    UserService userService;
    GameService gameService;
    private WebSocketHandler webSocketHandler;


    public Server()  {
        webSocketHandler = null;
        try {
            webSocketHandler = new WebSocketHandler();
        } catch (DataAccessException e){
            System.out.println(e);
        }
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

        Spark.webSocket("/connect", webSocketHandler); // THis will take gamecommand and have the switch cases


        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.get("/game", this::listGame);
        Spark.put("/board", this::getBoard);
        Spark.put("/move", this::move);



        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object move(Request request, Response response) {
        try {
            var body = new Gson().fromJson(request.body(), GameBody.class);
            userService.checkAuth(request.headers("Authorization"));
            String gameBoard = gameService.getBoard(body.gameID());
            response.status(200);
            return new Gson().toJson(Map.of("gameBoard", gameBoard, "playerColor", body.playerColor() != null ? body.playerColor(): "WHITE", "gameID", body.gameID()));
        } catch(DataAccessException e){
            response.status(e.getStatus());
            return new Gson().toJson(new ErrorMessage(e.getMessage()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Object getBoard(Request request, Response response) {
        try {
            var body = new Gson().fromJson(request.body(), GameBody.class);
            userService.checkAuth(request.headers("Authorization"));
            String gameBoard = gameService.getBoard(body.gameID());
            response.status(200);
            return new Gson().toJson(Map.of("gameBoard", gameBoard, "playerColor", body.playerColor() != null ? body.playerColor(): "WHITE", "gameID", body.gameID()));
        } catch(DataAccessException e){
            response.status(e.getStatus());
            return new Gson().toJson(new ErrorMessage(e.getMessage()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Object listGame(Request request, Response response) {
        try {
            userService.checkAuth(request.headers("Authorization"));
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
            gameService.joinGame(request.headers("Authorization"), body.playerColor(), body.gameID(), userName);
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
