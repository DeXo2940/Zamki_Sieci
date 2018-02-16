import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable {


    private Socket socket;

    public Client() {

    }

    public void doIt(String server, int serverPort) throws Exception {

        this.socket = new Socket(InetAddress.getByName(server), serverPort);
        this.run();
    }

    public boolean isConnected() {
        if(socket.isConnected()) return true;
        else return false;
    }

    @Override
    public void run() {
        try {

            System.out.println("\r\nConnected to: " + this.socket.getInetAddress());
            String fromsrv;

            DataOutputStream outToServer = new DataOutputStream(this.socket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            outToServer.writeBytes("1234" + "\n");

            while ((fromsrv = inFromServer.readLine()) != null) {
                System.out.print(fromsrv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}