import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Alert;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class Client implements Runnable {


    private Socket socket;
    private Integer teamNumber = 0;
    private Integer move;
    private boolean ifMyMove = false;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    private Game game;
    private boolean ready = false;

    public void alert(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
        public Client() {
        this.game = new Game();
    }

    public boolean doIt(String server, int serverPort) {

        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(server, serverPort), 5000);
            return true;
        } catch (IOException e) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    alert("Nie można się połączyć!");
                }
            });
            return false;
            }
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

    public char ifImNew(String communicate) {
        if (communicate.charAt(0) == 't') {
            this.setTeamNumber(Character.getNumericValue(communicate.charAt(1)));
            return communicate.charAt(2);
        } else return 'x';
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

            boolean team1Castle = false, team2Castle = false;

            while (!(proper & team1Castle)) {
                char sign = 'c', signCastle = 'x';
                int howManyCards = 0, teamNr = 0;
                while (signCastle != sign) {
                    fromsrv = inFromServer.readLine();
                    proper = checkCommunicate(fromsrv);
                    if (proper) {
                        signCastle = fromsrv.charAt(0);
                        howManyCards = Character.getNumericValue(fromsrv.charAt(3));
                        teamNr = Character.getNumericValue(fromsrv.charAt(1));
                    }
                }
                //System.out.println("Jestem na zewnatrz");
                Team team = null;
                if (game.getMine().getNumber() == teamNr) {
                    team = game.getMine();
                } else team = game.getOpposite();

                //System.out.println("Zameeeek: " + team.getCastle().size());
                for (int i = 1; i <= howManyCards; i++) {
                    fromsrv = inFromServer.readLine();
                    if (fromsrv.charAt(2) == '0') {
                        //System.out.println("Rozmiar: " + team.getCastle().size());
                        team.addToCastle(new Card(Character.getNumericValue(fromsrv.charAt(3))));
                    } else {
                        String nr = "" + fromsrv.charAt(2) + fromsrv.charAt(3);
                        team.addToCastle(new Card(Integer.parseInt(nr)));
                    }
                }

                if (team.getCastle().size() == howManyCards) {
                    team1Castle = true;
                    //System.out.println("Jestem gotowy w chuj");

                } else {
                    //obsluzyc wyjatek - powtorzyc komunikat???
                }
            }

            //dziaua////////////////////////////////////////////////////////////////////////////////////////////////
            while (!(proper & team2Castle)) {
                char sign = 'c', signCastle = 'x';
                int howManyCards = 0, teamNr = 0;
                while (signCastle != sign) {
                    fromsrv = inFromServer.readLine();
                    proper = checkCommunicate(fromsrv);
                    if (proper) {
                        signCastle = fromsrv.charAt(0);
                        howManyCards = Character.getNumericValue(fromsrv.charAt(3));
                        teamNr = Character.getNumericValue(fromsrv.charAt(1));
                    }
                }
                //System.out.println("Jestem na zewnatrz");
                Team team = null;
                if (game.getMine().getNumber() == teamNr) {
                    team = game.getMine();
                } else team = game.getOpposite();

                // System.out.println("Zameeeek: " + team.getCastle().size());
                for (int i = 1; i <= howManyCards; i++) {
                    fromsrv = inFromServer.readLine();
                    if (fromsrv.charAt(2) == '0') {
                        //System.out.println("Rozmiar: " + team.getCastle().size());
                        team.addToCastle(new Card(Character.getNumericValue(fromsrv.charAt(3))));
                    } else {
                        String nr = "" + fromsrv.charAt(2) + fromsrv.charAt(3);
                        team.addToCastle(new Card(Integer.parseInt(nr)));
                    }
                }

                if (team.getCastle().size() == howManyCards) {
                    team2Castle = true;
                    //System.out.println("Jestem gotowy w chuj");

                } else {
                    //obsluzyc wyjatek - powtorzyc komunikat???
                }
            }

            while (!(fromsrv = inFromServer.readLine()).equals("x000x")) {
                if (checkCommunicate(fromsrv) & fromsrv.charAt(0) == 'j') {
                    System.out.println("K: " + fromsrv);
                    String nr = "" + fromsrv.charAt(1) + fromsrv.charAt(2);
                    System.out.println("Wartosc: " + nr);
                    game.setHowManyCards(Integer.parseInt(nr));
                    System.out.println(game.getHowManyCards());
                    //System.out.println("Koniec, jestem mega gotowy");
                }
            }

            ///jestem gotowyyyyyyy

            //////////////////////koniec inicjalizacji


            while (true) {
                while ((fromsrv = inFromServer.readLine()) != null) {
                    System.out.println(fromsrv);
                    if (checkCommunicate(fromsrv)) {
                        switch (fromsrv.charAt(0)) {
                            case 's':
                                addPlayer(fromsrv);
                                break;
                            case 'R':
                                setReady(true);
                                System.out.println("Jestem gotowy");
                                break;
                            case 'j':
                                howManyCards(fromsrv);
                                break;
                            case 'W':
                                whoWin(fromsrv);
                                break;
                            case 'e':
                                handleError();
                                break;
                            case 'd':
                                move();
                                break;
                            case 'w':
                                waitForIt();
                                break;
                            case 'm':
                                break;
                            case 'y':
                                setIfMyMove(true);
                                break;
                            default: handleError();
                        }
                    }
                    ///aktualizuje graczy w teamie

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chooseCard(Integer id) {
        System.out.println("Id wybranej karty: " + id);
        String message;
        if (id < 10) {
            message = "l" + String.valueOf(this.getTeamNumber()) + "0" + String.valueOf(id) + "l";
        } else message = "l" + String.valueOf(this.getTeamNumber()) + String.valueOf(id) + "l";

        System.out.println(message);
        try {
            DataOutputStream outToServer = new DataOutputStream(this.socket.getOutputStream());
            outToServer.writeBytes(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void waitForIt() {
    }

    public void move() {

    }

    public void handleError() {
        //@TODO
    }

    public void whoWin(String fromsrv) {
        Integer id = Character.getNumericValue(fromsrv.charAt(3));
        if (id == 0) id = getTeamNumber();
        game.setWinnerTeamId(id);
    }

    public void howManyCards(String fromsrv) {
        String nr = "" + fromsrv.charAt(1) + fromsrv.charAt(2);
        game.setHowManyCards(Integer.parseInt(nr));
    }

    public void addPlayer(String fromsrv) {
        Team team = chooseTeam(Character.getNumericValue(fromsrv.charAt(1)));
        System.out.println("Rozmiar teamu: " + team.getPlayers().size());
        while (team.getPlayers().size() < Character.getNumericValue(fromsrv.charAt(3))) {
            team.addPlayer();
            System.out.println("Dodałem gracza!");
        }
    }

    public Team chooseTeam(Integer number) {
        if (this.getTeamNumber() == number) return this.game.getMine();
        else return this.game.getOpposite();
    }

    public Integer getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(Integer teamNumber) {
        this.teamNumber = teamNumber;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean ifMyMove() {
        return ifMyMove;
    }

    public void setIfMyMove(boolean ifMyMove) {
        this.ifMyMove = ifMyMove;
    }
}