package org.cis1200.SpaceInvaders;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.io.*;
import java.util.List;

/**
 * GameCourt
 * <p>
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 */

// File IO other concept type
//store info into a file to undo
//(which bugs were there before, the position of the ammo and position of swap)

public class GameCourt extends JPanel {

    // the state of the game logic
    private Map<Integer, Bug> bugs;
    private Swap swap;

    private boolean playing = false; // whether the game is running
    private final JLabel status; // Current status text, i.e. "Running..."
    private SwapAmmo swammo = new SwapAmmo(COURT_WIDTH, COURT_HEIGHT, Color.BLUE);
    private BugAmmo bammo = new BugAmmo(COURT_WIDTH, COURT_HEIGHT, Color.RED);
    private boolean clicked;
    private boolean won;
    // Game constants
    public static final int COURT_WIDTH = 1000;
    public static final int COURT_HEIGHT = 600;
    public static final int SWAP_VELOCITY = 10;

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 35;
    public static final int BAMMO_INTERVAL = 3500;
    public static boolean lost;
    private Random rand = new Random();
    private File file;
    private FileReader fr;
    private BufferedReader br;
    private FileWriter fw;
    private BufferedWriter bw;
    private ArrayList<Integer> bugList = new ArrayList<>();
    private ArrayList<Integer> undoBugList = new ArrayList<>();
    private File otherFile;
    private FileReader otherFr;
    private BufferedReader otherBr;
    private FileWriter otherFw;
    private BufferedWriter otherBw;
    private ArrayList<Integer> otherList = new ArrayList<>();
    //keys

