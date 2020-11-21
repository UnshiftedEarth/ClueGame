package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public abstract class Player {
	
	private String name;
	private Color color;
	private int row;
	private int column;
	private int x;
	private int y;
	private boolean animate;
	private boolean offset;
	private boolean roomTarget;
	private boolean finished;
	protected Set<Card> hand;
	protected Set<Card> seenCards;
	protected Board board = Board.getInstance();
	
	
	public Player() {
		super();
		hand = new HashSet<>();
		seenCards = new HashSet<>();
	}
	
	public Player(String name, String color) {
		this.name = name;
		setColor(color);
		hand = new HashSet<>();
		seenCards = new HashSet<>();
	}
	
	public Player(String name, String color, int r, int c) {
		this.name = name;
		setColor(color);
		hand = new HashSet<>();
		seenCards = new HashSet<>();
		setLocation(r, c);
	}
	

	public void updateHand(Card card) {
		hand.add(card);
	}
	
	// this method returns the room card the player is currently in 
	public Card getRoomCard() {
		BoardCell cell = board.getCell(row, column);
		Room room = board.getRoom(cell);
		String roomName = room.getName();
		return board.getCard(roomName);
	}
	
	/*
	 * Looks for matches in players hand to disprove a suggestion
	 * returns null if no matches found
	 */
	public Card disproveSuggestion(Solution suggestion) {
		Card player = suggestion.person;
		Card room = suggestion.room;
		Card weapon = suggestion.weapon;
		ArrayList<Card> match = new ArrayList<>();
		// create a list of cards in hand that match the suggestion
		for (Card card : hand) {
			if (card.equals(player) || card.equals(room) || card.equals(weapon)) {
				match.add(card);
			}
		}
		// handle different numbers of matches
		if (match.size() == 1) {
			return match.get(0);
		}
		else if (match.size() > 1) {
			Random random = new Random();
			int rand = random.nextInt(match.size());
			return match.get(rand);
		}
		return null;
	}
	
	// draw the player on the board
	public void draw(Graphics g, Location loc) {
		// gather data from location object
		int w = loc.getCellWidth();
		int h = loc.getCellHeight();
		int n = sameLocationNumber();
		// if animation is active
		if (animate) {
			
		}
		// if multiple players in a room
		else if (n > 0) {
			x = loc.calcX(column);
			y = loc.calcY(row);
			x += loc.SPACING;
			y += loc.SPACING;
			x += loc.getPlayerOffset() * n;
		}
		else {
			x = loc.calcX(column);
			y = loc.calcY(row);
			x += loc.SPACING;
			y += loc.SPACING;
		}
		w -= loc.SPACING;
		h -= loc.SPACING;
		
		// paint the player
		g.setColor(color);
		g.fillOval(x, y, w, h);
		g.setColor(Color.BLACK);
		g.drawOval(x, y, w, h);
	}
	
	public boolean isInRoom() {
		if (board.getCell(row, column).isRoomCenter()) {
			return true;
		}
		return false;
	}
	
	private int sameLocationNumber() {
		int count = 0;
		for (Player player : board.getPlayers()) {
			if (player.getRow() == row && player.getColumn() == column && !player.equals(this) && !player.offset) {
				count++;
				offset = true;
			}
		}
		return count;
	}
	
	public boolean hasCard(Card card) {
		for (Card c : hand) {
			if (c.equals(card)) {
				return true;
			}
		}
		return false;
	}
	
	public void updateSeen(Card seenCard) {
		seenCards.add(seenCard);
	}
	
	public void clearHand() {
		hand.clear();
	}
	
	public void clearSeen() {
		seenCards.clear();
	}
	

	// setters and getters
	public String getName() {
		return name;
	}

	public Color getColor() {
		return color;
	}
	
	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public Set<Card> getHand() {
		return hand;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public void setOffset(boolean offset) {
		this.offset = offset;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setAnimate(boolean b) {
		animate = b;
	}

	public void setLocation(int row, int col) {
		this.row = row;
		this.column = col;
	}
	
	public boolean isRoomTarget() {
		return roomTarget;
	}

	public void setRoomTarget(boolean roomTarget) {
		this.roomTarget = roomTarget;
	}

	public void setColor(String c) {
		try {
		    Field field = Class.forName("java.awt.Color").getField(c);
		    Color temp = (Color) field.get(null);
		    int red = temp.getRed();
		    int green = temp.getGreen();
		    int blue = temp.getBlue();
		    int adj = 100;
		    if (red + adj <= 255) {
		    	red += adj;
		    }
		    if (green + adj <= 255) {
		    	green += adj;
		    }
		    if (blue + adj <= 255) {
		    	blue += adj;
		    }
		    Color lighter = new Color(red, green, blue);
		    color = lighter;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
