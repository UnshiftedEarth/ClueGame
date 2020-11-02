package clueGame;

public class ComputerPlayer extends Player {
	

	
	public ComputerPlayer() {
		super();
	}
	
	public ComputerPlayer(String name, String color) {
		super(name, color);
	}
	
	public Solution createSuggestion() {
		//TODO write
		return new Solution();
	}
	
	public BoardCell selectTargets() {
		//TODO write
		return new BoardCell(0,0);
	}
	
	// setters and getters 
}
