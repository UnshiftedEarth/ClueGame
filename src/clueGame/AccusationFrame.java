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

public class AccusationFrame extends JDialog{
	
	private JComboBox personP;
	private JComboBox weaponP;
	private JComboBox roomP;
	Font font = new Font("Helvetica", Font.BOLD, 15);
	
	private static Board instance = Board.getInstance();
	
	public AccusationFrame() {
		setSize(400, 200);
		setLayout(new GridLayout(4,2));
		setTitle("Make an Accusation");
		
		JLabel person = new JLabel("Person:");
		person.setHorizontalAlignment(JTextField.CENTER);
		person.setFont(font);
		JLabel weapon = new JLabel("Weapon:");
		weapon.setHorizontalAlignment(JTextField.CENTER);
		weapon.setFont(font);
		JLabel room = new JLabel("Room:");
		room.setFont(font);
		room.setHorizontalAlignment(JTextField.CENTER);
				
		JButton submit = new JButton("Submit");
		submit.addActionListener(new SubmitListener());
		submit.setFont(new Font("Helvetica", Font.BOLD, 20));
		JButton cancel = new JButton("Cancel");
		cancel.setFont(new Font("Helvetica", Font.BOLD, 20));
		cancel.addActionListener(e -> {
			this.setVisible(false);
		});
		
		personP = new JComboBox();
		personP.setFont(font);
		weaponP = new JComboBox();
		weaponP.setFont(font);
		roomP = new JComboBox();
		roomP.setFont(font);
		
		add(room);
		add(roomP);
		add(person);
		add(personP);
		add(weapon);
		add(weaponP);
		add(cancel);
		add(submit);
		
		populateComboBoxes();
	}
	
	private void populateComboBoxes() {
		Set<Card> deck = instance.getDeck();
		for (Card card : deck) {
			CardType type = card.getType();
			if (type == CardType.WEAPON){
				weaponP.addItem(card.getName());
			}
			else if (type == CardType.PLAYER) {
				personP.addItem(card.getName());
			}
			else {
				roomP.addItem(card.getName());
			}	
		}
	}
	
	private class SubmitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			System.out.println("run");
			Solution answer = instance.getTheAnswer();
			String personA = personP.getSelectedItem().toString();
			String weaponA = weaponP.getSelectedItem().toString();
			String roomA = roomP.getSelectedItem().toString();
			String person = answer.person.getName();
			String weapon = answer.weapon.getName();
			String room = answer.room.getName();
			if (person.equals(personA) && weapon.equals(weaponA) && room.equals(roomA)) {
				instance.displayWin();
			}
			else {
				instance.displayLoss();
			}
		}
		
	}

	public static void main(String[] args) {
		instance.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");	
		instance.initialize();
		AccusationFrame frame = new AccusationFrame();
		frame.setVisible(true);	
	}

}
