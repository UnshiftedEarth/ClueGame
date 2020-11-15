package clueGame;

import java.awt.Font;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;

public class Room {
	
	private String name;
	private BoardCell centerCell;
	private BoardCell labelCell;
	private Room secretPassage;
	private boolean target;
	private Set<BoardCell> doorList;
	

	public Room(String name) {
		super();
		this.name = name;
		doorList = new HashSet<>();
	}
	
	// method that draws the room names
	public void drawLabel(Graphics g, Location loc) {
		if (labelCell == null) {
			return;
		}
		// gather data from location object
		int fontSize = loc.getFontSize();
		int h = loc.getCellHeight();
		int x = loc.calcX(labelCell);
		int y = loc.calcY(labelCell);
		
		// paint the room name
		g.setFont(new Font("Comic Sans MS", Font.BOLD, fontSize));
		g.drawString(name, x+5, y+h);
	}
	
	//setters and getters
	public String getName() {
		return name;
	}

	public BoardCell getCenterCell() {
		return centerCell;
	}

	public void setCenterCell(BoardCell centerCell) {
		this.centerCell = centerCell;
	}

	public BoardCell getLabelCell() {
		return labelCell;
	}

	public void setLabelCell(BoardCell labelCell) {
		this.labelCell = labelCell;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isTarget() {
		return target;
	}

	public void setTarget(boolean target) {
		this.target = target;
	}

	public void addToDoorList(BoardCell cell) {
		doorList.add(cell);
	}

	public Room getSecretPassage() {
		return secretPassage;
	}

	public void setSecretPassage(Room secretPassage) {
		this.secretPassage = secretPassage;
	}

	public Set<BoardCell> getDoorList() {
		return doorList;
	}

}
