public abstract class Deadline {

    private int health;
    private int posX;
    private int myLane;
    private GamePanel parent;

    public Deadline(GamePanel parent, int lane) {
        this.parent = parent;
        this.myLane = lane;
        this.posX = 1000; // Example starting position
    }

    public void advance() {
        posX -= 1; // Example movement speed
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getPosX() {
        return posX;
    }

    public int getMyLane() {
        return myLane;
    }

    public GamePanel getParent() {
        return parent;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health <= 0) {
            parent.setPoints(parent.getPoints() + (this instanceof MajorDeadline ? 20 : 5));
            parent.getLaneDeadlines().get(myLane).remove(this);
        }
    }

    public void slow() {
        // Implement slow logic if necessary
    }
}
