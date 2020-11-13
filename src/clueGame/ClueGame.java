package clueGame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClueGame extends JFrame {
	
	public static final int WIDTH = 880;
	public static final int HEIGHT = 820;
	
	public String layoutConfigFile = "ClueLayout.csv";
	private String setupConfigFile = "ClueSetup.txt";

	private static GameControlPanel controlPanel;
	private static ClueCardsPanel cardsPanel;
	private static int roll;
	private Board instance;
	
	public ClueGame() {
		controlPanel = new GameControlPanel();
		cardsPanel = new ClueCardsPanel();
		cardsPanel.setPreferredSize(new Dimension(160, 0));
		setup();
		instance.playGame();
	}
	
	public static void rollDice() {
		Random random = new Random();
		roll = random.nextInt(6) + 1;
		controlPanel.setRoll("" + roll);
	}
	
	public static int getRoll() {
		return roll;
	}
	
	private void setup() {
		instance = Board.getInstance();
		instance.setConfigFiles(layoutConfigFile, setupConfigFile);	
		instance.initialize();
		
		add(controlPanel, BorderLayout.SOUTH);
		add(cardsPanel, BorderLayout.EAST);
		add(instance, BorderLayout.CENTER);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(HEIGHT, WIDTH);
		setTitle("Clue Game");
		setVisible(true);
		
		String start = "           You are " + instance.getHuman().getName() +
				". \n You must solve the mystery \nbefore the computer players";
		JOptionPane.showMessageDialog(this, start, "Welcome detective", 1);
	}

	public static void main(String[] args) {
		ClueGame game = new ClueGame();
	}

}
