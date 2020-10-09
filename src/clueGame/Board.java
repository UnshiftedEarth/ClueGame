package clueGame;

import java.util.*;


public class Board {

	private BoardCell[][] grid;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;

	private String layoutConfigFile;
	private String setupConfigFile;

	private Map<Character, Room> roomMap;
	private static Board instance = new Board();

	final int NUM_ROWS = 100;
	final int NUM_COLUMNS = 100;


	private Board() {
		super();
		grid = new BoardCell[NUM_ROWS][NUM_COLUMNS];
		targets = new HashSet<BoardCell>();
		visited = new HashSet<BoardCell>();
		roomMap = new HashMap<Character, Room>();
	}

	/*
	 * setup method for initializing the board
	 */
	public void initialize() {
		loadConfigFiles();
		loadSetupConfig();
		loadLayoutConfig();
		//createAdjLists();
		
		//TODO modify this when implementing code
		//temporary setup
		tempSetup();
	}
	
	//temporary setup method until initializations of files
	public void tempSetup() {
		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_COLUMNS; j++) {
				BoardCell cell = new BoardCell(i, j);
				grid[i][j] = cell;
			}
		}
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

	/*
	 * methods for calculating valid spaces to move to
	 */
	public void calcTargets(BoardCell startCell, int pathlength) {
		visited.clear();
		targets.clear();
		visited.add(startCell);
		findAllTargets(startCell, pathlength);
	}
	
	/*
	 * recursive method for calculating valid spaces to move to
	 */
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
		//TODO not written yet
	}

	public void loadSetupConfig() {
		//TODO not written yet
	}
	
	public void loadLayoutConfig() {
		//TODO not written yet
	}

	//setters and getters
	public Set<BoardCell> getTargets() {
		return targets;
	}

	public BoardCell getCell(int row, int col) {
		return grid[row][col];
	}
	
	public Room getRoom(char room) {
		//TODO not written yet
		return new Room("");
	}
	
	public Room getRoom(BoardCell cell) {
		//TODO not written yet
		return new Room("");
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
