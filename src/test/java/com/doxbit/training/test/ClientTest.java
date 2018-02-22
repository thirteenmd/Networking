package com.doxbit.training.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ClientTest {
    public static void main(String[] args){
        ClientTest client = new ClientTest();
        try {
            client.message();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void message() throws IOException {
        Socket socket = new Socket("localhost", 4321);
        PrintStream printStream = new PrintStream(socket.getOutputStream());
        printStream.println("Hello to server from client!");

        InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String message = bufferedReader.readLine();
        System.out.println(message);
    }
}
