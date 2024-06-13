import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JLayeredPane implements MouseMotionListener {

    private Image bgImage;
    private Image pencilShooterImage;
    private Image freezePencilShooterImage;
    private Image laptopImage;
    private Image pencilImage;
    private Image freezePencilImage;

    private Image normalDeadlineImage;
    private Image majorDeadlineImage;
    private Collider[] colliders;

    private ArrayList<ArrayList<Deadline>> laneDeadlines;
    private ArrayList<ArrayList<Pencil>> lanePencils;
    private ArrayList<Time> activeTimes;

    private Timer redrawTimer;
    private Timer advancerTimer;
    private Timer timeProducer;
    private Timer deadlineProducer;
    private JLabel timeScoreboard;
    private JLabel pointsLabel;
    private JLabel mentalHealthLabel;

    private GameWindow.HomeworkSupplyType activeHomeworkSupplyBrush = GameWindow.HomeworkSupplyType.None;

    private int mouseX, mouseY;

    private int timeScore;
    private int points;
    private int mentalHealth = 100; // Initial mental health
    private boolean deadlinesAdded = false;

    private int normalDeadlineCount = 0;

    public int getTimeScore() {
        return timeScore;
    }

    public void setTimeScore(int timeScore) {
        this.timeScore = timeScore;
        timeScoreboard.setText(String.valueOf(timeScore));
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
        pointsLabel.setText("Points: " + points);
        System.out.println("Points: " + points); // Debugging output
    }

    public int getMentalHealth() {
        return mentalHealth;
    }

    public void setMentalHealth(int mentalHealth) {
        this.mentalHealth = mentalHealth;
        mentalHealthLabel.setText("Mental Health: " + mentalHealth);
        System.out.println("Mental Health: " + mentalHealth); // Debugging output
        if (mentalHealth < 0) {
            JOptionPane.showMessageDialog(this, "You have lost due to poor mental health! Game Over.");
            System.exit(0); // End the game
        }
    }

    public GamePanel(JLabel timeScoreboard) {
        setSize(1000, 752);
        setLayout(null);
        addMouseMotionListener(this);
        this.timeScoreboard = timeScoreboard;
        setTimeScore(150);

        // Point tracker label
        pointsLabel = new JLabel("Points: 0");
        pointsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        pointsLabel.setForeground(Color.WHITE);
        pointsLabel.setBounds(850, 10, 150, 20);
        add(pointsLabel);

        // Mental health label
        mentalHealthLabel = new JLabel("Mental Health: 100");
        mentalHealthLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mentalHealthLabel.setForeground(Color.WHITE);
        mentalHealthLabel.setBounds(850, 30, 150, 20);
        add(mentalHealthLabel);

        bgImage = loadImage("images/mainBG.png");
        pencilShooterImage = loadImage("images/plants/pencilShooter.png");
        freezePencilShooterImage = loadImage("images/plants/freezePencilShooter.png");
        laptopImage = loadImage("images/plants/laptop.png");
        pencilImage = loadImage("images/pencil.png");
        freezePencilImage = loadImage("images/freezePencil.png");
        normalDeadlineImage = loadImage("images/zombies/normalDeadline.png");
        majorDeadlineImage = loadImage("images/zombies/majorDeadline.png");

        laneDeadlines = new ArrayList<>();
        laneDeadlines.add(new ArrayList<>()); //line 1
        laneDeadlines.add(new ArrayList<>()); //line 2
        laneDeadlines.add(new ArrayList<>()); //line 3
        laneDeadlines.add(new ArrayList<>()); //line 4
        laneDeadlines.add(new ArrayList<>()); //line 5

        lanePencils = new ArrayList<>();
        lanePencils.add(new ArrayList<>()); //line 1
        lanePencils.add(new ArrayList<>()); //line 2
        lanePencils.add(new ArrayList<>()); //line 3
        lanePencils.add(new ArrayList<>()); //line 4
        lanePencils.add(new ArrayList<>()); //line 5

        colliders = new Collider[45];
        for (int i = 0; i < 45; i++) {
            Collider a = new Collider();
            a.setLocation(44 + (i % 9) * 100, 109 + (i / 9) * 120);
            a.setAction(new HomeworkSupplyActionListener((i % 9), (i / 9)));
            colliders[i] = a;
            add(a, Integer.valueOf(0));
        }

        activeTimes = new ArrayList<>();

        redrawTimer = new Timer(25, (ActionEvent e) -> repaint());
        redrawTimer.start();

        advancerTimer = new Timer(60, (ActionEvent e) -> advance());
        advancerTimer.start();

        timeProducer = new Timer(5000, (ActionEvent e) -> {
            Random rnd = new Random();
            Time t = new Time(this, rnd.nextInt(800) + 100, 0, rnd.nextInt(300) + 200);
            activeTimes.add(t);
            add(t, Integer.valueOf(1));
        });
        timeProducer.start();

        deadlineProducer = new Timer(7000, (ActionEvent e) -> summonDeadlines(1, -1));
        deadlineProducer.start();
    }

    private void summonDeadlines(int count, int lastLane) {
        if (count <= 0) return;

        Random rnd = new Random();
        int lane;
        do {
            lane = rnd.nextInt(5);
        } while (lane == lastLane);

        Deadline d;
        if (normalDeadlineCount > 0 && normalDeadlineCount % 5 == 0) {
            d = new MajorDeadline(this, lane);
            normalDeadlineCount = 0;
            System.out.println("Created a MajorDeadline");
        } else {
            d = new NormalDeadline(this, lane);
            normalDeadlineCount++;
        }
        laneDeadlines.get(lane).add(d);
        deadlinesAdded = true;

        summonDeadlines(count - 1, lane);
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

    private void advance() {
        boolean allDeadlinesCleared = true;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < laneDeadlines.get(i).size(); j++) {
                Deadline d = laneDeadlines.get(i).get(j);
                d.advance();
                if (d.getPosX() < 0) {
                    JOptionPane.showMessageDialog(this, "DEADLINES ATE YOUR HOMEWORK! Game Over.");
                    System.exit(0); // End the game
                }
                if (d.getHealth() > 0) {
                    allDeadlinesCleared = false;
                }
            }

            for (int k = 0; k < lanePencils.get(i).size(); k++) {
                Pencil p = lanePencils.get(i).get(k);
                p.advance();
            }
        }

        for (int l = 0; l < activeTimes.size(); l++) {
            activeTimes.get(l).advance();
        }

        if (deadlinesAdded && allDeadlinesCleared) { // Check the flag before declaring a win
            JOptionPane.showMessageDialog(this, "Congratulations! You've cleared all deadlines!");
            System.exit(0); // End the game
        }

        if (points >= 300) {
            JOptionPane.showMessageDialog(this, "Congratulations! You've reached 300 points!");
            System.exit(0); // End the game
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImage, 0, 0, null);

        for (int i = 0; i < 45; i++) {
            Collider c = colliders[i];
            if (c.assignedHomeworkSupply != null) {
                HomeworkSupply hs = c.assignedHomeworkSupply;
                if (hs instanceof PencilShooter) {
                    g.drawImage(pencilShooterImage, 60 + (i % 9) * 100, 129 + (i / 9) * 120, null);
                }
                if (hs instanceof FreezePencilShooter) {
                    g.drawImage(freezePencilShooterImage, 60 + (i % 9) * 100, 129 + (i / 9) * 120, null);
                }
                if (hs instanceof Laptop) {
                    g.drawImage(laptopImage, 60 + (i % 9) * 100, 129 + (i / 9) * 120, null);
                }
            }
        }

        for (int i = 0; i < 5; i++) {
            for (Deadline d : laneDeadlines.get(i)) {
                if (d instanceof NormalDeadline) {
                    g.drawImage(normalDeadlineImage, d.getPosX(), 109 + (i * 120), null);
                } else if (d instanceof MajorDeadline) {
                    g.drawImage(majorDeadlineImage, d.getPosX(), 109 + (i * 120), null);
                    System.out.println("Drawing MajorDeadline at lane " + i + " position " + d.getPosX());
                }
            }

            for (int j = 0; j < lanePencils.get(i).size(); j++) {
                Pencil p = lanePencils.get(i).get(j);
                if (p instanceof FreezePencil) {
                    g.drawImage(freezePencilImage, p.getPosX(), 130 + (i * 120), null);
                } else {
                    g.drawImage(pencilImage, p.getPosX(), 130 + (i * 120), null);
                }
            }
        }
    }

    private class HomeworkSupplyActionListener implements ActionListener {

        int x, y;

        public HomeworkSupplyActionListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (activeHomeworkSupplyBrush == GameWindow.HomeworkSupplyType.Laptop) {
                if (getTimeScore() >= 50) {
                    colliders[x + y * 9].setHomeworkSupply(new Laptop(GamePanel.this, x, y));
                    setTimeScore(getTimeScore() - 50);
                    setMentalHealth(getMentalHealth() + 10);  // Gain mental health
                }
            }
            if (activeHomeworkSupplyBrush == GameWindow.HomeworkSupplyType.PencilShooter) {
                if (getTimeScore() >= 100) {
                    colliders[x + y * 9].setHomeworkSupply(new PencilShooter(GamePanel.this, x, y));
                    setTimeScore(getTimeScore() - 100);
                    setMentalHealth(getMentalHealth() - 10);  // Lose mental health
                }
            }

            if (activeHomeworkSupplyBrush == GameWindow.HomeworkSupplyType.FreezePencilShooter) {
                if (getTimeScore() >= 150) {  // Updated price for FreezePencilShooter
                    colliders[x + y * 9].setHomeworkSupply(new FreezePencilShooter(GamePanel.this, x, y));
                    setTimeScore(getTimeScore() - 150);  // Deduct the updated price
                    setMentalHealth(getMentalHealth() - 20);  // Lose mental health
                }
            }
            activeHomeworkSupplyBrush = GameWindow.HomeworkSupplyType.None;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    static int progress = 0;

    public static void setProgress(int num) {
        progress = progress + num;
        System.out.println(progress);
        if (progress >= 150) {
            if ("1".equals(LevelData.LEVEL_NUMBER)) {
                JOptionPane.showMessageDialog(null, "LEVEL COMPLETED!" + '\n' + "Starting next LEVEL");
                GameWindow.gw.dispose();
                LevelData.write("2");
                GameWindow.gw = new GameWindow();
            } else {
                JOptionPane.showMessageDialog(null, "LEVEL COMPLETED!" + '\n' + "More levels will come soon!" + '\n' + "Resetting data");
                LevelData.write("1");
                System.exit(0);
            }
            progress = 0;
        }
    }

    public GameWindow.HomeworkSupplyType getActiveHomeworkSupplyBrush() {
        return activeHomeworkSupplyBrush;
    }

    public void setActiveHomeworkSupplyBrush(GameWindow.HomeworkSupplyType activeHomeworkSupplyBrush) {
        this.activeHomeworkSupplyBrush = activeHomeworkSupplyBrush;
    }

    public ArrayList<ArrayList<Deadline>> getLaneDeadlines() {
        return laneDeadlines;
    }

    public void setLaneDeadlines(ArrayList<ArrayList<Deadline>> laneDeadlines) {
        this.laneDeadlines = laneDeadlines;
    }

    public ArrayList<ArrayList<Pencil>> getLanePencils() {
        return lanePencils;
    }

    public void setLanePencils(ArrayList<ArrayList<Pencil>> lanePencils) {
        this.lanePencils = lanePencils;
    }

    public ArrayList<Time> getActiveTimes() {
        return activeTimes;
    }

    public void setActiveTimes(ArrayList<Time> activeTimes) {
        this.activeTimes = activeTimes;
    }

    public Collider[] getColliders() {
        return colliders;
    }

    public void setColliders(Collider[] colliders) {
        this.colliders = colliders;
    }
}
