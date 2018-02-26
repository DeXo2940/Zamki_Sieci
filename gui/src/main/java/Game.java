import java.util.ArrayList;

public class Game {
    private Team mine, opposite;
    private Integer howManyCards;

    public Game () {
        howManyCards = 0;
    }



    public Integer getHowManyCards() {
        return howManyCards;
    }

    public void setHowManyCards(Integer howManyCards) {
        this.howManyCards = howManyCards;
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
}
