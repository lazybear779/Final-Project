import java.awt.Rectangle;

public class FreezePencil extends Pencil {

    public FreezePencil(GamePanel parent, int lane, int startX) {
        super(parent, lane, startX);
    }

    @Override
    public void advance() {
        Rectangle pRect = new Rectangle(getPosX(), 130 + getMyLane() * 120, 28, 28);
        for (int i = 0; i < gp.getLaneDeadlines().get(getMyLane()).size(); i++) {
            Deadline d = gp.getLaneDeadlines().get(getMyLane()).get(i);
            Rectangle dRect = new Rectangle(d.getPosX(), 109 + getMyLane() * 120, 400, 120);
            if (pRect.intersects(dRect)) {
                d.takeDamage(damage);
                d.slow();
                gp.getLanePencils().get(getMyLane()).remove(this);
                break;
            }
        }
        setPosX(getPosX() + 15);
    }
}
