package com.gianba.JavaChatApp.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatClientGUI extends JFrame {
    private JTextArea messageArea;
    private JTextField textField;
    private ChatClient client;
    private JButton exitButton;

    public ChatClientGUI(){
        super("Chat Application");
        setSize(400,500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        add(new JScrollPane(messageArea), BorderLayout.CENTER);

        String name = JOptionPane.showInputDialog(this,"Enter you name","Entry name",JOptionPane.PLAIN_MESSAGE);
        this.setTitle("Chat Application - " + name);

        textField = new JTextField();
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = "[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] " + name + ": " + textField.getText();
                client.sendMessage(message);
                textField.setText("");
            }
        });
        add(textField,BorderLayout.SOUTH);

        exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> {

//            Trying to send departuremessage before close chat / Not working
//            String departureMessage = name + " has left the chat";
//            client.sendMessage(departureMessage);

            System.exit(0);
        });
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(textField, BorderLayout.CENTER);
        bottomPanel.add(exitButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        try {


            this.client= new ChatClient("127.0.0.1",5000,this::onMessageReceived);
            client.startClient();
        }catch (IOException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Error connecting to the server","Connection error",JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    private void onMessageReceived(String message){
        SwingUtilities.invokeLater(() -> messageArea.append(message + "\n"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ChatClientGUI().setVisible(true);
        });
    }
}
