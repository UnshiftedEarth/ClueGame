package clueGame;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;


public class Board {

	private BoardCell[][] grid;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;

	private String layoutConfigFile;
	private String setupConfigFile;

	private Map<Character, Room> roomMap;
	private static Board instance = new Board();

	private int NUM_ROWS = 0;
	private int NUM_COLUMNS = 0;


	private Board() {
		super();
		targets = new HashSet<BoardCell>();
		visited = new HashSet<BoardCell>();
		roomMap = new HashMap<Character, Room>();
	}

	/*
	 * setup method for initializing the board
	 */
	public void initialize() {
		// must load config files first before creating adjLists
		try {
			loadConfigFiles();
		} catch (BadConfigFormatException | FileNotFoundException e) {
			e.printStackTrace();
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
	private void findAllTargets(BoardCell startCell, int length) {
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

	public void loadConfigFiles() throws BadConfigFormatException, FileNotFoundException {
		loadSetupConfig();
		loadLayoutConfig();
	}

	@SuppressWarnings("resource")
	public void loadSetupConfig() throws FileNotFoundException, BadConfigFormatException {
		// read in setup txt file
		FileReader reader = new FileReader(setupConfigFile);
		Scanner scanner = new Scanner(reader);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.substring(0, 2).equals("//")) {
				continue;
			}
			String[] lineList = line.split(",");
			if (!lineList[0].equals("Room") && !lineList[0].equals("Space")) {
				throw new BadConfigFormatException("Error: Setup file is not in proper format");
			}
			String roomName = lineList[1].trim();
			char symbol = lineList[2].trim().charAt(0);
			Room room = new Room(roomName);
			roomMap.put(symbol, room);
		}
	}
	
	
	@SuppressWarnings("resource")
	public void loadLayoutConfig() throws BadConfigFormatException, FileNotFoundException {
		// reads in the board csv file and stores in temporary board ArrayList matrix
		ArrayList<List<String>> tempBoard = new ArrayList<List<String>>();
		int numCols = 0;
			FileReader reader = new FileReader(layoutConfigFile);
		Scanner scanner = new Scanner(reader);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			List<String> lineList = Arrays.asList(line.split(","));
			tempBoard.add(lineList);
			if (numCols != 0 && lineList.size() != numCols) {
				String message = "Error: The board layout file does "
					+ "not have the same number of columns in every row";
				throw new BadConfigFormatException(message);
			}
			numCols = lineList.size();
		}
		
		// set number of rows and columns based on ArrayList matrix
		NUM_ROWS = tempBoard.size();
		NUM_COLUMNS = tempBoard.get(0).size();
		// allocate space for grid 
		grid = new BoardCell[NUM_ROWS][NUM_COLUMNS];
		
		initializeBoard(tempBoard);
	}

	private void initializeBoard(ArrayList<List<String>> tempBoard) throws BadConfigFormatException {
		// initialize the board with board cells
		for (int i = 0; i < tempBoard.size(); i++) {
			for (int j = 0; j < tempBoard.get(i).size(); j++) {
				BoardCell cell = new BoardCell(i,j);
				grid[i][j] = cell;
				String entry = tempBoard.get(i).get(j);
				if (!roomMap.containsKey(entry.charAt(0))) {
					throw new BadConfigFormatException("Error: Layout refers to room not in setup file");
				}
				changeCellAttributes(cell, entry);
			}
		}
	}

	private void changeCellAttributes(BoardCell cell, String entry) {
		// adjust cell private variables depending on symbol from config
		Room room = roomMap.get(entry.charAt(0));
		cell.setInitial(entry.charAt(0));
		if (entry.length() == 1) {
			// if symbol has only one character
			if (!room.getName().equals("Unused") && !room.getName().equals("Walkway")) {
				cell.setRoom(true);
			}
			else {
				cell.setRoom(false);
			}
			cell.setRoomCenter(false);
			cell.setRoomLabel(false);
			cell.setDoorDirection(DoorDirection.NONE);
		}
		else {
			// if symbol has 2 characters
			String second = entry.substring(1,2);
			switch (second) {
			case "^":
				cell.setDoorDirection(DoorDirection.UP);
				setCellNotRoom(cell);
				break;
			case ">":
				cell.setDoorDirection(DoorDirection.RIGHT);
				setCellNotRoom(cell);
				break;
			case "v": 
				cell.setDoorDirection(DoorDirection.DOWN);
				setCellNotRoom(cell);
				break;
			case "<":
				cell.setDoorDirection(DoorDirection.LEFT);
				setCellNotRoom(cell);
				break;
			case "*":
				cell.setRoom(true);
				cell.setRoomCenter(true);
				cell.setRoomLabel(false);
				cell.setDoorDirection(DoorDirection.NONE);
				room.setCenterCell(cell);
				break;
			case "#":
				cell.setRoom(true);
				cell.setRoomCenter(false);
				cell.setRoomLabel(true);
				cell.setDoorDirection(DoorDirection.NONE);
				room.setLabelCell(cell);
				break;
			default: 
				if (roomMap.containsKey(second.charAt(0))) {
					cell.setSecretPassage(second.charAt(0));
					cell.setRoom(true);
					cell.setRoomLabel(false);
					cell.setRoomCenter(false);
					cell.setDoorDirection(DoorDirection.NONE);
				}
				else {
					// throw exception
				}
				break;
			}
		}
		cell.setOccupied(false);
	}

	private void setCellNotRoom(BoardCell cell) {
		// sets cell to not a room
		cell.setRoom(false);
		cell.setRoomLabel(false);
		cell.setRoomCenter(false);
	}
	

	//setters and getters
	public Set<BoardCell> getTargets() {
		return targets;
	}

	public BoardCell getCell(int row, int col) {
		return grid[row][col];
	}
	
	public Room getRoom(char room) {
		return roomMap.get(room);
	}
	
	public Room getRoom(BoardCell cell) {
		return roomMap.get(cell.getInitial());
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
		layoutConfigFile = "./data/" + csv;
		setupConfigFile = "./data/" + txt;
	}
	
}
