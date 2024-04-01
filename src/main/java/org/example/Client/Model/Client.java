package org.example.Client.Model;

import org.example.Client.GUI.ClientWindow;
import org.example.NET.Connection;
import org.example.NET.ConnectionObserver;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.UUID;

public class Client implements ConnectionObserver{

    private final String IP_ADDRESS = "localhost";
    private final int PORT = 8181;
    String name;
    boolean isRegistered = false;

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

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Connection getConnection() {
        return connection;
    }

    public void sendMessage(String message) throws IOException {
        connection.getOutStream().writeUTF(getName() + " : " + message);
        connection.getOutStream().flush();


    }

    @Override
    public void onConnectionReady(Connection connection) {
        try {
            clientWindow.sendMessage("Connection ready");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onReceiveString(Connection connection, String value) {
        try{
            clientWindow.sendMessage(value);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void onDisconnect(Connection connection){
        try {
            clientWindow.sendMessage("Connection close");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        connection.disconnect();
    }

    @Override
    public void onException(Connection connection, Exception e) {
        System.out.println("Exception:" + e.getMessage());
    }


}
