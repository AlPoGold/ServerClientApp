package org.example.NET;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

//Инкапсулируем класс с сетью (1 соединение клиент---сервер)
public class Connection {
    private Socket socket;
    private  Thread rxThread;
    private final ConnectionObserver eventListener;
    String nameClient;
    List<String> messages;

    // Потоки ввода-вывода
    private  BufferedReader in;
    private  BufferedWriter out;


    public Connection(ConnectionObserver eventListener, String ipAdress, int port, String nameClient) throws IOException {
        this(new Socket(ipAdress, port), eventListener);
        this.nameClient = nameClient;
        this.messages = new ArrayList<>();
    }

    public Connection(Socket socket, ConnectionObserver connectionObserver) throws IOException{
        this.socket = socket;
        this.eventListener = connectionObserver;
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            rxThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        //Передали экземпляр обрамляющего класса
                        eventListener.onConnectionReady(Connection.this);
                        while(!rxThread.isInterrupted()){
                            String msg = in.readLine();
                            eventListener.onReceiveString(Connection.this, msg);
                        }

                    }catch (IOException e){
                        System.out.println(e.getMessage());
                        eventListener.onException(Connection.this, e);
                    }finally{
                        eventListener.onDisconnect(Connection.this);
                    }
                }
            });
            rxThread.start();
        }catch (IOException e){
            System.out.println("No connection: "+ e.getMessage());
        }
    }

    public synchronized void sendMessage(String newMessage){
        try{
            //перевод каретки в начало и новая строка
            messages.add(newMessage);
            out.write(newMessage + "\r\n");
            out.flush();
        }catch (Exception e){
            eventListener.onException(Connection.this, e);
        }

    }


    public synchronized void disconnect(){
        rxThread.interrupt();
        try{
            socket.close();
        }catch(IOException e){
            eventListener.onException(Connection.this, e);
        }

    }

    @Override
    public String toString() {
        return "Connection: " + socket.getInetAddress() + " : " + socket.getPort();
    }

    public String getClientName() {
        return nameClient;
    }

    public List<String> getMessages() {
        return messages;
    }

    public synchronized String receiveMessage() throws IOException {
        return in.readLine();
    }

    public synchronized void sendNewMessage(String message) throws IOException {
        out.write(message + "\r\n");
        out.flush();
    }
}
