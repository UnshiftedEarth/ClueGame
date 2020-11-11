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
	private Set<BoardCell> doorList;
	

	public Room(String name) {
		super();
		this.name = name;
		doorList = new HashSet<>();
	}
	
	public void drawLabel(Graphics g, double width, double height, int numRows, int numCols) {
		if (labelCell == null) {
			return;
		}
		int cellWidth = (int) Math.floor(width/numCols);
		int cellHeight = (int) Math.floor(height/numRows);
		int proportion = 0;
		
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
		
		int fontSize = (int) Math.ceil(proportion/40);
		int offSetX = ((int) width-(cellWidth*numCols))/2;
		int offSetY = ((int) height-(cellHeight*numRows))/2;
		int h = cellHeight;
		int x = labelCell.getCol() * cellWidth + offSetX;
		int y = labelCell.getRow() * cellHeight + offSetY;
		
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
