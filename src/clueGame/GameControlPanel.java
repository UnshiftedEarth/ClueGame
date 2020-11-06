package clueGame;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GameControlPanel extends JFrame {
	
	
	public GameControlPanel() {
		JPanel gameControlPanel = new JPanel();
		JPanel firstRow = new JPanel();
		JPanel secondRow = new JPanel();
		
		gameControlPanel.setLayout(new GridLayout(2,0));
		gameControlPanel.add(firstRow);
		gameControlPanel.add(secondRow);
		
		firstRow.setLayout(new GridLayout());
		JPanel whoseTurn = createWhoseTurn();
		firstRow.add(whoseTurn);
		JPanel roll = createRoll();
		firstRow.add(roll);
		JButton makeAccusation = new JButton("Make Accusation");
		firstRow.add(makeAccusation);
		JButton next = new JButton("Next!");
		firstRow.add(next);
		
		add(gameControlPanel, BorderLayout.CENTER);
	}
	
	private JPanel createRoll() {
		JPanel roll = new JPanel();
		roll.setLayout(new GridLayout(1,2));
		JLabel label = new JLabel("Roll:");
		JTextField num = new JTextField("5");
		roll.add(label);
		roll.add(num);
		return roll;
	}

	private JPanel createWhoseTurn() {
		JPanel whoseTurn = new JPanel();
		whoseTurn.setLayout(new GridLayout(2,1));
		JLabel label = new JLabel("Whose Turn?");
		JTextField player = new JTextField("sdfsd");
		whoseTurn.add(label);
		whoseTurn.add(player);
		return whoseTurn;
	}

	public static void main(String[] args) {
		GameControlPanel control = new GameControlPanel();
		control.setSize(750, 180);
		control.setTitle("Control Panel For Clue");
		control.setVisible(true);
		control.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
