package clueGame;

public class Location {
	
	private int cellWidth;
	private int cellHeight;
	private int offSetX;
	private int offSetY;
	private int doorHeight;
	private int fontSize;
	public static final int SPACING = 2;
	
	public Location(double width, double height, int numRows, int numCols) {
		// get cell width and height
		cellWidth = (int) Math.floor(width/numCols);
		cellHeight = (int) Math.floor(height/numRows);
		// make cell a square
		if (cellWidth > cellHeight) {
			cellWidth = cellHeight;
		}
		else {
			cellHeight = cellWidth;
		}
		
		// calculate the offset from the board edge
		offSetX = ((int) width-(cellWidth*numCols))/2;
		offSetY = ((int) height-(cellHeight*numRows))/2;
		
		// calculate proportion from board dimensions
		int proportion = 0;
		if (height < width) {
			proportion = (int) height;
		}
		else {
			proportion = (int) width;
		}
		doorHeight = (int) Math.floor(proportion/117);
		fontSize = (int) Math.ceil(proportion/40);
	}
	
	/*
	 * Methods that calculate the x and y drawing point of 
	 * a cell given the cell itself or a row and column space
	 */
	public int calcX(BoardCell cell) {
		return cell.getCol() * cellWidth + offSetX;
	}
	
	public int calcY(BoardCell cell) {
		return cell.getRow() * cellHeight + offSetY;
	}
	
	public int calcX(int col) {
		return col * cellWidth + offSetX;
	}
	
	public int calcY(int row) {
		return row * cellHeight + offSetY;
	}

	
	// setters and getters 
	public int getCellWidth() {
		return cellWidth;
	}

	public int getCellHeight() {
		return cellHeight;
	}

	public int getOffSetX() {
		return offSetX;
	}

	public int getOffSetY() {
		return offSetY;
	}

	public int getDoorHeight() {
		return doorHeight;
	}

	public int getFontSize() {
		return fontSize;
	}
	
	
}
