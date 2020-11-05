package clueGame;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player {
	
	Random random = new Random();
	
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
	
	/*
	 * Method that selects a target to move to from a list of targets 
	 */
	public BoardCell selectTarget(Set<BoardCell> targets) {
		Board board = Board.getInstance();
		ArrayList<BoardCell> rooms = new ArrayList<BoardCell>();
		// look for rooms not in seen list
		for (BoardCell cell : targets) {
			if (cell.isRoomCenter()) {
				String roomName = board.getRoom(cell).getName();
				if (!seenCards.contains(board.getCard(roomName))) {
					rooms.add(cell);
				}
			}
		}
		// if only one room in targets not seen
		if (rooms.size() == 1) {
			return rooms.get(0);
		}
		// if more than one room in targets not seen
		else if (rooms.size() > 1) {
			int rand = random.nextInt(rooms.size());
			return rooms.get(rand);
		}
		// if there are targets and no rooms not seen
		else if (targets.size() != 0) {
			Object[] list = targets.toArray();
			int rand = random.nextInt(targets.size());
			return (BoardCell) list[rand];
		}
		return null;
	}
	
	// setters and getters 
}
