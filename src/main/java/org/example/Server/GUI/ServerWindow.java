package org.example.Server.GUI;

import org.example.Server.Model.ServerHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ServerWindow extends JFrame {
    public static final int HEIGHT = 500;
    public static final int WIDTH = 500;
    private final String TITLE = "Chat Server";

    public boolean isServerUp() {
        return isServerUp;
    }

    private boolean isServerUp;

    //widgets
    private JTextArea txtField;
    private JButton btnStart;
    private JButton btnStop;

    //services
    private ServerHandler serverHandler;


    public ServerWindow(ServerHandler serverHandler) {
        initWindow();
        this.serverHandler = serverHandler;

    }

    private void initWindow(){
        setSize(WIDTH, HEIGHT);
        setTitle(TITLE);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel jPanel = createMainPanel();
        add(jPanel);

        setVisible(true);
    }

    private JPanel createMainPanel() {
        JPanel jPanel = new JPanel(new BorderLayout());
        txtField = new JTextArea();
        txtField.setEditable(false);
        jPanel.add(txtField, BorderLayout.CENTER);
        JScrollPane scrollPane = new JScrollPane(txtField);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        jPanel.add(scrollPane, BorderLayout.CENTER);


        JPanel jPanelBtn = new JPanel(new GridLayout(1, 2));
        btnStart = new JButton("START SESSION");
        btnStop = new JButton("STOP SESSION");

        btnStart.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                updateStatus("Server was started ", true);
                appendText(serverHandler.readLog());
            }
        });

        btnStop.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                updateStatus("Server was stopped ", false);

            }
        });

        jPanelBtn.add(btnStart);
        jPanelBtn.add(btnStop);
        jPanelBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        jPanel.add(jPanelBtn, BorderLayout.SOUTH);


        return jPanel;
    }

    private void updateStatus(String text, boolean isServer){

        String message = String.format(text + LocalDateTime.now().format(ServerHandler.formatDateTime) + "!\n");
        appendText(message);
        serverHandler.writeLog(message);
        isServerUp = isServer;

    }

    public void appendText(String text) {
        try {
            txtField.append(text);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
