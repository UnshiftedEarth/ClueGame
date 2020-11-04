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
	
	private static Solution theAnswer;
	private Set<Card> deck;
	private ArrayList<Player> players;

	private Map<Character, Room> roomMap;
	private static Board instance = new Board();

	private int NUM_ROWS = 0;
	private int NUM_COLUMNS = 0;
	
	


	private Board() {
		super();
		targets = new HashSet<>();
		visited = new HashSet<>();
		roomMap = new HashMap<>();
		deck = new HashSet<>();
		players = new ArrayList<>();
		theAnswer = new Solution();
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
			return;
		}
		// door lists must be created before calculating adjacencies
		calcDoorLists();
		calcAdjacencies();
		dealCards();
	}
	
	/*
	 * Setup method for creating a list of all doors that correspond to each room
	 */
	private void calcDoorLists() {
		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_COLUMNS; j++) {
				addToDoorList(i, j);
			}
		}
	}
	
	// method for adding a cell to a room's door list
	private void addToDoorList(int i, int j) {
		BoardCell cell = grid[i][j];
		DoorDirection door = cell.getDoorDirection();
		switch (door) {
		case NONE:
			return;
		case UP:
			addDoorList(i-1, j, cell);
			break;
		case DOWN:
			addDoorList(i+1, j, cell);
			break;
		case RIGHT:
			addDoorList(i, j+1, cell);
			break;
		case LEFT:
			addDoorList(i, j-1, cell);
			break;
		}
	}

	private void addDoorList(int i, int j, BoardCell cell) {
		BoardCell temp = grid[i][j]; 
		Room room = roomMap.get(temp.getInitial());
		room.addToDoorList(cell);
	}
	
	
	/*
	 * Setup method to create adjacency lists for all board cells
	 */
	private void calcAdjacencies() {
		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_COLUMNS; j++) {
				BoardCell cell = grid[i][j];
				Room room = roomMap.get(cell.getInitial());
				if(i + 1 < NUM_ROWS) {
					BoardCell down = grid[i+1][j];
					checkAdjacent(cell, down, DoorDirection.DOWN);
				}
				if(i - 1 >= 0) {
					BoardCell up = grid[i-1][j];
					checkAdjacent(cell, up, DoorDirection.UP);
				}
				if(j + 1 < NUM_COLUMNS) {
					BoardCell right = grid[i][j+1];
					checkAdjacent(cell, right, DoorDirection.RIGHT);
				}
				if(j - 1 >= 0) {
					BoardCell left = grid[i][j-1];
					checkAdjacent(cell, left, DoorDirection.LEFT);
				}
				if (cell.isRoomCenter()) {
					checkAdjacencyRoomCenter(cell, room);
				}
			}
		}
	}
	
	// method to check adjacencies of room centers
	private void checkAdjacencyRoomCenter(BoardCell cell, Room room) {
		for (BoardCell c : room.getDoorList()) {
			if (!c.isOccupied()) {
				cell.addToAdjList(c);
			}
		}
		if (room.getSecretPassage() != null) {
			cell.addToAdjList(room.getSecretPassage().getCenterCell());
		}
	}
	
	// method to check adjacencies of neighboring cells
	private void checkAdjacent(BoardCell cell, BoardCell next, DoorDirection direction) {
		Room nextRoom = roomMap.get(next.getInitial());
		String name = nextRoom.getName();
		if (next.isRoom() && cell.getDoorDirection() == direction) {
			cell.addToAdjList(nextRoom.getCenterCell());
		}
		else if (!cell.isRoom() && (name.equals("Walkway") || name.equals("Hallway"))) {
			cell.addToAdjList(next);
		}
	}

	// method to load both config files
	public void loadConfigFiles() throws BadConfigFormatException, FileNotFoundException {
		loadSetupConfig();
		loadLayoutConfig();
	}
	
	/*
	 * This method deals cards from the deck to the solution class and each player
	 */
	public void dealCards() {
		Random random = new Random();
		ArrayList<Card> deckCopy =  new ArrayList<>();
		ArrayList<Player> playersCopy =  new ArrayList<>();
		deckCopy.addAll(deck);
		playersCopy.addAll(players);
		int rand = 0;
		
		//deal solution cards 
		theAnswer.person = randomSolutionCard(deckCopy, CardType.PLAYER);
		theAnswer.weapon = randomSolutionCard(deckCopy, CardType.WEAPON);
		theAnswer.room = randomSolutionCard(deckCopy, CardType.ROOM);
		
		// deal cards in deck to all players randomly
		int size = deckCopy.size(); 
		int location = 0;
		for (int i = 0; i < size; i++) {
			rand = random.nextInt(deckCopy.size());
			playersCopy.get(location).updateHand(deckCopy.remove(rand));
			if (location == 5) {
				location = -1;
			}
			location++;
		}
	}
	
	// helper method to find a random card for a specified type
	private Card randomSolutionCard (ArrayList<Card> deck, CardType type) {
		Random random = new Random();
		int rand = random.nextInt(deck.size());
		while (deck.get(rand).getType() != type) {
			rand = random.nextInt(deck.size());
		}
		return deck.remove(rand);
	}
	
	
	/*
	 * this method loads in the room, weapons and player data from the setup text file
	 */
	public void loadSetupConfig() throws FileNotFoundException, BadConfigFormatException {
		FileReader reader = new FileReader(setupConfigFile);
		Scanner scanner = new Scanner(reader);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.substring(0, 2).equals("//")) {
				continue;
			}
			String[] lineList = line.split(",");
			String type = lineList[0];
			if (!type.equals("Room") && !type.equals("Space") && !type.equals("Player") && !type.equals("Weapon")) {
				scanner.close();
				throw new BadConfigFormatException("Error: Setup file is not in proper format");
			}
			else if (type.equals("Space")) {
				String roomName = lineList[1].trim();
				loadSpace(lineList, roomName);
			}
			else if (type.equals("Room")) {
				String roomName = lineList[1].trim();
				loadSpace(lineList, roomName);
				deck.add(new Card(roomName, CardType.ROOM));
			}
			else if (type.equals("Player")) {
				String playerType = lineList[1].trim();
				String color = lineList[3].trim();
				String name = lineList[2].trim();
				if (playerType.equals("Human")) {
					players.add(new HumanPlayer(name, color));
				}
				else if (playerType.equals("Computer")) {
					players.add(new ComputerPlayer(name, color));
				}
				else {
					scanner.close();
					throw new BadConfigFormatException("Error: Setup file is not in proper format");
				}
				deck.add(new Card(name, CardType.PLAYER));
			}
			else if (type.equals("Weapon")) {
				String weaponName = lineList[1].trim();
				deck.add(new Card(weaponName, CardType.WEAPON));
			}
		}
		scanner.close();
	}
	
	// help method to load room and space
	private void loadSpace(String[] lineList, String roomName) {
		char symbol = lineList[2].trim().charAt(0);
		Room room = new Room(roomName);
		roomMap.put(symbol, room);
	}
	
	
	/*
	 * This method loads in the board from the layout csv file
	 */
	public void loadLayoutConfig() throws BadConfigFormatException, FileNotFoundException {
		// reads in the board csv file and stores in temporary board ArrayList matrix
		ArrayList<List<String>> tempBoard = new ArrayList<>();
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
				scanner.close();
				throw new BadConfigFormatException(message);
			}
			numCols = lineList.size();
		}
		scanner.close();
		
		// set number of rows and columns based on ArrayList matrix
		NUM_ROWS = tempBoard.size();
		NUM_COLUMNS = tempBoard.get(0).size();
		// allocate space for grid 
		grid = new BoardCell[NUM_ROWS][NUM_COLUMNS];
		
		initializeBoard(tempBoard);
	}
	
	/*
	 * method for initializing the board with BoardCells
	 */
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
	
	/*
	 * method for adjusting cell private variables depending on symbol from config
	 */
	private void changeCellAttributes(BoardCell cell, String entry) throws BadConfigFormatException {
		Room room = roomMap.get(entry.charAt(0));
		cell.setInitial(entry.charAt(0));
		if (entry.length() == 1) {
			// if symbol has only one character
			String name = room.getName();
			if (!name.equals("Unused") && !name.equals("Walkway") && !name.equals("Hallway")) {
				cell.setRoom(true);
			}
			cell.setDoorDirection(DoorDirection.NONE);
		}
		else {
			// if symbol has 2 characters
			String secondEntry = entry.substring(1,2);
			switch (secondEntry) {
			case "^":
				cell.setDoorDirection(DoorDirection.UP);
				break;
			case ">":
				cell.setDoorDirection(DoorDirection.RIGHT);
				break;
			case "v": 
				cell.setDoorDirection(DoorDirection.DOWN);
				break;
			case "<":
				cell.setDoorDirection(DoorDirection.LEFT);
				break;
			case "*":
				cell.setRoom(true);
				cell.setRoomCenter(true);
				cell.setDoorDirection(DoorDirection.NONE);
				room.setCenterCell(cell);
				break;
			case "#":
				cell.setRoom(true);
				cell.setRoomLabel(true);
				cell.setDoorDirection(DoorDirection.NONE);
				room.setLabelCell(cell);
				break;
			default: 
				// if symbol is not a doorway or room center/label cell
				// is a secret passage or an exception
				char secondChar = secondEntry.charAt(0);
				if (roomMap.containsKey(secondChar)) {
					Room secret = roomMap.get(secondChar);
					room.setSecretPassage(secret);
					cell.setSecretPassage(secondChar);
					cell.setRoom(true);
					cell.setDoorDirection(DoorDirection.NONE);
				}
				else {
					throw new BadConfigFormatException("Error: Layout File is not in proper format");
				}
				break;
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
			if (visited.contains(cell) || !cell.isRoomCenter() && cell.isOccupied()) {
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
	
	// clears the deck and players
	public void clearDeck() {
		deck.clear();
		theAnswer = new Solution();
		players.clear();
	}
	
	/*
	 * Method to check the accusation with the solution, parameters must be in 
	 * player, room, weapon format
	 */
	public boolean checkAccusation(Card player, Card room, Card weapon) {
		boolean personB = player.equals(theAnswer.person);
		boolean roomB = room.equals(theAnswer.room);
		boolean weaponB = weapon.equals(theAnswer.weapon);
		
		return personB && roomB && weaponB;
	}

	public Card handleSuggestion(Player player, Card person, Card room, Card weapon) {
		ArrayList<Player> playerOrder = getPlayerOrder(player);
		for (Player p : playerOrder) {
			if (p.equals(player)) {
				continue;
			}
			Card disprove = p.disproveSuggestion(person, room, weapon);
			if (disprove != null) {
				return disprove;
			}
		}
		return null;
	}
	
	private ArrayList<Player> getPlayerOrder(Player player) {
		ArrayList<Player> order = new ArrayList<>();
		int j = players.indexOf(player);
		for (int i = 0; i < players.size(); i++) {
			order.add(players.get(j));
			if (j == players.size()-1) {
				j = -1;
			}
			j++;
		}
		return order;
	}

	//setters and getters
	public Set<BoardCell> getTargets() {
		return targets;
	}

	public BoardCell getCell(int row, int col) {
		return grid[row][col];
	}
	
	public Card getCard(String x) {
		for (Card card : deck) {
			if (card.getName().equals(x)) {
				return card;
			}
		}
		return null;
	}
	
	public Player getPlayer(String x) {
		for (Player player : players) {
			if (player.getName().equals(x)) {
				return player;
			}
		}
		return null;
	}
	
	public Room getRoom(char room) {
		return roomMap.get(room);
	}
	
	public Room getRoom(BoardCell cell) {
		return roomMap.get(cell.getInitial());
	}
	
	public Set<BoardCell> getAdjList(int r, int c) {
		BoardCell cell = grid[r][c];
		return cell.getAdjList();
	}
	
	public Set<Card> getDeck() {
		return deck;
	}
	
	public Solution getSolution() {
		return theAnswer;
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
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

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

}
