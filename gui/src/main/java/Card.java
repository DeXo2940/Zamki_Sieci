public class Card {
    private int sign;
    private boolean reversed;

    public Card (int sign) {
        this.sign = sign;
        this.reversed = false;
    }


    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public boolean isReversed() {
        return reversed;
    }

    public void setReversed(boolean reversed) {
        this.reversed = reversed;
    }
}