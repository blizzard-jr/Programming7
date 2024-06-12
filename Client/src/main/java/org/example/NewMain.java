package org.example;

import com.sun.tools.javac.Main;

public class NewMain {
    public static void main(String[] args){
        try {
            UserInterface.main(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
