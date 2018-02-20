import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ServerNioTest {

    public static List<Integer> ports = new ArrayList<Integer>(){{add(4321); add(3219); add(1501); add(1500); add(4458);}};

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();

        for (Integer port: ports){
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            System.out.println("listen: " + port);
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        }

        while (true){
            System.out.println("Waiting for new connection...");
            selector.select();

            Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();

            while (selectedKeys.hasNext()){
                SelectionKey selectedKey = selectedKeys.next();

                if (selectedKey.isAcceptable()){
                    SocketChannel client = ((ServerSocketChannel) selectedKey.channel()).accept();
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ);
                    System.out.println("Connection accepted: " + client.getRemoteAddress());
                }else if (selectedKey.isReadable()){
                    SocketChannel client = (SocketChannel) selectedKey.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(256);
                    client.read(buffer);
                    String message = new String(buffer.array()).trim();

                    System.out.println("Message received! Message content: " + message);

                    String response = "Response";
                    byte[] r = response.getBytes();
                    ByteBuffer responseBuffer = ByteBuffer.wrap(r);
                    client.write(responseBuffer);

                    System.out.println("Confirmation sent! Connection will be closed!");
                    client.close();
                }
                selectedKeys.remove();
            }
        }
    }

    private static boolean isPortInUse(int port) {
        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /* should not be thrown */
                }
            }
        }

        return false;
    }
}
