package clueGame;

import java.util.*;


public class Board {

	private BoardCell[][] grid;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;

	private String layoutConfigFile;
	private String setupConfigFile;

	private Map<Character, Room> roomMap;
	private static Board instance;

	final int NUM_ROWS= 4;
	final int NUM_COLUMNS = 4;


	public Board() {
		super();
		grid = new BoardCell[NUM_ROWS][NUM_COLUMNS];
		targets = new HashSet<BoardCell>();
		visited = new HashSet<BoardCell>();
		boardSetup();
	}

	/*
	 * setup method for initializing the board and creating 
	 */
	private void boardSetup() {
		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_COLUMNS; j++) {
				BoardCell cell = new BoardCell(i, j);
				grid[i][j] = cell;
			}
		}
		createAdjLists();
	}
	
	/*
	 * setup method to create adjacency lists for all board cells
	 */
	private void createAdjLists() {
		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_COLUMNS; j++) {
				BoardCell cell = grid[i][j];
				if(i + 1 < NUM_ROWS) {
					cell.addToAdjList(grid[i+1][j]);
				}
				if(i - 1 >= 0) {
					cell.addToAdjList(grid[i-1][j]);
				}
				if(j + 1 < NUM_COLUMNS) {
					cell.addToAdjList(grid[i][j+1]);
				}
				if(j - 1 >= 0) {
					cell.addToAdjList(grid[i][j-1]);
				}
			}
		}
	}
	
	public void initialize() {
		// not written yet
	}

	public void calcTargets(BoardCell startCell, int pathlength) {
		visited.clear();
		targets.clear();
		visited.add(startCell);
		findAllTargets(startCell, pathlength);
	}

	public void findAllTargets(BoardCell startCell, int length) {
		for (BoardCell cell : startCell.getAdjList()) {
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

	public void loadConfigFiles() {
		// not written yet
	}

	public void loadSetupConfig() {
		// not written yet
	}
	
	public void loadLayoutConfig() {
		// not written yet
	}

	//setters and getters
	public Set<BoardCell> getTargets() {
		return targets;
	}

	public BoardCell getCell(int row, int col) {
		return grid[row][col];
	}

	public Map<Character, Room> getRoomMap() {
		return roomMap;
	}

	public static Board getInstance() {
		return instance;
	}

	public int getNumRows() {
		return NUM_ROWS;
	}

	public int getNumColumns() {
		return NUM_COLUMNS;
	}

	public void setConfigFiles(String csv, String txt) {
		layoutConfigFile = csv;
		setupConfigFile = txt;
	}
	
}
