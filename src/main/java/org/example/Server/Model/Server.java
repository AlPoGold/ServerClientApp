package org.example.Server.Model;
import org.example.NET.Connection;
import org.example.Server.GUI.ServerWindow;

import java.util.List;
import java.util.stream.Collectors;

public class Server {
    public static final int PORT = 8181;
    private ServerHandler serverHandler;
    private ServerWindow serverWindow;

    public static void main(String[] args) {
        new Server();
    }
    public Server() {
        serverWindow = new ServerWindow(this);
        serverHandler = new ServerHandler(this);

    }
    
    

    public ServerHandler getServerHandler() {
        return serverHandler;
    }

    public void startListening() {
        serverHandler.setStatus(true);
        serverHandler.startServer();
        serverWindow.appendText("Server is started!");

    }

    public void stopListening() {
        serverHandler.stopServer();
        serverHandler.setStatus(false);
        serverWindow.appendText("Server is stopped!");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            serverHandler.stopServer();
        }));
    }

    public void showAllMessages(List<Connection> connections) {
        for (Connection conn: connections
             ) {
            serverWindow.appendText(conn.getMessages().stream().collect(Collectors.joining()));
        }
    }
}
