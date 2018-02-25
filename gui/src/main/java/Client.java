import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable {


    private Socket socket;
    private Integer teamNumber = 0;
    private Integer move;
    private Game game;

    public Client() {
        this.game = new Game();
    }

    public void doIt(String server, int serverPort) throws Exception {
        this.socket = new Socket(InetAddress.getByName(server), serverPort);
    }

    public boolean isConnected() {
        if(socket.isConnected()) return true;
        else return false;
    }


    public boolean checkCommunicate(String com) {
        if (com.length() != 5) return false;
        if (com.charAt(0) == com.charAt(com.length()-1)) return true;
        return false;
    }

    public void getMessage () {

    }

    public char ifImNew(String communicate) {
        if (communicate.charAt(0) == 't') {
            this.setTeamNumber((int) communicate.charAt(1));
            return communicate.charAt(2);
        }
        else return 'x';
    }

    public boolean ifWaiting(String communicate) {
        if(communicate.charAt(0) == 'w') {
            //wait
            return true;
        }
        else {
            if (communicate.charAt(0) == 'm') return false;
        }
        return true;
    }

    public boolean ifMyMove(String communicate) {
        if (communicate.charAt(0) == 'd') return true;
        else return false;
    }

    public boolean ifEnd (String communicate) {
        if (communicate.charAt(0) == 'x') return true;
        else return false;
    }

    public int whoWin(String communicate) {
        if (communicate.charAt(0) == 'u') {
            if ((int)communicate.charAt(4) == this.getTeamNumber()) {
                //yes you win
                return this.getTeamNumber();
            }
            else return (int)communicate.charAt(4);
        }
        else return -1;
    }

    @Override
    public void run() {
        try {

            System.out.println("\r\nConnected to: " + this.socket.getInetAddress());
            String fromsrv;

            DataOutputStream outToServer = new DataOutputStream(this.socket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));


            while ((fromsrv = inFromServer.readLine()) != null) {
                System.out.println(fromsrv);
                checkCommunicate(fromsrv);
                //System.out.println(ifImNew(fromsrv));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Integer getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(Integer teamNumber) {
        this.teamNumber = teamNumber;
    }
}