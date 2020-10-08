package clueGame;

import java.util.HashSet;
import java.util.Set;


public class BoardCell implements Comparable<BoardCell> {
	
	private int row, col;
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
	
	public boolean isRoom() {
		return isRoom;
	}

	public boolean isOccupied() {
		return isOccupied;
	}

	public void setOccupied(boolean occupied) {
		isOccupied = occupied;
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
