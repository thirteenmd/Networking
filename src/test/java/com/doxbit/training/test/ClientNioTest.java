package com.doxbit.training.test;

import com.doxbit.training.ClientNio;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import static com.doxbit.training.ClientNio.isIp;
import static com.doxbit.training.ClientNio.isNumeric;

public class ClientNioTest {

    public static Set<Integer> ports;

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("check.txt");
        try{
            BufferedReader reader = Files.newBufferedReader(path, Charset.forName("UTF-8"));
            String currentLine = null;
            while((currentLine = reader.readLine()) != null){
                if (isNumeric(currentLine)){
                    ClientNio.ports.add(Integer.parseInt(currentLine));
                }
                if (isIp(currentLine)){
                    ClientNio.hostname = currentLine;
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        Thread secondThread = new Thread(new Runnable() {
            @Override
            public void run() {
                ServerNioTest.ports = ClientNio.ports;
                try {
                    ServerNioTest.main(null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
        secondThread.start();

        for (Integer p: ClientNio.ports){
            ClientNio.testConnection(ClientNio.hostname, p);
        }

        System.exit(0);
    }
}
