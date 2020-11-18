package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class ClueCardsPanel extends JPanel{
	
	private JPanel peopleInHand = new JPanel(); 
	private JPanel peopleSeen = new JPanel();
	private JPanel roomsInHand = new JPanel();
	private JPanel roomsSeen = new JPanel();
	private JPanel weaponsInHand = new JPanel();
	private JPanel weaponsSeen = new JPanel();
	private Font font;
	
	
	public ClueCardsPanel() {
		font = new Font("Helvetica", Font.PLAIN, 15);
		// create panel to hold components
		JPanel knownCards = new JPanel();
		TitledBorder titledBorder = new TitledBorder(new EtchedBorder(0), "Known Cards");
		titledBorder.setTitleJustification(2);
		knownCards.setBorder(titledBorder);
		knownCards.setLayout(new GridLayout(0,1));
		
		// create the sub-panels for each type of card
		JPanel people = createPanel("People", peopleInHand, peopleSeen);
		JPanel rooms = createPanel("Rooms", roomsInHand, roomsSeen);
		JPanel weapons = createPanel("Weapons", weaponsInHand, weaponsSeen);
		
		// add sub-panels to the main panel
		knownCards.add(people);
		knownCards.add(rooms);
		knownCards.add(weapons);
		// add panel to jframe
		this.setLayout(new GridLayout());
		add(knownCards);
	}

	/*
	 * Method for creating each panel according to the parameters
	 */
	private JPanel createPanel(String name, JPanel handCards, JPanel seenCards) {
		// setup panel and add label
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setBorder(new TitledBorder(new EtchedBorder(), name));
		JLabel inHand = new JLabel("In Hand:");
		inHand.setFont(new Font("Helvetica", Font.BOLD, 15));
		inHand.setPreferredSize(new Dimension(0,60));
		panel.add(inHand);
		
		// add sub-panel to hold cards
		handCards.setLayout(new GridLayout(0,1));
		panel.add(handCards);
		JTextField noneCard1 = new JTextField("None", 10);
		noneCard1.setFont(font);
		handCards.add(noneCard1);
		noneCard1.setEditable(false);
		
		//add seen label and sub-panel to hold cards
		JLabel seen = new JLabel("Seen:");
		seen.setFont(new Font("Helvetica", Font.BOLD, 15));
		seen.setPreferredSize(new Dimension(0,60));
		panel.add(seen);
		seenCards.setLayout(new GridLayout(0,1));
		panel.add(seenCards);
		JTextField noneCard2 = new JTextField("None");
		noneCard2.setFont(font);
		noneCard2.setEditable(false);
		seenCards.add(noneCard2);
		
		return panel;
	}
	
	// method to add a card name to the hand 
	public void addToHand(String name, CardType type, Player player) {
		JTextField card = new JTextField(name);
		card.setFont(font);
		card.setBackground(player.getColor());
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
	
	// method to add card name to the seen list
	public void addToSeen(String name, CardType type, Player player) {
		JTextField card = new JTextField(name);
		card.setFont(font);
		card.setBackground(player.getColor());
		card.setPreferredSize(new Dimension(0,60));
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
	
	// helper method to check if seen or hand is empty and adjust accordingly
	private void checkEmpty(JTextField card, JPanel panel) {
		Component[] items = panel.getComponents();
		JTextField first = (JTextField) items[0];
		if (first.getText().equals("None")) {
			panel.remove(first);
			panel.add(card);
		}
		else {
			// make sure card is not already in the list
			for (Component c : items) {
				JTextField field = (JTextField) c;
				if (field.getText().equals(card.getText())) {
					return;
				}
			}
			panel.add(card);
		}
	}
	

	public static void main(String[] args) {
		JFrame display = new JFrame();
		ClueCardsPanel control = new ClueCardsPanel();
		display.add(control, BorderLayout.CENTER);
		display.setSize(250, 900);
		display.setTitle("Control Panel For Clue");
		display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Board board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");	
		board.initialize();
		ArrayList<Player> players = board.getPlayers();
		
		control.addToHand("Green", CardType.PLAYER, players.get(0));
		control.addToHand("Blue", CardType.PLAYER, players.get(0));
		control.addToHand("Gun", CardType.WEAPON, players.get(0));
		control.addToSeen("Storage", CardType.ROOM, players.get(4));
		control.addToSeen("Spike", CardType.WEAPON, players.get(5));
		
		control.addToSeen("Yellow", CardType.PLAYER, players.get(2));
		control.addToSeen("Red", CardType.PLAYER, players.get(3));
		control.addToSeen("Pink", CardType.PLAYER, players.get(4));
		
		control.addToSeen("Laboratory", CardType.ROOM, players.get(1));
		control.addToSeen("Electrical", CardType.ROOM, players.get(1));
		control.addToSeen("Weapons", CardType.ROOM, players.get(3));
		control.addToSeen("Admin", CardType.ROOM, players.get(4));
		control.addToSeen("Specimen", CardType.ROOM, players.get(5));
		control.addToSeen("Oxygen", CardType.ROOM, players.get(1));
		control.addToSeen("Security", CardType.ROOM, players.get(2));
		
		control.addToSeen("Knife", CardType.WEAPON, players.get(3));
		control.addToSeen("Bat", CardType.WEAPON, players.get(2));
		control.addToSeen("Bow", CardType.WEAPON, players.get(5));
		
		display.setVisible(true);
	}

	
}
