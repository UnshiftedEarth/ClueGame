package experiment;

import java.util.*;

public class TestBoardCell {
	
	
	public TestBoardCell(int row, int column) {
		super();
	}
	
	// Setters and Getters
	public Set<TestBoardCell> getAdjList() {
		return new TreeSet<TestBoardCell>();
	}
	
	public void setRoom(boolean room) {
		
	}
	
	public boolean isRoom() {
		return false;
	}

	public boolean isOccupied() {
		return false;
	}

	public void setOccupied(boolean occupied) {
		
	}

	
}
