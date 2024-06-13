import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Laptop extends HomeworkSupply {

    private Timer timeProduceTimer;

    public Laptop(GamePanel parent, int x, int y) {
        super(parent, x, y);
        timeProduceTimer = new Timer(12000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Time t = new Time(getGp(), 60 + x * 100, 110 + y * 120, 130 + y * 120);
                getGp().getActiveTimes().add(t);
                getGp().add(t, Integer.valueOf(1));
            }
        });
        timeProduceTimer.start();
    }

    @Override
    public void stop() {
        timeProduceTimer.stop();
    }
}
