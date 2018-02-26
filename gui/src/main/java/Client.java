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
        if (socket.isConnected()) return true;
        else return false;
    }


    public boolean checkCommunicate(String com) {
        if (com.length() != 5) return false;
        if (com.charAt(0) == com.charAt(com.length() - 1)) return true;
        return false;
    }

    public void getMessage() {

    }

    public char ifImNew(String communicate) {
        if (communicate.charAt(0) == 't') {
            this.setTeamNumber(Character.getNumericValue(communicate.charAt(1)));
            return communicate.charAt(2);
        } else return 'x';
    }

    public boolean ifWaiting(String communicate) {
        if (communicate.charAt(0) == 'w') {
            //wait
            return true;
        } else {
            if (communicate.charAt(0) == 'm') return false;
        }
        return true;
    }

    public boolean ifMyMove(String communicate) {
        if (communicate.charAt(0) == 'd') return true;
        else return false;
    }

    public boolean ifEnd(String communicate) {
        if (communicate.charAt(0) == 'x') return true;
        else return false;
    }

    public int whoWin(String communicate) {
        if (communicate.charAt(0) == 'u') {
            if ((int) communicate.charAt(4) == this.getTeamNumber()) {
                //yes you win
                return this.getTeamNumber();
            } else return (int) communicate.charAt(4);
        } else return -1;
    }


    public String color(char color) {
        switch (color) {
            case 'o':
                return "orange";
            case 'r':
                return "red";
            case 'g':
                return "green";
            case 'b':
                return "blue";
            case 'y':
                return "yellow";
            case 'w':
                return "white";
            case 'p':
                return "purple";
            default:
                return "transparent";
        }
    }

    public boolean howManyPlayers(String communicate) {
        if (communicate.charAt(0) == 's') {
            if (communicate.charAt(1) == this.getTeamNumber()) {
                for (int i = 2; i <= communicate.charAt(3); i++) game.getMine().getPlayers().add(i);
                return true;
            } else if (communicate.charAt(1) == 2) {
                for (int i = 1; i < communicate.charAt(3); i++) game.getOpposite().getPlayers().add(i);
                return true;
            } else return false;

        } else return false;
    }

    @Override
    public void run() {
        try {

            boolean connected = false, otherTeams = false, ready = false;
            System.out.println("\r\nConnected to: " + this.socket.getInetAddress());
            String fromsrv;

            DataOutputStream outToServer = new DataOutputStream(this.socket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

            boolean proper = false;

            //połącz się, uzupełnij kolor i nr swojego teamu
            while (!(connected & proper)) {
                fromsrv = inFromServer.readLine();
                System.out.println(fromsrv);
                proper = checkCommunicate(fromsrv);
                if (ifImNew(fromsrv) != 'x') {
                    connected = true;
                    Team mine = new Team(1, color(fromsrv.charAt(2)), getTeamNumber());
                    game.setMine(mine);
                } else {
                    //obsluz wyjatek
                    //@TODO
                }
            }
////////////////////////////////////////////dziala
            //dostań info ile teamów
            proper = false;
            boolean teams = false;
            while (!(proper & teams)) {
                Team opposite = null;
                fromsrv = inFromServer.readLine();
                System.out.println(fromsrv);
                proper = checkCommunicate(fromsrv);
                if (fromsrv.charAt(0) == 'n') {
                    if (getTeamNumber() == 1) {
                        opposite = new Team(color(fromsrv.charAt(3)), 2);
                    } else
                        opposite = new Team(color(fromsrv.charAt(3)), 1);
                    game.setOpposite(opposite);
                    teams = true;
                }
            }

            /////////////////////////////////////////////////działa

            System.out.println(inFromServer.readLine());


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