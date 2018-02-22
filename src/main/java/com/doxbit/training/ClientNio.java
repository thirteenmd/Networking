package com.doxbit.training;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientNio {
    public static Set<Integer> ports = new HashSet<>();
    public static String hostname = "localhost";

    public static void testConnection(String hostname, Integer port) throws IOException {
        InetSocketAddress address = new InetSocketAddress(hostname, port);
        SocketChannel client = SocketChannel.open(address);

        String message = port.toString() + " free?";

        byte[] m = message.getBytes();
        ByteBuffer messageBuffer = ByteBuffer.wrap(m);
        client.write(messageBuffer);

        messageBuffer.clear();

        ByteBuffer confirmationBuffer = ByteBuffer.allocate(256);
        client.read(confirmationBuffer);
        String confirmation = new String(confirmationBuffer.array()).trim();

        if (confirmation.equals("Response")){
            System.err.println("Confirmation received from port " + port.toString() + "! " + confirmation);
        }else {
            System.err.println("No confirmation from port "+ port.toString() + "!");
        }

        confirmationBuffer.clear();
        client.close();
    }

    public static boolean isIp(String text) {
        Pattern p = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
        Matcher m = p.matcher(text);
        return m.find();
    }

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
}
