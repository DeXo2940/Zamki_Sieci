import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client implements Runnable {


    private Socket socket;

    public Client() {

    }
    public Client(InetAddress serverAddress, int serverPort) throws Exception {
        this.socket = new Socket(serverAddress, serverPort);
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client();


        client.run();

    }

    @Override
    public void run() {
        try {
            Client client = new Client(
                    InetAddress.getByName("127.0.0.1"),
                    1234);
            System.out.println("\r\nConnected to: " + client.socket.getInetAddress());
            String fromsrv;

            DataOutputStream outToServer = new DataOutputStream(client.socket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(client.socket.getInputStream()));
            outToServer.writeBytes("1234" + "\n");

            while ((fromsrv = inFromServer.readLine()) != null) {
                System.out.print(fromsrv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}