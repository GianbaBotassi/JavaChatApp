package com.gianba.JavaChatApp.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.function.Consumer;

public class ChatClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out = null;
    private Consumer<String> onMessageReceived;

    public ChatClient(String serverAddress, int serverPort,Consumer<String> onMessageReceived) throws IOException {
        socket = new Socket(serverAddress, serverPort);
        System.out.println("Connected to the chat server");
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        this.onMessageReceived = onMessageReceived;
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }
    public void startClient() {
        new Thread(() -> {
            try {
                String line = "";
                while ((line = in.readLine()) != null) {
                    onMessageReceived.accept(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public BufferedReader getIn() {
        return in;
    }
}
