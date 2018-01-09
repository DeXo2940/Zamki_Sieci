public class Team {
    private List <Player> squad;
    private List <Card> castle;
    //Integer points;
    private boolean decision;

    public Team() {
        this.squad = new ArrayList<>();
        this.castle = new ArrayList<>();
        this.decision = false;
    }

    public void joinTeam(Player player) {
        this.squad.add(player);
    }

    public void leaveTeam(Player player) {
        this.squad.remove(player);
    }

    public List<Player> getSquad() {
        return squad;
    }

    public void setSquad(List<Player> squad) {
        this.squad = squad;
    }

    public List<Card> getCastle() {
        return castle;
    }

    public void setCastle(List<Card> castle) {
        this.castle = castle;
    }

    public boolean isDecision() {
        return decision;
    }

    public void setDecision(boolean decision) {
        this.decision = decision;
    }
}