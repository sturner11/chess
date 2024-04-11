package server.websocket;


import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.ServerMessage;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String username, Session session) {
        try {
            var connection = new Connection(username, session);
            connections.put(username, connection);
        } catch (Exception e) {
            var test = e;
            System.out.println(test);
        }
    }

    public void remove(String username) {
        connections.remove(username);
    }

    public void broadcast(String username, ServerMessage serverMessage) throws IOException {
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.username.equals(username)) {
                    c.send(new Gson().toJson(serverMessage));
                }
            }
        }
    }

    public void send(String username, ServerMessage serverMessage) throws IOException {
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (c.username.equals(username)) {
                    c.send(new Gson().toJson(serverMessage));
                }
            }
        }
    }

    public void sendAll(ServerMessage notification) throws IOException {
        for (var c : connections.values()) {
//            if (c.session.isOpen()) {
                    c.send(new Gson().toJson(notification));
//            }
        }
    }

}