    public GameCourt(JLabel status) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // The timer is an object which triggers an action periodically with the
        // given INTERVAL. We register an ActionListener with this timer, whose
        // actionPerformed() method is called each time the timer triggers. We
        // define a helper method called tick() that actually does everything
        // that should be done in a single time step.
        Timer timer = new Timer(INTERVAL, e -> tick());
        timer.start(); // MAKE SURE TO START THE TIMER!
        //make a new timer for the bug ammo
        Timer bammoTimer = new Timer(BAMMO_INTERVAL, e -> btick());
        bammoTimer.start();
        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // This key listener allows the square to move as long as an arrow key
        // is pressed, by changing the square's velocity accordingly. (The tick
        // method below actually moves the square.)
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    swap.setVx(-SWAP_VELOCITY);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    swap.setVx(SWAP_VELOCITY);
                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    clicked = true;
                }
            }

            public void keyReleased(KeyEvent e) {
                swap.setVx(0);
                swap.setVy(0);
            }
        });

        this.status = status;
    }

    public void btick() {
        if (playing) {
            int randNum = rand.nextInt(24);
            while (bugs.get(randNum).getIsHit()) {
                randNum = rand.nextInt(24);
            }
            bammo.setPx(bugs.get(randNum).getPx() + 30);
            bammo.setPy(bugs.get(randNum).getPy() + 60);
            repaint();
        }
    }

    public boolean isClicked() {
        return clicked;
    }

    public static boolean isLost() {
        return lost;
    }

    public boolean isWon() {
        return won;
    }

    public SwapAmmo getSwammo() {
        return swammo;
    }

    public BugAmmo getBammo() {
        return bammo;
    }

    public Swap getSwap() {
        return swap;
    }

    public void setClickedToTrue() {
        clicked = true;
    }

    /*public ArrayList<Integer> getBugs() {

    }*/

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        bugs = new TreeMap<>();
        for (int i = 0; i < 24; i++) {
            Bug bug = new Bug(COURT_WIDTH, COURT_HEIGHT);
            bug.setPx(((i % 6) * 100) + 220);
            if (i < 6) {
                bug.setPy(0);
            } else if (i < 12) {
                bug.setPy(70);
            } else if (i < 18) {
                bug.setPy(140);
            } else {
                bug.setPy(210);
            }
            bugs.put(i, bug);
        }
        swap = new Swap(COURT_WIDTH, COURT_HEIGHT);
        playing = true;
        status.setText("Running...");
        clicked = false;
        won = false;
        swammo.setPx(swap.getPx() + 30);
        swammo.setPy(swap.getPy());
        bammo.setPy(-200);
        lost = false;
        file = new File("UndoData.txt");
        try {
            fr = new FileReader(file);
        } catch (IOException e) {
            throw new IllegalArgumentException("file doesn't exist.");
        }
        br = new BufferedReader(fr);
        try {
            fw = new FileWriter("UndoData.txt");
        } catch (IOException e) {
            throw new IllegalArgumentException("file doesn't exist.");
        }
        bw = new BufferedWriter(fw);


        otherFile = new File("UndoDataOther.txt");
        try {
            otherFr = new FileReader(otherFile);
        } catch (IOException e) {
            throw new IllegalArgumentException("file doesn't exist.");
        }
        otherBr = new BufferedReader(otherFr);
        try {
            otherFw = new FileWriter("UndoDataOther.txt");
        } catch (IOException e) {
            throw new IllegalArgumentException("file doesn't exist.");
        }
        otherBw = new BufferedWriter(otherFw);
        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    public void undo() {
        //bugs = new TreeMap<>();
        bw = new BufferedWriter(fw);
        otherBw = new BufferedWriter(otherFw);
        String s;
        String text;
        ArrayList<List<String>> list = new ArrayList<>();
        swap = new Swap(COURT_WIDTH, COURT_HEIGHT);
        try {
            while ((s = br.readLine()) != null) {
                text = s;
                List<String> separatedList = new ArrayList<>();
                String[] separated = text.split(",");
                separatedList.addAll(Arrays.asList(separated));
                list.add(separatedList);
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.addAll(Arrays.asList(separated));
                undoBugList.add(Integer.parseInt(arrayList.get(0).trim()));
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("file doesn't exist.");
        }
        String ss;
        String texts;
        ArrayList<List<String>> lists = new ArrayList<>();
        try {
            while ((ss = otherBr.readLine()) != null) {
                texts = ss;
                List<String> separatedList = new ArrayList<>();
                String[] separated = texts.split(",");
                separatedList.addAll(Arrays.asList(separated));
                lists.add(separatedList);
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.addAll(Arrays.asList(separated));
                swap.setPx(Integer.parseInt(separatedList.get(0)));
                swap.setPy(Integer.parseInt(separatedList.get(1)));
                swammo.setPx(Integer.parseInt(separatedList.get(2)));
                swammo.setPy(Integer.parseInt(separatedList.get(3)));
                bammo.setPx(Integer.parseInt(separatedList.get(4)));
                bammo.setPy(Integer.parseInt(separatedList.get(5)));
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("file doesn't exist.");
        }
        for (int i = 0; i < 24; i++) {
            Bug bug = new Bug(COURT_WIDTH, COURT_HEIGHT);
            if (undoBugList.contains(i)) {
                bugs.put(i, bug);
                bug.setPx(Integer.parseInt(list.get(undoBugList.indexOf(i)).get(1)));
                bug.setPy(Integer.parseInt(list.get(undoBugList.indexOf(i)).get(2)) - 50);

            }
        }
        playing = true;
        status.setText("Running...");
        clicked = false;
        won = false;
        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    public int getFirstBugPx() {
        return bugs.get(0).getPx();
    }

    public int getFirstBugPy() {
        return bugs.get(0).getPy();
    }

    /**
     * This method is called every time the timer defined in the constructor
     * triggers.
     */
    public void tick() {
        if (playing) {
            // advance the square and snitch in their current direction.
            for (Map.Entry<Integer, Bug> b : bugs.entrySet()) {
                if ((b.getValue().getPx() == 940) || b.getValue().getPx() == 0) {
                    for (Map.Entry<Integer, Bug> bug : bugs.entrySet()) {
                        //changing the velo to go to the other direction
                        bug.getValue().setVx(bug.getValue().getVx() * -1);
                        //bring them down 50 units
                        bug.getValue().setPy(bug.getValue().getPy() + 50);
                    }
                    break;
                }
            }
            for (Map.Entry<Integer, Bug> b : bugs.entrySet()) {
                b.getValue().setPx(b.getValue().getPx() + b.getValue().getVx());
                b.getValue().setPy(b.getValue().getPy() + b.getValue().getVy());
            }

            if (!clicked) {
                swammo.setPx(swap.getPx() + 30);
                swammo.setPy(swap.getPy());
            }
            if (clicked) {
                swammo.move();
            }
            if (swammo.getPy() < 0) {
                swammo.setPx(swap.getPx() + 30);
                swammo.setPy(swap.getPy());
                clicked = false;
            }
            bammo.move();
            swap.move();
            for (Map.Entry<Integer, Bug> bug : bugs.entrySet()) {
                if (swammo.intersects(bug.getValue())) {
                    bug.getValue().setIsHit();
                    System.out.println("bug hit");
                    swammo.setPx(swap.getPx() + 30);
                    swammo.setPy(swap.getPy());
                    clicked = false;
                }
            }
            int temp = 0;
            for (Map.Entry<Integer, Bug> bug : bugs.entrySet()) {
                if (!bug.getValue().getIsHit()) {
                    temp = -1;
                }
            }
            if (temp == 0) {
                won = true;
            }
            if (won) {
                playing = false;
                status.setText("You win!");
            }
            for (Map.Entry<Integer, Bug> bug : bugs.entrySet()) {
                if (bug.getValue().intersects(swap)) {
                    for (Map.Entry<Integer, Bug> b : bugs.entrySet()) {
                        if (!b.getValue().getIsHit()) {
                            bugList.add(b.getKey());
                        }
                    }
                    System.out.println(bugList.size());
                    String s = "";
                    for (int i = 0; i < bugList.size(); i++) {
                        s = s + bugList.get(i) + "," + bugs.get(bugList.get(i)).getPx() + "," +
                                bugs.get(bugList.get(i)).getPy() + "\n";
                    }
                    System.out.println(s);
                    try {
                        bw.write(s);
                        s = "";
                        bw.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    String others = "";
                    others = others + swap.getPx() + "," + swap.getPy() +
                            "," + swammo.getPx() + "," +
                            swammo.getPy() + "," + bammo.getPx() +
                            "," + bammo.getPy();
                    try {
                        otherBw.write(others);
                        others = "";
                        otherBw.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    lost = true;
                    playing = false;
                    status.setText("You lose!");
                }
            }
            if (bammo.intersects(swap)) {
                playing = false;
                status.setText("You lose!");
            }
            // update the display
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Map.Entry<Integer, Bug> b : bugs.entrySet()) {
            if (!b.getValue().getIsHit()) {
                b.getValue().draw(g);
            }
            if (b.getValue().getIsHit()) {
                b.getValue().setPx(-100);
                b.getValue().setPy(-100);
                b.getValue().draw(g);
            }
        }
        swap.draw(g);
        swammo.draw(g);
        bammo.draw(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}