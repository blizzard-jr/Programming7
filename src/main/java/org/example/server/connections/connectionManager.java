package org.example.server.connections;

import org.example.commands.Command;
import org.example.server.details.Serialization;

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
                InputStreamReader reader = new InputStreamReader(clientSocket.getInputStream());
                
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
