package clueGame;

import java.awt.Color;

public abstract class Player {
	
	private String name;
	private Color color;
	
	
	public Player() {
		super();
	}
	
	// setters and getters
	public Object getName() {
		return name;
	}

	public Object getColor() {
		return color;
	}
}
