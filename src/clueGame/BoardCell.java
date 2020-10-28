package clueGame;

import java.util.HashSet;
import java.util.Set;


public class BoardCell implements Comparable<BoardCell> {
	
	private int row;
	private int col;
	private char initial;
	private char secretPassage;
	private DoorDirection doorDirection;
	private boolean roomLabel;
	private boolean roomCenter;
	private boolean isOccupied;
	private boolean isRoom;
	private Set<BoardCell> adjList;
	
	
	public BoardCell(int row, int col) {
		super();
		this.row = row;
		this.col = col;
		adjList = new HashSet<>();
	}
	
	
	
	// Setters and Getters
	public Set<BoardCell> getAdjList() {
		return adjList;
	}
	
	public void addToAdjList(BoardCell cell) {
		adjList.add(cell);
	}
	
	public void setRoom(boolean room) {
		isRoom = room;
	}
	
	public void setInitial(char initial) {
		this.initial = initial;
	}

	public void setSecretPassage(char secretPassage) {
		this.secretPassage = secretPassage;
	}

	public void setDoorDirection(DoorDirection doorDirection) {
		this.doorDirection = doorDirection;
	}

	public void setRoomLabel(boolean roomLabel) {
		this.roomLabel = roomLabel;
	}

	public void setRoomCenter(boolean roomCenter) {
		this.roomCenter = roomCenter;
	}
	
	public boolean isDoorway() {
		return doorDirection != DoorDirection.NONE;
	}
	
	public boolean isRoom() {
		return isRoom;
	}

	public boolean isOccupied() {
		return isOccupied;
	}
	
	public boolean isLabel() {
		return roomLabel;
	}
	
	public boolean isRoomCenter() {
		return roomCenter;
	}

	public void setOccupied(boolean occupied) {
		isOccupied = occupied;
	}
	
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}
	
	public char getSecretPassage() {
		return secretPassage;
	}

	public char getInitial() {
		return initial;
	}
	
	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	@Override
	public int compareTo(BoardCell cell) {
		if (this.equals(cell)) {
			return 0;
		}
		return -1;
	}

}
