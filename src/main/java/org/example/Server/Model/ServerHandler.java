package org.example.Server.Model;

import org.example.NET.Connection;
import org.example.NET.ConnectionObserver;
import org.example.NET.Logger;
import java.io.*;
import java.net.ServerSocket;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ServerHandler implements Logger, ConnectionObserver {
    public static final int PORT = 8181;
    public static final String ipAddress = "localhost";
    public static DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    public static DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("HH:mm:ss");
    private Thread serverThread;
    private Server server;
    protected static List<Connection> handlers = Collections.synchronizedList(new ArrayList<Connection>());


    private HashMap<String, Connection> clients;
    private Connection clientConnection;
    private List<Connection> connections;
    private boolean isServerUp = false;




    public ServerHandler(Server server) {
        clients = new HashMap<String, Connection>();
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
            updateServer();
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
                        handlers.add(clientConnection);
                        connections.add(clientConnection);
                        server.sendServiceMessage("SERVER: " + clientConnection + " was added!");

                    } catch (IOException e) {
                        writeLog(e.getMessage());
                        break;
                    }

                }

            } catch (IOException e) {
                System.out.println("Can't connect to server");
                writeLog("Can't connect to server");
            }
        } else {
            for (Connection conn: handlers
                 ) {
                conn.disconnect();
            }
            serverThread.interrupt();
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

    protected static void broadcast(String message) {
        for (Connection conn: handlers
             ) {
            try {
                conn.getOutStream().writeUTF(message);
                conn.getOutStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addClient(Connection connection) {
        clients.put(connection.getClientName(), connection);
        writeLog("new client:" + connection.getClientName() + " was added!\n");
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
    public void onConnectionReady(Connection connection) {
        broadcast("Connection is up for " + connection);

    }
    @Override
    public void onReceiveString(Connection connection, String value) {

        // TODO : WHY IS IT NOT WORKING??
       server.sendServiceMessage(value);
       if(value.contains("have been registered")){
           registeredClient(connection, connection.getClientName());
       }else if(Objects.equals(value, "/exit")){
            onDisconnect(connection);
        }else if(Objects.equals(value, "/all")){
            broadcast(allMembers());
        }else{
           broadcast(value);
           System.out.println(allMembers());
       }

    }

    private String allMembers() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Connection> entry: clients.entrySet()
             ) {
            sb.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public void onDisconnect(Connection connection) {
        server.sendServiceMessage(connection +  " is disconnected");
        broadcast(connection +  " is disconnected");
        connections.remove(connection);
        connection.disconnect();
    }

    @Override
    public void onException(Connection connection, Exception e) {
        writeLog(connection + ": exception" + e.getMessage());
    }

    @Override
    public void registeredClient(Connection connection, String name) {
        addClient(connection);
    }
}



