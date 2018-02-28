import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

public class Team {
    private ArrayList<Integer> players;
    private StringProperty color = new SimpleStringProperty();
    private Integer number;
    private ArrayList<Card> castle;

    public Team (String color, Integer number) {
        players = new ArrayList<>();
        this.color.setValue(color);;
        this.number = number;
        castle = new ArrayList<>();
        //System.out.println("Castle size"+castle.size());
    }

    public Team (Integer player, String color, Integer number) {
        players = new ArrayList<>();
        this.color.setValue(color);
        this.number = number;
        castle = new ArrayList<>();
        players.add(player);
    }

    public void addToCastle(Card c) {
        this.castle.add(c);
    }

    public void addPlayer() {
        players.add(players.size());

    }

    public ArrayList<Integer> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Integer> players) {
        this.players = players;
    }

    public String getColor() {
        return color.get();
    }

    public void setColor(String color) {
        this.color.setValue(color);

    }

    public StringProperty getColorProperty() {
        return color;
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
