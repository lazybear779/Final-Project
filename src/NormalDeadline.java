public class NormalDeadline extends Deadline {

    public NormalDeadline(GamePanel parent, int lane) {
        super(parent, lane);
        setHealth(100); // Example health value
    }

    @Override
    public void advance() {
        super.advance();
        if (getHealth() <= 0) {
            getParent().setPoints(getParent().getPoints() + 5); // Award 5 points for killing a NormalDeadline
            getParent().getLaneDeadlines().get(getMyLane()).remove(this);
        }
    }
}
