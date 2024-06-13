import java.awt.*;

public class Pencil {

    private int posX;
    protected int damage; // Change this to protected
    protected GamePanel gp;
    private int myLane;

    public Pencil(GamePanel parent, int lane, int startX) {
        this.gp = parent;
        this.myLane = lane;
        this.posX = startX;
        this.damage = 10; // Example damage value
    }

    public void advance() {
        Rectangle pRect = new Rectangle(posX, 130 + myLane * 120, 28, 28);
        for (int i = 0; i < gp.getLaneDeadlines().get(myLane).size(); i++) {
            Deadline d = gp.getLaneDeadlines().get(myLane).get(i);
            Rectangle dRect = new Rectangle(d.getPosX(), 109 + myLane * 120, 400, 120);
            if (pRect.intersects(dRect)) {
                d.takeDamage(damage);
                gp.getLanePencils().get(myLane).remove(this);
                break;
            }
        }
        posX += 15;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getMyLane() {
        return myLane;
    }

    public void setMyLane(int myLane) {
        this.myLane = myLane;
    }
}
