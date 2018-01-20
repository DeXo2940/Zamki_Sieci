import java.util.Set;

public class Game {
    Set<Card> freeCards;

    public Game(Set<Card> freeCards) {
        this.freeCards = freeCards;
    }

    public Game() {

    }

    public Set<Card> getFreeCards() {
        return freeCards;
    }

    public void setFreeCards(Set<Card> freeCards) {
        this.freeCards = freeCards;
    }
}