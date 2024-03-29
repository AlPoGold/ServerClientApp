package org.example.Client.Model;

import org.example.Client.GUI.ClientWindow;
import org.example.NET.Connection;
import org.example.NET.ConnectionObserver;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.UUID;

public class Client implements ConnectionObserver, ActionListener {

    private final String IP_ADDRESS = "localhost";
    private final int PORT = 8181;
    String name;
    UUID uuid;

    ClientHandler clientHandler;
    ClientWindow clientWindow;
    Connection connection;

    public static void main(String[] args) throws InterruptedException {
        new Client();

    }



    public Client() throws InterruptedException {
        clientWindow = new ClientWindow(this);
        clientHandler = new ClientHandler();

        try{

            this.connection = new Connection(this, IP_ADDRESS, PORT, name="default");

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

    public Connection getConnection() {
        return connection;
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
        //TODO send message

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


    public String getMessage() {
            return clientWindow.getMessage();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        connection.sendMessage(getMessage());
    }
}
