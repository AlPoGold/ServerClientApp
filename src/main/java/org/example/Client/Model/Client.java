package org.example.Client.Model;

import org.example.Client.GUI.ClientWindow;
import org.example.NET.Connection;
import org.example.NET.ConnectionObserver;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.UUID;

public class Client implements ConnectionObserver {

    private final String IP_ADDRESS = "localhost";
    private final int PORT = 8181;
    String name;
    UUID uuid;

    ClientHandler clientHandler;
    ClientWindow clientWindow;
    Connection connection;

    public static void main(String[] args) {
        new Client();
    }

    public Client() {
        clientWindow = new ClientWindow(this);
        clientHandler = new ClientHandler();
        try{
            name = clientWindow.getNameClient();
            this.connection = new Connection(this, IP_ADDRESS, PORT, name);

            connection.sendMessage(name + "was registered");
        }catch(IOException e){
            System.out.println("No connection" + e.getMessage());
        }



    }


    public String getName() {
        name = clientWindow.getNameClient();
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void sendMessage(String message) {
        connection.sendMessage(message);
    }

    @Override
    public void onConnectionReady(Connection connection) {
        System.out.println("Connection ready");

    }

    @Override
    public void onReceiveString(Connection connection, String value) {
        clientWindow.sendMessage(value);

    }

    @Override
    public void onDisconnect(Connection connection) {
        connection.disconnect();
        System.out.println("Connection close");
    }

    @Override
    public void onException(Connection connection, Exception e) {
        System.out.println("Exception:" + e.getMessage());
    }


    public void getMessage(String message) {
        connection.sendMessage(message);
        clientHandler.writeLog(message);
    }
}
