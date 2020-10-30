package clueGame;

import java.util.HashSet;
import java.util.Set;

public class Room {
	
	private String name;
	private BoardCell centerCell;
	private BoardCell labelCell;
	private Room secretPassage;
	private Set<BoardCell> doorList;
	

	public Room(String name) {
		super();
		this.name = name;
		doorList = new HashSet<>();
	}
	
	public String getName() {
		return name;
	}

	public BoardCell getCenterCell() {
		return centerCell;
	}

	public void setCenterCell(BoardCell centerCell) {
		this.centerCell = centerCell;
	}

	public BoardCell getLabelCell() {
		return labelCell;
	}

	public void setLabelCell(BoardCell labelCell) {
		this.labelCell = labelCell;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addToDoorList(BoardCell cell) {
		doorList.add(cell);
	}

	public Room getSecretPassage() {
		return secretPassage;
	}

	public void setSecretPassage(Room secretPassage) {
		this.secretPassage = secretPassage;
	}

	public Set<BoardCell> getDoorList() {
		return doorList;
	}

}
