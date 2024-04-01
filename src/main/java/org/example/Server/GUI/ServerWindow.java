package org.example.Server.GUI;

import org.example.Server.Model.Server;
import org.example.Server.Model.ServerHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

public class ServerWindow extends JFrame{
    public static final int HEIGHT = 500;
    public static final int WIDTH = 500;
    private final String TITLE = "Chat Server";

    private Server server;

    //widgets
    private JTextArea txtField;
    private JButton btnStart;
    private JButton btnStop;


    public ServerWindow(Server server) {
        this.server = server;
        initWindow();
    }

    private void initWindow() {
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
                server.startListening();

            }
        });

        btnStop.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                server.stopListening();

            }
        });

        jPanelBtn.add(btnStart);
        jPanelBtn.add(btnStop);
        jPanelBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        jPanel.add(jPanelBtn, BorderLayout.SOUTH);
        return jPanel;
    }

    public void appendText(String text) {
        try {
            txtField.append(text+"\n");
        } catch (RuntimeException e) {
            System.out.println("can't append text");
        }
    }

}
