package clueGame;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

public class ClueGame extends JFrame {
	
	public static final int WIDTH = 880;
	public static final int HEIGHT = 820;
	
	public String layoutConfigFile = "ClueLayout.csv";
	private String setupConfigFile = "ClueSetup.txt";

	private GameControlPanel controlPanel;
	private ClueCardsPanel cardsPanel;
	private Board instance;
	
	public ClueGame() {
		controlPanel = new GameControlPanel();
		cardsPanel = new ClueCardsPanel();
		cardsPanel.setPreferredSize(new Dimension(160, 0));
		setup();
		playGame();
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
		setVisible(true);
	}

	private void playGame() {
		// TODO start clue game
	}

	public static void main(String[] args) {
		ClueGame game = new ClueGame();
	}

}
