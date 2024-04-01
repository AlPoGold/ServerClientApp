package org.example.Client.GUI;

import org.example.Client.Model.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ClientWindow extends JFrame {
    public static DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    public static DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("HH:mm:ss");
    private String TITLE = "Chat Client";
    private final int WIDTH = 400;
    private final int HEIGHT = 400;

    private Client client;


    JPanel mainPanel;

    private JPanel logPanel;
    private JTextArea textArea;
    private JTextField textField;



    private String nameClient;
    private String message;

    public JTextArea getTextArea() {
        return textArea;
    }

    public String getNameClient() {
        return nameClient;
    }

    public ClientWindow(Client client) {
        this.client = client;
        initWindow();

    }
    private void initWindow() {

        setSize(WIDTH, HEIGHT);
        setTitle(TITLE);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(createMainPanel());

        setVisible(true);
    }

    private JPanel createMainPanel() {

        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        logPanel = createLogPanel();
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);



        textArea.setEditable(false);
        JPanel messagePan = createMessagePan();

        addComponent(logPanel, 0, 0, 1, 1, 1.0, 1.0);
        addComponent(scrollPane, 0, 1, 1, 1, 3.0, 16.0);
        addComponent(messagePan, 0, 2, 1, 1, 1.0, 1.0);
        return mainPanel;
    }

    private void addComponent(JComponent label, int gridx, int gridy, int gridwidth, int gridheight,
                              double weightx, double weighty) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(label, gbc);
    }

    private JPanel createMessagePan() {
        JPanel jpanel = new JPanel(new GridLayout(1, 2));


        textField = new JTextField();
        textField.setEditable(false);
        JButton sendBtn = new JButton("SEND");
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                message = textField.getText();
                textField.setText("");
                try {
                    client.sendMessage(message);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

        });

        sendBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                message = textField.getText();
                textField.setText("");
                try {
                    client.sendMessage(message);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        jpanel.add(textField);
        jpanel.add(sendBtn);

        return jpanel;
    }



    private JPanel createLogPanel() {

        JPanel jpanel = new JPanel(new GridLayout(2, 3));

        JTextField ipField = new JTextField();
        ipField.setText("localhost");
        JTextField portField = new JTextField();
        portField.setText("8181");

        JPasswordField passwordField = new JPasswordField("password");
        passwordField.setEchoChar('\u2022');

        JTextField loginField = new JTextField();
        loginField.setText("polina");

        JButton btnLogin = new JButton("LOGIN");
        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                nameClient = loginField.getText();
                logPanel.setVisible(false);
                String message = String.format("%s: %s have been registered!", LocalTime.now().format(formatTime), nameClient);
                client.registered(nameClient);


                try {
                    client.sendMessage(message);
                    client.setRegistered(true);
                } catch (IOException ex) {
                    System.out.println("Something has gone wrong with login");
                }
                textField.setEditable(true);
            }
        });

        jpanel.add(ipField);
        jpanel.add(portField);
        jpanel.add(loginField);
        jpanel.add(passwordField);
        jpanel.add(btnLogin);


        return jpanel;

    }

    public void sendMessage(String value) throws IOException {
        String message = String.format("%s: %s", LocalTime.now().format(formatTime), value);
        textArea.append(message+"\n");


    }

    public String getMessage() {
        return textField.getText();
    }
}
