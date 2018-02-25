import java.util.ArrayList;

public class Team {
    private ArrayList<Integer> players;
    private String color;
    private Integer number;
    private ArrayList<Card> castle;

    public ArrayList<Integer> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Integer> players) {
        this.players = players;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public ArrayList<Card> getCastle() {
        return castle;
    }

    public void setCastle(ArrayList<Card> castle) {
        this.castle = castle;
    }
}
