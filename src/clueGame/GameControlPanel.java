package clueGame;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GameControlPanel extends JFrame {
	
	private JTextField result;
	private JTextField guess;
	private JTextField roll;
	private JTextField turnDescription;
	
	public GameControlPanel() {
		JPanel gameControlPanel = new JPanel();
		JPanel firstRow = new JPanel();
		firstRow.setLayout(new GridLayout());
		JPanel secondRow = new JPanel();
		secondRow.setLayout(new GridLayout(1,2));
		
		gameControlPanel.setLayout(new GridLayout(2,1));
		gameControlPanel.add(firstRow);
		gameControlPanel.add(secondRow);
		
		// add items to first row
		JPanel whoseTurn = createWhoseTurn();
		firstRow.add(whoseTurn);
		JPanel roll = createRoll();
		firstRow.add(roll);
		JButton makeAccusation = new JButton("Make Accusation");
		firstRow.add(makeAccusation);
		JButton next = new JButton("Next!");
		firstRow.add(next);
		
		// add items to second row
		JPanel resultPanel = createGuessPanel();
		JPanel guessPanel = createResultPanel();
		secondRow.add(guessPanel);
		secondRow.add(resultPanel);
		
		add(gameControlPanel, BorderLayout.CENTER);
	}

	private JPanel createResultPanel() {
		JPanel resultPanel = new JPanel();
		resultPanel.setLayout(new GridLayout());
		resultPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));
		result = new JTextField();
		result.setEditable(false);
		resultPanel.add(result);
		return resultPanel;
	}

	private JPanel createGuessPanel() {
		JPanel guessPanel = new JPanel();
		guessPanel.setLayout(new GridLayout());
		guessPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
		guess = new JTextField();
		guess.setEditable(false);
		guessPanel.add(guess);
		return guessPanel;
	}
	
	private JPanel createRoll() {
		JPanel rollPanel = new JPanel();
		JLabel label = new JLabel("Roll:");
		roll = new JTextField(5);
		roll.setEditable(false);
		rollPanel.add(label);
		rollPanel.add(roll);
		return rollPanel;
	}

	private JPanel createWhoseTurn() {
		JPanel whoseTurnPanel = new JPanel();
		JLabel label = new JLabel("Whose Turn?");
		turnDescription = new JTextField(16);
		turnDescription.setEditable(false);
		whoseTurnPanel.add(label);
		whoseTurnPanel.add(turnDescription);
		return whoseTurnPanel;
	}

	// setters 
	public void setResult(String r) {
		result.setText(r);
	}

	public void setGuess(String g) {
		guess.setText(g);
	}

	public void setRoll(String r) {
		roll.setText(r);
	}

	public void setTurnDescription(String d) {
		turnDescription.setText(d);
	}

	public static void main(String[] args) {
		GameControlPanel control = new GameControlPanel();
		control.setSize(750, 180);
		control.setTitle("Control Panel For Clue");
		control.setVisible(true);
		control.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		control.setGuess("Blue");
		control.setTurnDescription("I don't have a guess. Sorry :(");
		control.setRoll("3");
		control.setResult("You lose then");
	}

}
