package experiment;

import java.util.*;

public class TestBoard {
	
	private TestBoardCell[][] board;
	private Set<TestBoardCell> targets;
	private Set<TestBoardCell> visited;
	
	final static int ROWS = 4;
	final static int COLS = 4;
	
	
	public TestBoard() {
		super();
		board = new TestBoardCell[ROWS][COLS];
		targets = new HashSet<TestBoardCell>();
		visited = new HashSet<TestBoardCell>();
		setup();
	}
	
	/*
	 * setup method for initializing the board and creating 
	 * the adjacency lists for each cell
	 */
	public void setup() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				TestBoardCell cell = new TestBoardCell(i, j);
				board[i][j] = cell;
			}
		}
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				TestBoardCell cell = board[i][j];
				if(i + 1 < ROWS) {
					cell.addToAdjList(board[i+1][j]);
				}
				if(i - 1 >= 0) {
					cell.addToAdjList(board[i-1][j]);
				}
				if(j + 1 < COLS) {
					cell.addToAdjList(board[i][j+1]);
				}
				if(j - 1 >= 0) {
					cell.addToAdjList(board[i][j-1]);
				}
			}
		}
	}
	
	public void calcTargets(TestBoardCell startCell, int pathlength) {
		visited.clear();
		targets.clear();
		visited.add(startCell);
		findAllTargets(startCell, pathlength);
	}
	
	public void findAllTargets(TestBoardCell startCell, int length) {
		for (TestBoardCell cell : startCell.getAdjList()) {
			if (visited.contains(cell) || cell.isOccupied()) {
				continue;
			}
			visited.add(cell);
			if (length == 1 || cell.isRoom()) {
				targets.add(cell);
			}
			else {
				findAllTargets(cell, length-1);
			}
			visited.remove(cell);
		}
	}
	
	public Set<TestBoardCell> getTargets() {
		return targets;
	}
	
	public TestBoardCell getCell(int row, int col) {
		return board[row][col];
	}
}
