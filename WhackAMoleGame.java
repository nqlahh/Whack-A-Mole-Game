import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class WhackAMoleGame extends JFrame implements ActionListener {
    private JPanel topPanel, gamePanel;
    private JButton startButton;
    private JLabel timerLabel,scoreLabel;
    private JButton[] moleButtons = new JButton[9];
    private Timer gameTimer, moleTimer;
    private int timeLeft = 30; // game duration in seconds
    private int score = 0;
    private int moleIndex = -1;
    private Random random = new Random();
    private ImageIcon moleIcon; 

    public WhackAMoleGame() {
        super("Whack-a-Mole");
        setLayout(new BorderLayout());

        // Load mole icon
        moleIcon = new ImageIcon("mole.png"); // Ensure you have a mole image in the same directory
        Image scaledImage = moleIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        moleIcon = new ImageIcon(scaledImage);

        // Top panel with start button and timer label
        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        startButton = new JButton("Start");
        startButton.addActionListener(e -> startGame());
        topPanel.add(startButton, BorderLayout.WEST);

        timerLabel = new JLabel("Time: 30", JLabel.CENTER);
        topPanel.add(timerLabel, BorderLayout.CENTER);

        scoreLabel = new JLabel("Score: 0", JLabel.RIGHT);
        topPanel.add(scoreLabel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Game grid panel
        gamePanel = new JPanel(new GridLayout(3, 3));
        for (int i = 0; i < 9; i++) {
            moleButtons[i] = new JButton();
            moleButtons[i].setBackground(Color.LIGHT_GRAY);
            moleButtons[i].addActionListener(this); // Button click = "whack"
            gamePanel.add(moleButtons[i]);
        }
        add(gamePanel, BorderLayout.CENTER);

        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void startGame() {
        startButton.setEnabled(false);
        score = 0;
        timeLeft = 30; 
        scoreLabel.setText("Score: " + score);
        timerLabel.setText("Time: " + timeLeft);

        // Game countdown timer (1 second)
        gameTimer = new Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time: " + timeLeft);
            if (timeLeft <= 0) {
                endGame();
            }
        });
        gameTimer.start();

        // Mole pop-up timer (every 800 ms)
        moleTimer = new Timer(800, e -> showRandomMole());
        moleTimer.start();
    }

    private void showRandomMole() {
        // Reset previous mole
        if (moleIndex != -1) {
            moleButtons[moleIndex].setIcon(null);
        }

        // Choose new mole index
        moleIndex = random.nextInt(9);
        moleButtons[moleIndex].setIcon(moleIcon);
    }

    private void endGame() {
        gameTimer.stop();
        moleTimer.stop();
        if (moleIndex != -1) moleButtons[moleIndex].setIcon(null);
        startButton.setEnabled(true);
        JOptionPane.showMessageDialog(this, "Time's up! You whacked " + score +  " mole(s)!");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 9; i++) {
            if (e.getSource() == moleButtons[i]) {
                if (i == moleIndex) {
                    score++;
                    scoreLabel.setText("Score: " + score);
                    moleButtons[i].setIcon(null);
                    moleIndex = -1; // prevent double counting
                }
            }
        }
    }

    public static void main(String[] args) {
        new WhackAMoleGame();
    }
}
