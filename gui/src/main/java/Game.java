import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Game {
    private Team mine, opposite;
    private IntegerProperty howManyCards = new SimpleIntegerProperty();
    private Integer winnerTeamId = 0;

    public Game() {
        howManyCards.setValue(0);
    }


    public int getHowManyCards() {
        return howManyCards.get();
    }

    public IntegerProperty getHowManyCardsProperty() {
        return howManyCards;
    }

    public void setHowManyCards(Integer howMany) {
        this.howManyCards.setValue(howMany);
    }

    public Team getMine() {
        return mine;
    }

    public void setMine(Team mine) {
        this.mine = mine;
    }

    public Team getOpposite() {
        return opposite;
    }

    public void setOpposite(Team opposite) {
        this.opposite = opposite;
    }

    public Integer getWinnerTeamId() {
        return winnerTeamId;
    }

    public void setWinnerTeamId(Integer winnerTeamId) {
        this.winnerTeamId = winnerTeamId;
    }
}
