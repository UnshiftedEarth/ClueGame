package clueGame;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public abstract class Player {
	
	private String name;
	private Color color;
	private int row;
	private int column;
	private Set<Card> hand;
	
	
	public Player() {
		super();
		hand = new HashSet<>();
	}
	
	public Player(String name, String color) {
		this.name = name;
		setColor(color);
		hand = new HashSet<>();
	}
	

	public void updateHand(Card card) {
		hand.add(card);
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
