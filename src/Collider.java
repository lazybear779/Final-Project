import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Collider extends JPanel implements MouseListener {

    private ActionListener al;
    public HomeworkSupply assignedHomeworkSupply;

    public Collider() {
        setOpaque(false);
        addMouseListener(this);
        setSize(100, 120);
    }

    public void setHomeworkSupply(HomeworkSupply hs) {
        assignedHomeworkSupply = hs;
    }

    public void removeHomeworkSupply() {
        assignedHomeworkSupply.stop();
        assignedHomeworkSupply = null;
    }

    public boolean isInsideCollider(int tx) {
        return (tx > getLocation().x) && (tx < getLocation().x + 100);
    }

    public void setAction(ActionListener al) {
        this.al = al;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {
        if (al != null) {
            al.actionPerformed(new ActionEvent(this, ActionEvent.RESERVED_ID_MAX + 1, ""));
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
