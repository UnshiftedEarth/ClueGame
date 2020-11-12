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
		cellWidth = (int) Math.floor(width/numCols);
		cellHeight = (int) Math.floor(height/numRows);
		if (cellWidth > cellHeight) {
			cellWidth = cellHeight;
		}
		else {
			cellHeight = cellWidth;
		}
		
		offSetX = ((int) width-(cellWidth*numCols))/2;
		offSetY = ((int) height-(cellHeight*numRows))/2;
		
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
