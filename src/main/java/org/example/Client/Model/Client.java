package org.example.Client.Model;

import org.example.Client.GUI.ClientWindow;
import org.example.NET.Connection;
import org.example.NET.ConnectionObserver;
import org.example.Server.Model.Server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;

public class Client implements ConnectionObserver, ActionListener {

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
        clientWindow = new ClientWindow();
        clientHandler = new ClientHandler();
        try{
            this.connection = new Connection(this, IP_ADDRESS, PORT);
        }catch(IOException e){
            System.out.println("No connection" + e.getMessage());
        }

    }


    public String getName() {

        return name;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

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
        System.out.println("Connection close");
    }

    @Override
    public void onException(Connection connection, Exception e) {
        System.out.println("Exception");
    }

    private synchronized void printMessage(String msg){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                clientHandler.writeLog(msg + "\n");
            }
        });
    }
}
