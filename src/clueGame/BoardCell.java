package clueGame;

import java.util.HashSet;
import java.util.Set;


public class BoardCell implements Comparable<BoardCell> {
	
	private int row, col;
	private char initial, secretPassage;
	private DoorDirection doorDirection;
	private boolean roomLabel, roomCenter;
	private boolean isOccupied, isRoom;
	private Set<BoardCell> adjList;
	
	
	public BoardCell(int row, int col) {
		super();
		this.row = row;
		this.col = col;
		adjList = new HashSet<BoardCell>();
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
	
	public boolean isDoorway() {
		//TODO not written yet
		return false;
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
