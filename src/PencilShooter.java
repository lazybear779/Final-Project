import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PencilShooter extends HomeworkSupply {

    private Timer shootTimer;

    public PencilShooter(GamePanel parent, int x, int y) {
        super(parent, x, y);
        shootTimer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getGp().getLaneDeadlines().get(y).size() > 0) {
                    getGp().getLanePencils().get(y).add(new Pencil(getGp(), y, 103 + getX() * 100));
                }
            }
        });
        shootTimer.start();
    }

    @Override
    public void stop() {
        shootTimer.stop();
    }
}
