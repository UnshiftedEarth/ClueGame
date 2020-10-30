package experiment;

import java.util.*;

public class TestBoardCell implements Comparable<TestBoardCell> {
	
	private int row, col;
	private boolean isOccupied, isRoom;
	private Set<TestBoardCell> adjList;
	
	public TestBoardCell(int row, int col) {
		super();
		this.row = row;
		this.col = col;
		adjList = new HashSet<TestBoardCell>();
	}
	
	// Setters and Getters
	public Set<TestBoardCell> getAdjList() {
		return adjList;
	}
	
	public void addToAdjList(TestBoardCell cell) {
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
	public int compareTo(TestBoardCell cell) {
		if (this.equals(cell)) {
			return 0;
		}
		return -1;
	}
	
}
