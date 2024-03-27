package org.example.Server.Model;

import org.example.Client.Model.Client;
import org.example.NET.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ServerHandler implements Logger {
    public static DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    public static DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("HH:mm:ss");

    private HashMap<UUID, Client> clients;



    public ServerHandler() {
        clients = new HashMap<>();

    }

    public List<String> getListClients() {
        List<String> nameClients = new ArrayList<>();
        for (Client client: clients.values()
             ) {
            nameClients.add(client.getName());
        }
        return nameClients;
    }

    public void addClient(Client client){
        clients.put(UUID.randomUUID(), client);
        writeLog("new client" + client + "was added!\n");
    }

    public void writeLog(String message) {
        try(FileWriter fileWriter = new FileWriter(".\\src\\main\\java\\org\\example\\NET\\logging.txt", true)){
            fileWriter.write(message);

        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public String readLog() {

        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(".\\src\\main\\java\\org\\example\\NET\\logging.txt"))) {
            String firstLine = reader.readLine();
            String line;
            if(firstLine==null){
                return "History is empty!\n";
            }else stringBuilder.append(firstLine).append("\n");
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();

    }

    public void start(int PORT) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен на порту " + PORT);
            writeLog("Сервер запущен на порту " + PORT);
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Подключился новый клиент: " + clientSocket);

                    PrintWriter clientOut = new PrintWriter(clientSocket.getOutputStream(), true);
                    clientOut.println("Подключение успешно. Пришлите свой идентификатор");

                    Scanner clientIn = new Scanner(clientSocket.getInputStream());
                    String clientId = clientIn.nextLine();
                    System.out.println("Идентификатор клиента " + clientSocket + ": " + clientId);

                    String allClients = clients.entrySet().stream()
                            .map(it -> "id = " + it.getKey() + ", client = " + it.getValue().getClientSocket())
                            .collect(Collectors.joining("\n"));
                    clientOut.println("Список доступных клиентов: \n" + allClients);

                    ClientHandler clientHandler = new ClientHandler(clientSocket, clients);
                    new Thread(clientHandler).start();

                    for (ClientHandler client : clients.values()) {
                        client.send("Подключился новый клиент: " + clientSocket + ", id = " + clientId);
                    }
                    clients.put(clientId, clientHandler);
                } catch (IOException e) {
                    System.err.println("Произошла ошибка при взаимодействии с клиентом: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось начать прослушивать порт " + PORT, e);
        }
    }
    }
}
