import javax.swing.*;
import java.awt.Image;
import java.awt.event.ActionEvent;

public class GameWindow extends JFrame {

    enum HomeworkSupplyType {
        None,
        Laptop,
        PencilShooter,
        FreezePencilShooter
    }

    public GameWindow() {
        setSize(1012, 785);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel time = new JLabel("TIME");
        time.setLocation(37, 80);
        time.setSize(60, 20);

        GamePanel gp = new GamePanel(time);
        gp.setLocation(0, 0);
        getLayeredPane().add(gp, Integer.valueOf(0));

        PlantCard laptop = new PlantCard(loadImage("images/cards/card_laptop.png"));
        laptop.setLocation(110, 8);
        laptop.setAction((ActionEvent e) -> {
            gp.setActiveHomeworkSupplyBrush(HomeworkSupplyType.Laptop);
        });
        getLayeredPane().add(laptop, Integer.valueOf(3));

        PlantCard pencilShooter = new PlantCard(loadImage("images/cards/card_pencilShooter.png"));
        pencilShooter.setLocation(175, 8);
        pencilShooter.setAction((ActionEvent e) -> {
            gp.setActiveHomeworkSupplyBrush(HomeworkSupplyType.PencilShooter);
        });
        getLayeredPane().add(pencilShooter, Integer.valueOf(3));

        PlantCard freezePencilShooter = new PlantCard(loadImage("images/cards/card_freezePencilShooter.png"));
        freezePencilShooter.setLocation(240, 8);
        freezePencilShooter.setAction((ActionEvent e) -> {
            gp.setActiveHomeworkSupplyBrush(HomeworkSupplyType.FreezePencilShooter);
        });
        getLayeredPane().add(freezePencilShooter, Integer.valueOf(3));

        getLayeredPane().add(time, Integer.valueOf(2));
        setResizable(false);
        setVisible(true);
    }

    private Image loadImage(String path) {
        System.out.println("Loading image: " + path);
        ImageIcon icon = new ImageIcon(Thread.currentThread().getContextClassLoader().getResource(path));
        if (icon.getImageLoadStatus() == java.awt.MediaTracker.ERRORED) {
            System.err.println("Error loading image: " + path);
            throw new RuntimeException("Resource not found: " + path);
        }
        return icon.getImage();
    }

    public GameWindow(boolean b) {
        Menu menu = new Menu();
        menu.setLocation(0, 0);
        setSize(1012, 785);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getLayeredPane().add(menu, Integer.valueOf(0));
        menu.repaint();
        setResizable(false);
        setVisible(true);
    }

    static GameWindow gw;

    public static void begin() {
        gw.dispose();
        gw = new GameWindow();
    }

    public static void main(String[] args) {
        gw = new GameWindow(true);
    }
}
