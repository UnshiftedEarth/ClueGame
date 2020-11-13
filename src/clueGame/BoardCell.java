package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
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
	private int x;
	private int y;
	private int cellWidth;
	private int cellHeight;
	
	
	public BoardCell(int row, int col) {
		super();
		this.row = row;
		this.col = col;
		adjList = new HashSet<>();
	}
	
	// Method for each boardcell to draw itself
	public void draw(Graphics g, Map<Character, Room> roomMap, Location loc, boolean target) {
		String name = roomMap.get(initial).getName();
		if (name.equals("Unused")) {
			return;
		}
		
		// gather data from location object
		cellWidth = loc.getCellWidth();
		cellHeight = loc.getCellHeight();
		int w = loc.getCellWidth();
		int h = loc.getCellHeight();
		x = loc.calcX(this);
		y = loc.calcY(this); 
		
		// paint cell gray if cell is room
		if (isRoom) {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(x + loc.SPACING/2, y + loc.SPACING/2, w, h);
		}
		// paint cell yellow for standard walkway
		else {
			// check if cell is a target
			if (target) {
				g.setColor(new Color(0, 205, 255));
			}
			else {
				g.setColor(new Color(255, 230, 0)); // yellowish color
			}
			
			x += loc.SPACING;
			y += loc.SPACING;
			w -= loc.SPACING;
			h -= loc.SPACING;
			g.fillRect(x, y, w, h);
		}
	}
	
	// method for drawing the room doors
	public void drawDoor(Graphics g, Location loc) {
		if (doorDirection == DoorDirection.NONE) {
			return;
		}
		
		// gather data from location object
		int w = loc.getCellWidth();
		int h = loc.getCellHeight();
		x = loc.calcX(this);
		y = loc.calcY(this);
		int door = loc.getDoorHeight();
		x += loc.SPACING;
		y += loc.SPACING;
		w -= loc.SPACING;
		h -= loc.SPACING;
		
		// determine which direction and paint
		g.setColor(Color.BLUE);
		switch (doorDirection) {
		case UP: 
			g.fillRect(x-loc.SPACING/2, y-door, w+loc.SPACING, door);
			break;
		case DOWN:
			g.fillRect(x-loc.SPACING/2, y+h, w+loc.SPACING, door);
			break;
		case LEFT:
			g.fillRect(x-door, y-loc.SPACING/2, door, h+loc.SPACING);
			break;
		case RIGHT:
			g.fillRect(x+w, y-loc.SPACING/2, door, h+loc.SPACING);
		}
	}
	
	public boolean containsClick(int mouseX, int mouseY) {
		Rectangle rect = new Rectangle(x, y, cellWidth, cellHeight);
		if (rect.contains(new Point(mouseX, mouseY))) {
			return true;
		}
		return false;
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

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public int compareTo(BoardCell cell) {
		if (this.equals(cell)) {
			return 0;
		}
		return -1;
	}

}
