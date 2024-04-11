package server.websocket;


import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.ServerMessage;

public class ConnectionManager {
    public final HashMap<String, ConcurrentHashMap<String, Connection>> allConnections = new HashMap<>();

    public void add(String username, Session session, String gameID) {
        try {
            var connection = new Connection(username, session);
            if (allConnections.get(gameID) == null){
                ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
                allConnections.put(gameID, connections);
            }
            ConcurrentHashMap<String, Connection> connections = allConnections.get(gameID);
            connections.put(username, connection);
            allConnections.put(gameID, connections);
        } catch (Exception e) {
            var test = e;
            System.out.println(test);
        }
    }

    public void remove(String username, String gameID) {
        ConcurrentHashMap<String, Connection> connections = allConnections.get(gameID);
        connections.remove(username);
        allConnections.put(gameID, connections);
    }

    public void broadcast(String username, ServerMessage serverMessage, String gameID) throws IOException {
        ConcurrentHashMap<String, Connection> connections = allConnections.get(gameID);
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.username.equals(username)) {
                    c.send(new Gson().toJson(serverMessage));
                }
            }
        }
    }

    public void send(String username, ServerMessage serverMessage, String gameID) throws IOException {
        ConcurrentHashMap<String, Connection> connections = allConnections.get(gameID);
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (c.username.equals(username)) {
                    c.send(new Gson().toJson(serverMessage));
                }
            }
        }
    }

    public void sendAll(ServerMessage notification, String gameID) throws IOException {
        ConcurrentHashMap<String, Connection> connections = allConnections.get(gameID);
        for (var c : connections.values()) {
//            if (c.session.isOpen()) {
                    c.send(new Gson().toJson(notification));
//            }
        }
    }

}
