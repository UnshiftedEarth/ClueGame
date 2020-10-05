package experiment;

import java.util.*;

public class TestBoard {
	
	
	public TestBoard(int rows, int col) {
		super();
	}

	public void calcTargets(TestBoardCell startCell, int pathlength) {
		
	}
	
	public Set<TestBoardCell> getTargets() {
		return new TreeSet<TestBoardCell>();
	}
	
	public TestBoardCell getCell(int row, int col) {
		return new TestBoardCell(0,0);
	}
}
