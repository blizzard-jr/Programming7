package org.example.main;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.stream.Stream;

public class Main {
    private static Socket client;
    private static BufferedWriter out;
    public static void main(String[] args) {
        try {
            client = new Socket("localhost", 4004);
            out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            out.write("Ну чё нахуй, межсетевой привет вам ёпт");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}