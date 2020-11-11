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
	public void draw(Graphics g, Map<Character, Room> roomMap, double width, double height, int numRows, int numCols) {
		String name = roomMap.get(initial).getName();
		if (name.equals("Unused")) {
			return;
		}
		
		int cellWidth = (int) Math.floor(width/numCols);
		int cellHeight = (int) Math.floor(height/numRows);
		if (cellWidth > cellHeight) {
			cellWidth = cellHeight;
		}
		else {
			cellHeight = cellWidth;
		}
		
		int offSetX = ((int) width-(cellWidth*numCols))/2;
		int offSetY = ((int) height-(cellHeight*numRows))/2;
		int spacing = 2;
		int w = cellWidth;
		int h = cellHeight;
		int x = col * cellWidth + offSetX;
		int y = row * cellHeight + offSetY;
		
		if (isRoom) {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(x + spacing/2, y + spacing/2, w, h);
		}
		else {
			g.setColor(Color.YELLOW);
			x += spacing;
			y += spacing;
			w -= spacing;
			h -= spacing;
			g.fillRect(x, y, w, h);
		}
	}
	
	public void drawDoor(Graphics g, double width, double height, int numRows, int numCol) {
		if (doorDirection == DoorDirection.NONE) {
			return;
		}
		int proportion = 0;
		int cellWidth = (int) Math.floor(width/numCol);
		int cellHeight = (int) Math.floor(height/numRows);
		if (cellWidth > cellHeight) {
			cellWidth = cellHeight;
		}
		else {
			cellHeight = cellWidth;
		}
		if (height < width) {
			proportion = (int) height;
		}
		else {
			proportion = (int) width;
		}
		int offSetX = ((int) width-(cellWidth*numCol))/2;
		int offSetY = ((int) height-(cellHeight*numRows))/2;
		int spacing = 2;
		int doorHeight = (int) Math.floor(proportion/117);
		int w = cellWidth;
		int h = cellHeight;
		int x = col * cellWidth + offSetX;
		int y = row * cellHeight + offSetY;
		x += spacing;
		y += spacing;
		w -= spacing;
		h -= spacing;
		
		g.setColor(Color.BLUE);
		switch (doorDirection) {
		case UP: 
			g.fillRect(x-spacing/2, y-doorHeight, w+spacing, doorHeight);
			break;
		case DOWN:
			g.fillRect(x-spacing/2, y+h, w+spacing, doorHeight);
			break;
		case LEFT:
			g.fillRect(x-doorHeight, y-spacing/2, doorHeight, h+spacing);
			break;
		case RIGHT:
			g.fillRect(x+w, y-spacing/2, doorHeight, h+spacing);
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
