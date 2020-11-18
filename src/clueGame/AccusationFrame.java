package clueGame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class AccusationFrame extends JFrame{
	
	JComboBox personP;
	JComboBox weaponP;
	JComboBox roomP;
	
	private static Board instance = Board.getInstance();
	
	public AccusationFrame() {
		setSize(400, 200);
		setLayout(new GridLayout(4,2));
		setTitle("Make an Accusation");
		
		JLabel person = new JLabel("Person:");
		person.setHorizontalAlignment(JTextField.CENTER);
		JLabel weapon = new JLabel("Weapon:");
		weapon.setHorizontalAlignment(JTextField.CENTER);
		JLabel room = new JLabel("Room:");
		room.setHorizontalAlignment(JTextField.CENTER);
				
		JButton submit = new JButton("Submit");
		submit.addActionListener(new SubmitListener());
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(e -> {
			System.exit(0);
		});
		
		personP = new JComboBox();
		weaponP = new JComboBox();
		roomP = new JComboBox();
		
		add(person);
		add(personP);
		add(weapon);
		add(weaponP);
		add(room);
		add(roomP);
		add(cancel);
		add(submit);
		
		populateComboBoxes();
	}
	
	private void populateComboBoxes() {
		Set<Card> deck = instance.getDeck();
		for (Card card : deck) {
			CardType type = card.getType();
			if(type == CardType.WEAPON){
				weaponP.addItem(card.getName());
			}else if(type == CardType.PLAYER) {
				personP.addItem(card.getName());
			}else {
				roomP.addItem(card.getName());
			}	
		}
	}
	
	private class SubmitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			Solution answer = instance.getTheAnswer();
			String personA = personP.getSelectedItem().toString();
			String weaponA = weaponP.getSelectedItem().toString();
			String roomA = roomP.getSelectedItem().toString();
			String person = answer.person.getName();
			String weapon = answer.weapon.getName();
			String room = answer.room.getName();
			if (person.equals(personA) && weapon.equals(weaponA) && room.equals(roomA)) {
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
