package clueGame;

public class HumanPlayer extends Player {
	
	
	
	public HumanPlayer() {
		super();
	}
	
	public HumanPlayer(String name, String color, int row, int col) {
		super(name, color);
		super.setLocation(row, col);
	}
	
	// setters and getters 
}
