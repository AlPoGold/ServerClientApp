package org.example.Server.Model;

import org.example.Client.Model.Client;
import org.example.Server.GUI.ServerWindow;

import java.util.List;

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

        
    }

    public void stopListening() {
        serverHandler.setStatus(false);
    }
}
