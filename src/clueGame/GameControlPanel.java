package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GameControlPanel extends JPanel {
	
	private JTextField result;
	private JTextField guess;
	private JTextField roll;
	private JTextField turnDescription;
	private Font font;
	
	public GameControlPanel() {
		font = new Font("Comic Sans MS", Font.BOLD, 20);
		// setup up initial panels
		JPanel gameControlPanel = new JPanel();
		JPanel firstRow = new JPanel();
		firstRow.setLayout(new GridLayout());
		JPanel secondRow = new JPanel();
		secondRow.setLayout(new GridLayout(1,2));
		
		gameControlPanel.setLayout(new GridLayout(2,1));
		gameControlPanel.add(firstRow);
		gameControlPanel.add(secondRow);
		
		// add items to first row
		JPanel whoseTurn = createWhoseTurnPanel();
		firstRow.add(whoseTurn);
		JPanel roll = createRollPanel();
		firstRow.add(roll);
		JButton makeAccusation = new JButton("Make Accusation");
		makeAccusation.setFont(new Font("Helvetica", Font.BOLD, 20));
		firstRow.add(makeAccusation);
		JButton next = new JButton("Next!");
		next.setFont(new Font("Helvetica", Font.BOLD, 20));
		next.addActionListener(new ButtonListner());
		firstRow.add(next);
		
		// add items to second row
		JPanel resultPanel = createGuessPanel();
		JPanel guessPanel = createResultPanel();
		secondRow.add(resultPanel);
		secondRow.add(guessPanel);
		
		setLayout(new GridLayout());
		add(gameControlPanel);
	}

	// creates the panel that displays the suggestion result
	private JPanel createResultPanel() {
		JPanel resultPanel = new JPanel();
		resultPanel.setLayout(new GridLayout());
		resultPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));
		result = new JTextField();
		result.setEditable(false);
		result.setFont(new Font("Helvetica", Font.BOLD, 20));
		result.setHorizontalAlignment(JTextField.CENTER);
		resultPanel.add(result);
		return resultPanel;
	}

	// creates the panel that displays the suggestion
	private JPanel createGuessPanel() {
		JPanel guessPanel = new JPanel();
		guessPanel.setLayout(new GridLayout());
		guessPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
		guess = new JTextField();
		guess.setEditable(false);
		guess.setFont(new Font("Helvetica", Font.BOLD, 20));
		guess.setHorizontalAlignment(JTextField.CENTER);
		guessPanel.add(guess);
		return guessPanel;
	}
	
	// creates the panel that displays the die roll
	private JPanel createRollPanel() {
		JPanel rollPanel = new JPanel();
		JLabel label = new JLabel("Roll:");
		label.setFont(font);
		roll = new JTextField(5);
		roll.setEditable(false);
		rollPanel.add(label);
		rollPanel.add(roll);
		roll.setFont(font);
		//roll.setPreferredSize(new Dimension(4,4));
		return rollPanel;
	}
	
	// creates the panel that displays whose turn it is
	private JPanel createWhoseTurnPanel() {
		JPanel whoseTurnPanel = new JPanel();
		JLabel label = new JLabel("Whose Turn?");
		label.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		turnDescription = new JTextField(16);
		turnDescription.setEditable(false);
		turnDescription.setHorizontalAlignment(JTextField.CENTER);
		turnDescription.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
		whoseTurnPanel.add(label);
		whoseTurnPanel.add(turnDescription);
		return whoseTurnPanel;
	}
	
	private class ButtonListner implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			Board instance = Board.getInstance();
			instance.buttonNext();
		}
		
	}

	// setters 
	public void setResult(String r, Color c) {
		result.setText(r);
		if (r.equals("")) {
			result.setOpaque(false);
		}
		else {
			result.setOpaque(true);
			result.setBackground(c);
		}
	}

	public void setGuess(String g, Color c) {
		guess.setText(g);
		if (g.equals("")) {
			guess.setOpaque(false);
		}
		else {
			guess.setOpaque(true);
			guess.setBackground(c);
		}
	}

	public void setRoll(String r) {
		roll.setText(r);
	}

	public void setTurnDescription(String d, Color color) {
		turnDescription.setText(d);
		turnDescription.setBackground(color);
	}

	public static void main(String[] args) {
		JFrame display = new JFrame();
		GameControlPanel control = new GameControlPanel();
		display.add(control, BorderLayout.CENTER);
		display.setSize(750, 180);
		display.setTitle("Control Panel For Clue");
		display.setVisible(true);
		display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// test the setters
		control.setGuess("Blue", Color.GREEN);
		control.setTurnDescription("I don't have a guess. Sorry :(", Color.GRAY);
		control.setRoll("3");
		control.setResult("You lose then", Color.GREEN);
	}

}
