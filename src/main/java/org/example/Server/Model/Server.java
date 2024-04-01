package org.example.Server.Model;
import org.example.NET.Connection;
import org.example.Server.GUI.ServerWindow;

import java.util.List;
import java.util.Timer;
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
       sendServiceMessage("Server is started!");

    }

    public void stopListening() {
        serverHandler.setStatus(false);
        sendServiceMessage("Server is stopped!");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            serverHandler.stopServer();
        }));
        serverHandler.stopServer();
    }

    public void sendServiceMessage(String str) {
        serverWindow.appendText(str);
    }
}
