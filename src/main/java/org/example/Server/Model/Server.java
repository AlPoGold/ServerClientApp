package org.example.Server.Model;

import org.example.Client.Model.Client;
import org.example.Server.GUI.ServerWindow;

import java.util.List;

public class Server {
    public static final int PORT = 8181;
    private ServerHandler serverHandler;
    private ServerWindow serverWindow;


    public Server() {
        serverHandler = new ServerHandler();
        serverWindow = new ServerWindow(serverHandler);

    }

    public void start(){
        serverHandler.start(PORT);
    }


}
