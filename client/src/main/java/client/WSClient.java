//package client;
//import javax.websocket.Endpoint;
//import javax.websocket.EndpointConfig;
//import javax.websocket.Session;
//import java.net.URI;
//import java.util.Scanner;
//
//public class WSClient extends Endpoint {
//
//    public static void main(String[] args) throws Exception {
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.println("Enter a message you want to echo");
//        while (true) ws.send(scanner.nextLine());
//    }
//
//    public Session session;
//
//    public WSClient() throws Exception {
//        URI uri = new URI("ws://localhost:8080/connect");
//        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
//        this.session = container.connectToServer(this, uri);
//
//        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
//            public void onMessage(String message) {
//                System.out.println(message);
//            }
//        });
//    }
//
//    @Override
//    public void onOpen(Session session, EndpointConfig endpointConfig) {
//
//    }
//
//    public void send(String msg) throws Exception {
//        this.session.getBasicRemote().sendText(msg);
//    }
//
//    public void onOpen(Session session, EndpointConfig endpointConfig) {
//    }
//}