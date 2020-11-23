package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.util.*;
import javax.swing.Timer;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class Board extends JPanel implements ActionListener {

	private BoardCell[][] grid;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;

	private String layoutConfigFile;
	private String setupConfigFile;
	
	private static Solution theAnswer;
	private Set<Card> deck;
	private ArrayList<Player> players;
	private Player currentPlayer;
	private Player movingPlayer;
	private Set<Room> targetRooms;

	private Map<Character, Room> roomMap;
	private static Board instance = new Board();

	private int NUM_ROWS = 0;
	private int NUM_COLUMNS = 0;
	
	Timer tm = new Timer(5, this);
	private boolean animate;
	private BoardCell targetCell;


	private Board() {
		super();
		targets = new HashSet<>();
		visited = new HashSet<>();
		roomMap = new HashMap<>();
		deck = new HashSet<>();
		players = new ArrayList<>();
		theAnswer = new Solution();
		this.addMouseListener(new BoardClicked());
		targetRooms = new HashSet();
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
			if (location == players.size()-1) {
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
		//FileReader reader = new FileReader(setupConfigFile); // loads from file
		InputStream reader = this.getClass().getResourceAsStream(setupConfigFile); // loads from package
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
				int rowLocation = Integer.parseInt(lineList[4].trim());
				int colLocation = Integer.parseInt(lineList[5].trim());
				if (playerType.equals("Human")) {
					HumanPlayer human = new HumanPlayer(name, color, rowLocation, colLocation);
					players.add(human);
				}
				else if (playerType.equals("Computer")) {
					players.add(new ComputerPlayer(name, color, rowLocation, colLocation));
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
		//FileReader reader = new FileReader(layoutConfigFile);
		InputStream reader = this.getClass().getResourceAsStream(layoutConfigFile); // loads from package
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
	public boolean checkAccusation(Solution accusation) {
		Card player = accusation.person;
		Card room = accusation.room;
		Card weapon = accusation.weapon;
		
		boolean personB = player.equals(theAnswer.person);
		boolean roomB = room.equals(theAnswer.room);
		boolean weaponB = weapon.equals(theAnswer.weapon);
		
		return personB && roomB && weaponB;
	}
	
	/*
	 * Method to handle a suggestion and cycle through players to disprove
	 */
	public Card handleSuggestion(Player player, Solution suggestion) {
		ArrayList<Player> playerOrder = getPlayerOrder(player);
		for (Player p : playerOrder) {
			if (p.equals(player)) {
				continue;
			}
			Card disprove = p.disproveSuggestion(suggestion);
			if (disprove != null) {
				return disprove;
			}
		}
		return null;
	}
	
	// helper method to return a list of the current order of players
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
	
	/*
	 * this method paints the Clue game board
	 */
	@Override
	public void paintComponent(Graphics g) {
		// gather initial data on board panel
		double width = this.getWidth();
		double height = this.getHeight();
		// paint the board initially black
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, (int) width, (int) height);
		// store all location data in location object
		Location location = new Location(width, height, NUM_ROWS, NUM_COLUMNS);
		
		// set room as target if center cell is target
		for (BoardCell[] cellRow : grid) {
			for (BoardCell cell : cellRow) {
				if (cell.isRoom() && isTarget(cell)) {
					targetRooms.add(roomMap.get(cell.getInitial()));
					roomMap.get(cell.getInitial()).setTarget(true);
				}
			}
		}
		// draw cells
		for (BoardCell[] cellRow : grid) {
			for (BoardCell cell : cellRow) {
				if (isTarget(cell)) {
					cell.draw(g, roomMap, location, true);
				}
				else {
					cell.draw(g, roomMap, location, false);
				}
			}
		}
		// draw the doorways
		for (BoardCell[] cellRow : grid) {
			for (BoardCell cell : cellRow) {
				cell.drawDoor(g, location);
			}
		}
		// draw room names 
		for (Room room : roomMap.values()) {
			room.drawLabel(g, location);
		}
		// draw players
		for (Player player : players) {
			player.draw(g, location);
		}
		for (Player player : players) {
			player.setOffset(false);
		}
	}
	
	// simple method to see if a cell is a target or not
	private boolean isTarget(BoardCell cell) {
		for (BoardCell c : targets) {
			if (c.equals(cell)) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Method called to animate player, triggered by timer for animation
	 */
	public void actionPerformed(ActionEvent e) {
		movingPlayer.setAnimate(true);
		int x = movingPlayer.getX();
		int y = movingPlayer.getY();
		
		BoardCell currentCell = grid[movingPlayer.getRow()][movingPlayer.getColumn()];
		int xSpeed = (int) Math.ceil((targetCell.getX() - currentCell.getX()) / 25.0);
		int ySpeed = (int) Math.ceil((targetCell.getY() - currentCell.getY()) / 25.0);
		
		if ((xSpeed > 0 && x >= targetCell.getX()) || (xSpeed < 0 && x <= targetCell.getX())) {
			stopTimer();
			return;
		}
		else if ((ySpeed > 0 && y >= targetCell.getY()) || (ySpeed < 0 && y <= targetCell.getY())) {
			stopTimer();
			return;
		}
		
		movingPlayer.setX(x+xSpeed);
		movingPlayer.setY(y+ySpeed);
		repaint();
		
	}

	/*
	 * Method used to stop the timer and continue game logic
	 */
	private void stopTimer() {
		movingPlayer.setAnimate(false);
		currentPlayer.setAnimate(false);
		movingPlayer.setLocation(targetCell.getRow(), targetCell.getCol());
		targetCell.setOccupied(true);
		repaint();
		if (movingPlayer.equals(currentPlayer)) {
			if (currentPlayer instanceof HumanPlayer) {
				if (currentPlayer.isInRoom()) {
					handleHumanSuggestion(targetCell);
				}
				currentPlayer.setFinished(true);
			}
			else {
				makeComputerSuggestion();
			}
		}
		
		tm.stop();
	}
	
	/*
	 * private mouse listener class for clicking on the board
	 */
	private class BoardClicked implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (!(currentPlayer instanceof HumanPlayer)) {
				return;
			}
			BoardCell clickedCell = getMouseCell();
			if (!isTarget(clickedCell)) {
				JOptionPane.showMessageDialog(instance, "That is not a target", "Error", 1);
				return;
			}
			
			currentPlayer.setSubmitState(false);
			targets.clear();
			clearTargetRooms();
			repaint();
			setupPlayerAnimation(clickedCell, currentPlayer);
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}
	}
	
	// clears the target rooms set
	private void clearTargetRooms() {
		for (Room room : targetRooms) {
			room.setTarget(false);
		}
		targetRooms.clear();
	}

	/*
	 * Method that setups up the player animation, starts the timer
	 */
	private void setupPlayerAnimation(BoardCell targetCell, Player player) {
		if (targetCell == null) {
			return;
		}
		this.targetCell = targetCell;
		this.movingPlayer = player;
		BoardCell currentCell = grid[player.getRow()][player.getColumn()];
		currentCell.setOccupied(false);
		// start the timer if player moves to another cell
		if (!targetCell.equals(currentCell)) {
			SwingUtilities.invokeLater(() -> tm.start());
		}
		else {
			currentCell.setOccupied(true);
			if (currentPlayer instanceof ComputerPlayer) {
				makeComputerSuggestion();
			}
			// if human player
			else {
				repaint();
				if (currentPlayer.isInRoom()) {
					handleHumanSuggestion(targetCell);
				}
				currentPlayer.setFinished(true);
			}
		}
		
	}
	
	// method to return the cell that the mouse clicked
	private BoardCell getMouseCell() {
		int x = getMousePosition().x;
		int y = getMousePosition().y;
		for (BoardCell[] cellRow : grid) {
			for (BoardCell cell : cellRow) {
				// if player clicked on a room cell then direct to center cell
				if (cell.containsClick(x, y) && cell.isRoom()) {
					return roomMap.get(cell.getInitial()).getCenterCell();
				}
				else if (cell.containsClick(x, y)) {
					return cell;
				}
			}
		}
		return null;
	}
	
	// This method displays the suggestion options
	public void handleHumanSuggestion(BoardCell cell) {
		if (!currentPlayer.isSubmitState()) {
			String room = roomMap.get(cell.getInitial()).getName();
			SuggestionFrame frame = new SuggestionFrame(room);
			frame.setLocationRelativeTo(this);
			frame.setModal(true);
			frame.setVisible(true);
		}
	}
	
	// Method that runs when the user wishes to make an Accusation
	public void buttonMakeAccusation() {
		if (currentPlayer instanceof HumanPlayer && !currentPlayer.isFinished()) {
			AccusationFrame frame = new AccusationFrame();
			frame.setLocationRelativeTo(this);
			frame.setModal(true);
			frame.setVisible(true);
		}
		else {
			JOptionPane.showMessageDialog(this, "It is not your turn");
		}
	}
	
	/*
	 * Submit button method that is called when human makes a suggestion
	 */
	public void buttonMakeSuggestion(Solution suggestion) {
		// find suggested player
		currentPlayer.setSubmitState(true);
		BoardCell currentPlayerCell = grid[currentPlayer.getRow()][currentPlayer.getColumn()];
		Player suggested = null;
		for (Player player : players) {
			if (player.getName().equals(suggestion.person.getName())) {
				suggested = player;
				break;
			}
		}

		// if suggested player is moved to a different room, set a flag
		BoardCell suggestedPlayerCell = grid[suggested.getRow()][suggested.getColumn()];
		suggestedPlayerCell.setOccupied(false);
		if (!suggestedPlayerCell.equals(currentPlayerCell)) {
			suggested.setRoomTarget(true);
		}

		if (!suggested.equals(currentPlayer)) {
			setupPlayerAnimation(currentPlayerCell, suggested);
		}

		Card disprove = handleSuggestion(currentPlayer, suggestion);

		ClueGame.setGuess(suggestion, currentPlayer);
		ClueGame.setResult(disprove, currentPlayer);

		// find the player who disproved the suggestion
		if (disprove != null) {
			for (Player player : players) {
				if (player.hasCard(disprove)) {
					ClueGame.addToSeen(disprove, player);
					break;
				}
			}
		}
	}
	
	/*
	 * Method that runs when the next button is pressed
	 */
	public void buttonNext() {
		if (tm.isRunning()) {
			return;
		}
		if (currentPlayer instanceof HumanPlayer && !currentPlayer.isFinished()) {
			JOptionPane.showMessageDialog(this, "Please Finish Your Turn", "Error", 1);
			return;
		}
		ArrayList<Player> playerOrder = getPlayerOrder(currentPlayer);
		currentPlayer = playerOrder.get(1); // get next player
		currentPlayer.setFinished(false);
		ClueGame.rollDice();
		BoardCell currentCell = grid[currentPlayer.getRow()][currentPlayer.getColumn()];
		calcTargets(currentCell, ClueGame.getRoll());
		
		// if player was moved by another player, add room to target
		if (currentPlayer.isRoomTarget()) {
			targets.add(grid[currentPlayer.getRow()][currentPlayer.getColumn()]);
		}
		currentPlayer.setRoomTarget(false);
		ClueGame.setTurn(currentPlayer);
		// if current Player is human
		if (currentPlayer instanceof HumanPlayer) {
			repaint();
			if (targets.size() == 0) {
				currentPlayer.setFinished(true);
				JOptionPane.showMessageDialog(this, "No move is avaliable", "No move", 1);
			}
			else {
				currentPlayer.setFinished(false);
			}
			return;
		}
		
		// decide if computer should make accusation
		makeComputerAccusation();
		ComputerPlayer comp = (ComputerPlayer) currentPlayer;
		BoardCell target = comp.selectTarget(targets);
		setupPlayerAnimation(target, currentPlayer);
		
		// clear the targets after moving player
		targets.clear();
		clearTargetRooms();
	}
	
	// Method for deciding if computer wins or not
	private void makeComputerAccusation() {
		ComputerPlayer comp = (ComputerPlayer) currentPlayer;
		Solution accusation = comp.getAccusation();
		if (accusation.equals(theAnswer)) {
			String end1 = "The player " + comp.getName() + " has guessed the correct solution";
			String end2 = "\nThe answer was: " + theAnswer.person.getName() + " with the " + 
			theAnswer.weapon.getName() + " in " + theAnswer.room.getName();
			JOptionPane.showMessageDialog(this, end1 + end2, "You Lose", 1);
			System.exit(0);
		}
	}

	/*
	 * Flow method for computer making a suggestion and updating GUI
	 */
	private void makeComputerSuggestion() {
		BoardCell currentPlayerCell = grid[currentPlayer.getRow()][currentPlayer.getColumn()];
		if (currentPlayerCell.isRoomCenter() && !currentPlayer.isFinished()) {
			currentPlayer.setFinished(true);
			ComputerPlayer comp = (ComputerPlayer) currentPlayer;
			Solution suggestion = comp.createSuggestion();
			String name = suggestion.person.getName();
			Player suggested = null;
			for (Player player : players) {
				if (player.getName().equals(name)) {
					suggested = player;
					break;
				}
			}
			
			// if suggested player is moved to a different room, set a flag
			BoardCell suggestedPlayerCell = grid[suggested.getRow()][suggested.getColumn()];
			suggestedPlayerCell.setOccupied(false);
			if (!suggestedPlayerCell.equals(currentPlayerCell)) {
				suggested.setRoomTarget(true);
			}
			
			// don't move the player if the suggested player is the current player
			if (!suggested.equals(currentPlayer)) {
				setupPlayerAnimation(currentPlayerCell, suggested);
			}
			Card disprove = handleSuggestion(comp, suggestion);
			
			// if no players can disprove set a possible accusation
			if (disprove == null) {
				comp.setAccusation(suggestion);
			}
			else {
				comp.updateSeen(disprove);
			}
			ClueGame.setGuess(suggestion, comp);
			ClueGame.setResult(disprove, comp);
		}
		else {
			ClueGame.clearGuess();
			ClueGame.clearResult();
		}
	}
	
	// Method to start the game
	public void playGame() {
		setOccupiedCells();
		currentPlayer = players.get(0);
		ClueGame.rollDice();
		ClueGame.setTurn(currentPlayer);
		ClueGame.setCards(currentPlayer.getHand(), currentPlayer);
		BoardCell currentCell = grid[currentPlayer.getRow()][currentPlayer.getColumn()];
		calcTargets(currentCell, ClueGame.getRoll());
		repaint();
	}
	
	// Display the human losing
	public void displayLoss() {
		String end1 = "That is not correct, You lose.";
		String end2 = "\nThe correct solution is " + theAnswer.person.getName() + " with the " +
		theAnswer.weapon.getName() + " in " + theAnswer.room.getName();
		JOptionPane.showMessageDialog(this, end1 + end2, "You Lose", 1);
		System.exit(0);
	}
	
	// display the human winning
	public void displayWin() {
		JOptionPane.showMessageDialog(this, "Congratulations, You Win!", "You Win", 1);
		System.exit(0);
	}
	
	// Simple helper method to set all players cells to occupied at the start
	private void setOccupiedCells() {
		for (Player player : players) {
			grid[player.getRow()][player.getColumn()].setOccupied(true);
		}
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
	
	public Player getHuman() {
		for (Player player : players) {
			if (player instanceof HumanPlayer) {
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

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public int getNumRows() {
		return NUM_ROWS;
	}

	public int getNumColumns() {
		return NUM_COLUMNS;
	}

	public static Solution getTheAnswer() {
		return theAnswer;
	}

	public void setConfigFiles(String csv, String txt) {
		layoutConfigFile = "/data/" + csv;
		setupConfigFile = "/data/" + txt;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

}
