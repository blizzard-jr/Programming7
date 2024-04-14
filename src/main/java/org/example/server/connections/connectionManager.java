package org.example.server.connections;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class connectionManager {
    private static Socket clientSocket;
    private static ServerSocket server;
    private static BufferedReader in;
    private static BufferedWriter out;
    public static void main(String[] args) throws IOException {
        try{
            server = new ServerSocket(4004);
            System.out.println("Server is ready!");
            clientSocket = server.accept();
            try{
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                String word = in.readLine();
                out.write("Hello, it`s a server, your message is " + word);
            }finally{
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally{
            server.close();
        }
    }
}
