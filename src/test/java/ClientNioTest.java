import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;

public class ClientNioTest {
    private static List<Integer> ports = ServerNioTest.ports;

    public static void main(String[] args) throws IOException, InterruptedException {
        String hostname = "localhost";
        for (Integer p: ports){
            testConnection(hostname, p);
            Thread.sleep(2000);
        }
    }

    private static void testConnection(String hostname, Integer port) throws IOException {
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
            System.out.println("Confirmation received from port " + port.toString() + "! " + confirmation);
        }else {
            System.out.println("No confirmation from port "+ port.toString() + "!");
        }

        confirmationBuffer.clear();
        client.close();
    }

}
