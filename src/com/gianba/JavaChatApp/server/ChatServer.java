package com.gianba.JavaChatApp.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer{

    //    List all clients connected
    private static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
//        Open serverSocket conn
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Server started. Waiting for clients..");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket);

            // Spawn a new thread for each client
            ClientHandler clientThread = new ClientHandler(clientSocket, clients);
            clients.add(clientThread);
            new Thread(clientThread).start();
        }


    }
    static class ClientHandler implements Runnable{
        private Socket clientSocket;
        private List<ClientHandler> clients;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket clientSocket, List<ClientHandler> clients) throws IOException {
            this.clientSocket = clientSocket;
            this.clients = clients;
            this.out = new PrintWriter(clientSocket.getOutputStream(),true);
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }

        @Override
        public void run() {
            try {
                String inputLine;
                while (((inputLine = in.readLine()) != null)){
                    for (ClientHandler aClient : clients
                    ) {
                        aClient.out.println(inputLine);
                    }
                }
            } catch (IOException e) {
                System.out.println("An error occurred: " + e.getMessage());
            }finally {
                try {
                    in.close();
                    out.close();
                    clientSocket.close();
                }catch (IOException e ){
                    e.printStackTrace();
                }
            }
        }
    }
}
