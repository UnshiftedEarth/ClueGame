package clueGame;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class SuggestionFrame extends JDialog {
	
	private JComboBox personP;
	private JComboBox weaponP;
	private JTextField roomP;
	private static Board instance = Board.getInstance();
	Font font = new Font("Helvetica", Font.BOLD, 15);
	
	public SuggestionFrame() {
		setSize(400, 200);
		setLayout(new GridLayout(4,2));
		setTitle("Make an Suggestion");
		
		JLabel person = new JLabel("Person:");
		person.setHorizontalAlignment(JTextField.CENTER);
		person.setFont(font);
		JLabel weapon = new JLabel("Weapon:");
		weapon.setHorizontalAlignment(JTextField.CENTER);
		weapon.setFont(font);
		JLabel room = new JLabel("Room:");
		room.setHorizontalAlignment(JTextField.CENTER);
		room.setFont(font);
				
		JButton submit = new JButton("Submit");
		submit.setFont(new Font("Helvetica", Font.BOLD, 20));
		JButton cancel = new JButton("Cancel");
		cancel.setFont(new Font("Helvetica", Font.BOLD, 20));
		
		submit.addActionListener(e -> {
			Solution suggestion = new Solution();
			suggestion.person = instance.getCard(personP.getSelectedItem().toString());
			suggestion.room = instance.getCard(roomP.getText());
			suggestion.weapon = instance.getCard(weaponP.getSelectedItem().toString());
			instance.buttonMakeSuggestion(suggestion);
			this.setVisible(false);
		});
		cancel.addActionListener(e -> {
			this.setVisible(false);
		});
		
		personP = new JComboBox();
		personP.setFont(font);
		weaponP = new JComboBox();
		weaponP.setFont(font);
		roomP = new JTextField();
		roomP.setFont(font);
		roomP.setEditable(false);
		
		add(room);
		add(roomP);
		add(person);
		add(personP);
		add(weapon);
		add(weaponP);
		add(cancel);
		add(submit);
	}
	
	public SuggestionFrame(String room) {
		this();
		populateComboBoxes(room);
	}
	
	private void populateComboBoxes(String room) {
		Set<Card> deck = instance.getDeck();
		roomP.setText(room);
		for (Card card : deck) {
			CardType type = card.getType();
			if (type == CardType.WEAPON){
				weaponP.addItem(card.getName());
			}
			else if (type == CardType.PLAYER) {
				personP.addItem(card.getName());
			}	
		}
	}
	
	private class SubmitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			Solution suggestion = new Solution();
			suggestion.person = instance.getCard(personP.getSelectedItem().toString());
			suggestion.room = instance.getCard(roomP.getText());
			suggestion.weapon = instance.getCard(weaponP.getSelectedItem().toString());
			instance.buttonMakeSuggestion(suggestion);
			
		}
		
	}

	public static void main(String[] args) {
		instance.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");	
		instance.initialize();
		SuggestionFrame frame = new SuggestionFrame();
		frame.setVisible(true);	
	}

}
