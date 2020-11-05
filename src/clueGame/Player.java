package clueGame;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public abstract class Player {
	
	private String name;
	private Color color;
	private int row;
	private int column;
	private Set<Card> hand;
	protected Set<Card> seenCards;
	
	
	public Player() {
		super();
		hand = new HashSet<>();
		seenCards = new HashSet<>();
	}
	
	public Player(String name, String color) {
		this.name = name;
		setColor(color);
		hand = new HashSet<>();
	}
	

	public void updateHand(Card card) {
		hand.add(card);
	}
	
	/*
	 * Looks for matches in players hand to disprove a suggestion
	 * returns null if no matches found
	 */
	public Card disproveSuggestion(Solution suggestion) {
		Card player = suggestion.person;
		Card room = suggestion.room;
		Card weapon = suggestion.weapon;
		ArrayList<Card> match = new ArrayList<>();
		// create a list of cards in hand that match the suggestion
		for (Card card : hand) {
			if (card.equals(player) || card.equals(room) || card.equals(weapon)) {
				match.add(card);
			}
		}
		// handle different numbers of matches
		if (match.size() == 1) {
			return match.get(0);
		}
		else if (match.size() > 1) {
			Random random = new Random();
			int rand = random.nextInt(match.size());
			return match.get(rand);
		}
		return null;
	}
	
	public void updateSeen(Card seenCard) {
		seenCards.add(seenCard);
	}
	
	public void clearHand() {
		hand.clear();
	}
	
	public void clearSeen() {
		seenCards.clear();
	}
	

	// setters and getters
	public Object getName() {
		return name;
	}

	public Object getColor() {
		return color;
	}
	
	public Set<Card> getHand() {
		return hand;
	}

	public void setColor(String c) {
		try {
		    Field field = Class.forName("java.awt.Color").getField(c);
		    color = (Color) field.get(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
