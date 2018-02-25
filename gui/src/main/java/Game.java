import java.util.ArrayList;

public class Game {
    private ArrayList<Team> teams;
    private Integer howManyCards;

    public Game () {
        teams = new ArrayList<>();
        howManyCards = 0;
    }


    public ArrayList<Team> getTeams() {
        return teams;
    }

    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }

    public Integer getHowManyCards() {
        return howManyCards;
    }

    public void setHowManyCards(Integer howManyCards) {
        this.howManyCards = howManyCards;
    }
}
