package org.example.NET;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//Инкапсулируем класс с сетью (1 соединение клиент---сервер)
public class Connection {
    private Socket socket;

    private  Thread rxThread;
    private final ConnectionObserver eventListener;
    String nameClient;
    List<String> messages;
    String newMsg;
    boolean isRegister = false;

    // Потоки ввода-вывода
    private  BufferedReader in;
    private  BufferedWriter out;
    protected DataInputStream inStream;
    protected DataOutputStream outStream;


    public Connection(ConnectionObserver eventListener, String ipAdress, int port, String nameClient) throws IOException {
        this(new Socket(ipAdress, port), eventListener);
        this.nameClient = nameClient;
        this.messages = new ArrayList<>();
    }

    public Connection(Socket socket, ConnectionObserver connectionObserver) throws IOException{
        this.socket = socket;
        this.eventListener = connectionObserver;
        try{
            inStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            outStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            rxThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //Передали экземпляр обрамляющего класса
                        eventListener.onConnectionReady(Connection.this);
                        while (!rxThread.isInterrupted()) {
                            newMsg = inStream.readUTF();
                            eventListener.onReceiveString(Connection.this, newMsg);
                            newMsg="";
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

    public void registeredClient(){
        isRegister = true;
    }

    public String getClientName() {
        return nameClient;
    }

    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

    public Thread getRxThread() {
        return rxThread;
    }

    public String getMsg() {
        return newMsg;
    }

    public DataInputStream getInStream() {
        return inStream;
    }

    public DataOutputStream getOutStream() {
        return outStream;
    }


    public ConnectionObserver getEventListener() {
        return eventListener;
    }
}
