package org.example.Client.Model;

import org.example.Client.GUI.ClientWindow;
import org.example.Server.Model.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;

public class Client {

    String name;
    UUID uuid;

    ClientHandler clientHandler;
    ClientWindow clientWindow;

    public Client() {
        clientWindow = new ClientWindow();
        clientHandler = new ClientHandler();

    }

    //    public static void main(String[] args) {
//        try {
//            Socket serverSocket = new Socket("localhost", Server.PORT);
//            System.out.println("Подключились к серверу: tcp://localhost:" + Server.PORT);
//
//            // Читаем с сервера приветственное сообщение
//            Scanner serverIn = new Scanner(serverSocket.getInputStream());
//            String input = serverIn.nextLine();
//            System.out.println("Сообщение от сервера: " + input);
//
//            // Отправили идентфиикатор на сервер
//            new PrintWriter(serverSocket.getOutputStream(), true).println(UUID.randomUUID());
//
//            new Thread(new ServerReader(serverSocket)).start();
//            new Thread(new ServerWriter(serverSocket)).start();
//        } catch (IOException e) {
//            throw new RuntimeException("Не удалось подключиться к серверу: " + e.getMessage(), e);
//        }

//    }

    public String getName() {

        return name;
    }
}