package org.example.Server.Model;

import org.example.Client.Model.Client;
import org.example.NET.Connection;
import org.example.NET.ConnectionObserver;
import org.example.NET.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ServerHandler implements Logger, ConnectionObserver {
    public static final int PORT = 8181;
    public static final String ipAddress = "localhost";
    public static DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    public static DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("HH:mm:ss");
    private Thread serverThread;
    private Server server;

    private HashMap<UUID, Connection> clients;
    private Connection clientConnection;
    private List<Connection> connections;
    private boolean isServerUp = false;


    public ServerHandler(Server server) {
        clients = new HashMap<>();
        connections = new ArrayList<>();
        this.server = server;
    }

    public void startServer() {
        serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                updateServer();
            }
        });
        serverThread.start();
    }

    public void stopServer() {
        if (serverThread != null) {
            serverThread.interrupt();
        }
    }

    public void setStatus(boolean workingServer) {
        isServerUp = workingServer;
    }

    private void updateServer() {
        if (isServerUp) {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                System.out.println("Server is UP!");
                while (true) {
                    try {
                        clientConnection = new Connection(serverSocket.accept(), this);
                        connections.add(clientConnection);
                        writeLog("add new client");

                    } catch (IOException e) {
                        writeLog(e.getMessage());
                    }

                    server.showAllMessages(connections);
                }

            } catch (IOException e) {
                System.out.println("Can't connect to server");
                writeLog("Can't connect to server");
            }
        } else {
            System.out.println("else-block");
        }
    }

    public List<String> getListClients() {
        List<String> nameClients = new ArrayList<>();
        for (Connection conn : clients.values()
        ) {
            nameClients.add(conn.getClientName());
        }
        return nameClients;
    }

    public void addClient(Connection connection) {
        clients.put(UUID.randomUUID(), connection);
        writeLog("new client" + connection.getClientName() + "was added!\n");
    }

    public void writeLog(String message) {
        try (FileWriter fileWriter = new FileWriter(".\\src\\main\\java\\org\\example\\NET\\logging.txt", true)) {
            fileWriter.write(message + "\n");

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public String readLog() {

        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(".\\src\\main\\java\\org\\example\\NET\\logging.txt"))) {
            String firstLine = reader.readLine();
            String line;
            if (firstLine == null) {
                return "History is empty!\n";
            } else stringBuilder.append(firstLine).append("\n");
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();

    }

    @Override
    public synchronized void onConnectionReady(Connection connection) {
        connections.forEach(System.out::println);

    }

//    @Override
//    public synchronized void onReceiveString(Connection connection, String value) {
//
//    }

    @Override
    public synchronized void onDisconnect(Connection connection) {

        connections.remove(connection);
        sendAllConnections(connection + "is disconnected");
        stopServer();

    }

    @Override
    public synchronized void onException(Connection connection, Exception e) {
        writeLog(connection + ": exception" + e.getMessage());
    }


    private void sendAllConnections(String message) {
        connections.stream().forEach(it -> it.sendMessage(message));
    }

    public Connection getClientConnection() {
        return clientConnection;
    }

    @Override
    public synchronized void onReceiveString(Connection connection, String value) {
        // Broadcast the received message to all clients
        for (Connection client : connections) {
            client.sendMessage(value);
        }
    }
}



