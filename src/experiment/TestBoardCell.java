package experiment;

import java.util.*;

public class TestBoardCell implements Comparable<TestBoardCell> {
	
	
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

	@Override
	public int compareTo(TestBoardCell cell) {
		if (this.equals(cell)) {
			return 0;
		}
		return -1;
	}
	
}
