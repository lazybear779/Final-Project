public class MajorDeadline extends Deadline {

    public MajorDeadline(GamePanel parent, int lane) {
        super(parent, lane);
        setHealth(300); // Example health value
    }

    @Override
    public void advance() {
        super.advance();
        if (getHealth() <= 0) {
            getParent().setPoints(getParent().getPoints() + 20); // Award 20 points for killing a MajorDeadline
            getParent().getLaneDeadlines().get(getMyLane()).remove(this);
        }
    }
}
