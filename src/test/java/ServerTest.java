import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTest {
//    public int[] udpPorts = {4321, 3218, 501};
//    public int[] tcpPorts = {3218, 500, 4321, 4455};

    public static void main(String[] args) {
        ServerTest server = new ServerTest();
        try {
            server.message();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void message()throws Exception{
        ServerSocket serverSocket = new ServerSocket(4321);
        Socket socket = serverSocket.accept();
        InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String message = bufferedReader.readLine();
        System.out.println(message);

        if (message != null){
            PrintStream printStream = new PrintStream(socket.getOutputStream());
            printStream.println("Message received!");
        }
    }

}
