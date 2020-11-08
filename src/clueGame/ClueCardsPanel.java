package clueGame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class ClueCardsPanel extends JFrame{
	
	private JPanel peopleInHand = new JPanel(); 
	private JPanel peopleSeen = new JPanel();
	private JPanel roomsInHand = new JPanel();
	private JPanel roomsSeen = new JPanel();
	private JPanel weaponsInHand = new JPanel();
	private JPanel weaponsSeen = new JPanel();
	
	
	public ClueCardsPanel() {
		JPanel knownCards = new JPanel();
		knownCards.setBorder(new TitledBorder(new EtchedBorder(), "Known Cards"));
		knownCards.setLayout(new GridLayout(0,1));
		
		JPanel people = createPanel("People", peopleInHand, peopleSeen);
		JPanel rooms = createPanel("Rooms", roomsInHand, roomsSeen);
		JPanel weapons = createPanel("Weapons", weaponsInHand, weaponsSeen);
		
		knownCards.add(people);
		knownCards.add(rooms);
		knownCards.add(weapons);
		
		add(knownCards, BorderLayout.CENTER);
	}


	private JPanel createPanel(String name, JPanel handCards, JPanel seenCards) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,1));
		panel.setBorder(new TitledBorder(new EtchedBorder(), name));
		JLabel inHand = new JLabel("In Hand:");
		panel.add(inHand);
		
		handCards.setLayout(new GridLayout(0,1));
		panel.add(handCards);
		JTextField noneCard1 = new JTextField("None", 10);
		handCards.add(noneCard1);
		noneCard1.setEditable(false);
		
		JLabel seen = new JLabel("Seen:");
		panel.add(seen);
		seenCards.setLayout(new GridLayout(0,1));
		panel.add(seenCards);
		JTextField noneCard2 = new JTextField("None", 5);
		noneCard2.setEditable(false);
		seenCards.add(noneCard2);
		
		return panel;
	}
	
	public void addToHand(String name, CardType type) {
		JTextField card = new JTextField(name);
		card.setEditable(false);
		if (type == CardType.PLAYER) {
			checkEmpty(card, peopleInHand);
		}
		else if (type == CardType.ROOM) {
			checkEmpty(card, roomsInHand);
		}
		else {
			checkEmpty(card, weaponsInHand);
		}
	}
	
	public void addToSeen(String name, CardType type) {
		JTextField card = new JTextField(name);
		card.setEditable(false);
		if (type == CardType.PLAYER) {
			checkEmpty(card, peopleSeen);
		}
		else if (type == CardType.ROOM) {
			checkEmpty(card, roomsSeen);
		}
		else {
			checkEmpty(card, weaponsSeen);
		}
	}
	
	private void checkEmpty(JTextField card, JPanel panel) {
		Component[] items = panel.getComponents();
		JTextField first = (JTextField) items[0];
		if (first.getText().equals("None")) {
			panel.remove(first);
			panel.add(card);
		}
		else {
			panel.add(card);
		}
	}
	

	public static void main(String[] args) {
		ClueCardsPanel control = new ClueCardsPanel();
		control.setSize(250, 900);
		control.setTitle("Control Panel For Clue");
		control.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		control.addToHand("Green", CardType.PLAYER);
		control.addToHand("Electrical", CardType.ROOM);
		control.addToHand("Gun", CardType.WEAPON);
		control.addToSeen("Blue", CardType.PLAYER);
		control.addToSeen("Storage", CardType.ROOM);
		control.addToSeen("Spike", CardType.WEAPON);
		
		control.addToSeen("Blue", CardType.PLAYER);
		control.addToSeen("Blue", CardType.PLAYER);
		control.addToSeen("Blue", CardType.PLAYER);
		control.addToSeen("Blue", CardType.PLAYER);
		control.addToSeen("Blue", CardType.PLAYER);
		control.addToSeen("Blue", CardType.PLAYER);
		control.addToSeen("Blue", CardType.PLAYER);
		
		control.setVisible(true);
	}

	
}
