package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class BoardCell implements Comparable<BoardCell> {
	
	private int row;
	private int col;
	private char initial;
	private char secretPassage;
	private DoorDirection doorDirection;
	private boolean roomLabel;
	private boolean roomCenter;
	private boolean isOccupied;
	private boolean isRoom;
	private Set<BoardCell> adjList;
	
	
	public BoardCell(int row, int col) {
		super();
		this.row = row;
		this.col = col;
		adjList = new HashSet<>();
	}
	
	// Method for each boardcell to draw itself
	public void draw(Graphics g, Map<Character, Room> roomMap, Location loc) {
		String name = roomMap.get(initial).getName();
		if (name.equals("Unused")) {
			return;
		}
		
		int w = loc.getCellWidth();
		int h = loc.getCellHeight();
		int x = loc.calcX(this);
		int y = loc.calcY(this); // change to pass in this object
		
		if (isRoom) {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(x + loc.spacing/2, y + loc.spacing/2, w, h);
		}
		else {
			g.setColor(new Color(255, 230, 0)); // yellowish color
			x += loc.spacing;
			y += loc.spacing;
			w -= loc.spacing;
			h -= loc.spacing;
			g.fillRect(x, y, w, h);
		}
	}
	
	public void drawDoor(Graphics g, Location loc) {
		if (doorDirection == DoorDirection.NONE) {
			return;
		}
		
		int w = loc.getCellWidth();
		int h = loc.getCellHeight();
		int x = loc.calcX(this);
		int y = loc.calcY(this);
		x += loc.spacing;
		y += loc.spacing;
		w -= loc.spacing;
		h -= loc.spacing;
		
		g.setColor(Color.BLUE);
		switch (doorDirection) {
		case UP: 
			g.fillRect(x-loc.spacing/2, y-loc.getDoorHeight(), w+loc.spacing, loc.getDoorHeight());
			break;
		case DOWN:
			g.fillRect(x-loc.spacing/2, y+h, w+loc.spacing, loc.getDoorHeight());
			break;
		case LEFT:
			g.fillRect(x-loc.getDoorHeight(), y-loc.spacing/2, loc.getDoorHeight(), h+loc.spacing);
			break;
		case RIGHT:
			g.fillRect(x+w, y-loc.spacing/2, loc.getDoorHeight(), h+loc.spacing);
		}
	}
	
	
	// Setters and Getters
	public Set<BoardCell> getAdjList() {
		return adjList;
	}
	
	public void addToAdjList(BoardCell cell) {
		adjList.add(cell);
	}
	
	public void setRoom(boolean room) {
		isRoom = room;
	}
	
	public void setInitial(char initial) {
		this.initial = initial;
	}

	public void setSecretPassage(char secretPassage) {
		this.secretPassage = secretPassage;
	}

	public void setDoorDirection(DoorDirection doorDirection) {
		this.doorDirection = doorDirection;
	}

	public void setRoomLabel(boolean roomLabel) {
		this.roomLabel = roomLabel;
	}

	public void setRoomCenter(boolean roomCenter) {
		this.roomCenter = roomCenter;
	}
	
	public boolean isDoorway() {
		return doorDirection != DoorDirection.NONE;
	}
	
	public boolean isRoom() {
		return isRoom;
	}

	public boolean isOccupied() {
		return isOccupied;
	}
	
	public boolean isLabel() {
		return roomLabel;
	}
	
	public boolean isRoomCenter() {
		return roomCenter;
	}

	public void setOccupied(boolean occupied) {
		isOccupied = occupied;
	}
	
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}
	
	public char getSecretPassage() {
		return secretPassage;
	}

	public char getInitial() {
		return initial;
	}
	
	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	@Override
	public int compareTo(BoardCell cell) {
		if (this.equals(cell)) {
			return 0;
		}
		return -1;
	}

}
