package org.example.Client.Model;

import org.example.NET.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ClientHandler implements Logger {

    @Override
    public void writeLog(String message) {
        try(FileWriter fileWriter = new FileWriter(".\\src\\main\\java\\org\\example\\NET\\logging.txt", true)){
            fileWriter.write(message+"\n");

        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
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
}
