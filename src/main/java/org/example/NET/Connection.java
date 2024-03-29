package org.example.NET;




import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

//Инкапсулируем класс с сетью (1 соединение клиент---сервер)
public class Connection {
    private Socket socket;

    private  Thread rxThread;
    private final ConnectionObserver eventListener;
    String nameClient;
    List<String> messages;
    String newMsg;

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
//            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
//            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            inStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            outStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            rxThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //Передали экземпляр обрамляющего класса
                        eventListener.onConnectionReady(Connection.this);
                        while (!rxThread.isInterrupted()) {
                            newMsg =inStream.readUTF();
                            System.out.println("in thread");
                            System.out.println(newMsg);
                            eventListener.onReceiveString(Connection.this, newMsg);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
//                    }catch (IOException e){
//                        System.out.println(e.getMessage());
//                        eventListener.onException(Connection.this, e);
//                    }finally{
//                        eventListener.onDisconnect(Connection.this);
//                    }
                }
            });
            rxThread.start();
        }catch (IOException e){
            System.out.println("No connection: "+ e.getMessage());
        }
    }


//    public synchronized void sendMessage(String newMessage){
//        try{
//            //перевод каретки в начало и новая строка
//            messages.add(newMessage);
//            out.write(newMessage + "\r\n");
//            msg = newMessage;
//            out.flush();
//        }catch (Exception e){
//            eventListener.onException(Connection.this, e);
//        }
//
//    }




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

    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
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

    public void sendMessage(String message) {
        newMsg = message;
    }
}
