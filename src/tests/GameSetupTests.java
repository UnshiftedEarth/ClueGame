package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;

class GameSetupTests {
	
	private static Board board;

	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
	}
	
	
	@Test
	void testLoadWeapons() {
		assertEquals(board.getCard("Gun").getName(), "Gun");
		assertEquals(board.getCard("Knife").getName(), "Knife");
		assertEquals(board.getCard("Bow").getName(), "Bow");
		assertEquals(board.getCard("Hands").getName(), "Hands");
		assertEquals(board.getCard("Bat").getName(), "Bat");
		assertEquals(board.getCard("Spike").getName(), "Spike");
		
		assertEquals(board.getCard("Gun").getType(), CardType.WEAPON);
		assertEquals(board.getCard("Knife").getType(), CardType.WEAPON);
		assertEquals(board.getCard("Bow").getType(), CardType.WEAPON);
		assertEquals(board.getCard("Hands").getType(), CardType.WEAPON);
		assertEquals(board.getCard("Bat").getType(), CardType.WEAPON);
		assertEquals(board.getCard("Spike").getType(), CardType.WEAPON);
	}

	@Test
	void testLoadPlayers() {
		assertEquals(board.getPlayer("Blue").getName(), "Blue");
		assertEquals(board.getPlayer("Green").getName(), "Green");
		assertEquals(board.getPlayer("Pink").getName(), "Pink");
		assertEquals(board.getPlayer("Yellow").getName(), "Yellow");
		assertEquals(board.getPlayer("Orange").getName(), "Orange");
		assertEquals(board.getPlayer("Red").getName(), "Red");
		
		assertEquals(board.getPlayer("Blue").getColor(), Color.BLUE);
		assertEquals(board.getPlayer("Green").getColor(), Color.GREEN);
		assertEquals(board.getPlayer("Pink").getColor(), Color.PINK);
		assertEquals(board.getPlayer("Yellow").getColor(), Color.YELLOW);
		assertEquals(board.getPlayer("Orange").getColor(), Color.ORANGE);
		assertEquals(board.getPlayer("Red").getColor(), Color.RED);
	
		assertTrue(board.getPlayer("Blue") instanceof HumanPlayer);
		assertTrue(board.getPlayer("Green") instanceof ComputerPlayer);
		assertTrue(board.getPlayer("Yellow") instanceof ComputerPlayer);
		assertTrue(board.getPlayer("Red") instanceof ComputerPlayer);
		assertTrue(board.getPlayer("Pink") instanceof ComputerPlayer);
		assertTrue(board.getPlayer("Orange") instanceof ComputerPlayer);
		
		assertEquals(board.getCard("Blue").getType(), CardType.PLAYER);
		assertEquals(board.getCard("Green").getType(), CardType.PLAYER);
		assertEquals(board.getCard("Yellow").getType(), CardType.PLAYER);
		assertEquals(board.getCard("Red").getType(), CardType.PLAYER);
		assertEquals(board.getCard("Pink").getType(), CardType.PLAYER);
		assertEquals(board.getCard("Orange").getType(), CardType.PLAYER);
		
		assertEquals(board.getCard("Blue").getName(), "Blue");
		assertEquals(board.getCard("Green").getName(), "Green");
		assertEquals(board.getCard("Pink").getName(), "Pink");
		assertEquals(board.getCard("Yellow").getName(), "Yellow");
		assertEquals(board.getCard("Orange").getName(), "Orange");
		assertEquals(board.getCard("Red").getName(), "Red");
	}

	@Test
	void testLoadRooms() {
		assertEquals(board.getCard("Laboratory").getName(), "Laboratory");
		assertEquals(board.getCard("Speciman").getName(), "Speciman");
		assertEquals(board.getCard("Admin").getName(), "Admin");
		assertEquals(board.getCard("Weapons").getName(), "Weapons");
		assertEquals(board.getCard("Oxygen").getName(), "Oxygen");
		assertEquals(board.getCard("Security").getName(), "Security");
		assertEquals(board.getCard("Electrical").getName(), "Electrical");
		assertEquals(board.getCard("Storage").getName(), "Storage");
		assertEquals(board.getCard("Office").getName(), "Office");
		
		assertEquals(board.getCard("Laboratory").getType(), CardType.ROOM);
		assertEquals(board.getCard("Speciman").getType(), CardType.ROOM);
		assertEquals(board.getCard("Admin").getType(), CardType.ROOM);
		assertEquals(board.getCard("Weapons").getType(), CardType.ROOM);
		assertEquals(board.getCard("Oxygen").getType(), CardType.ROOM);
		assertEquals(board.getCard("Security").getType(), CardType.ROOM);
		assertEquals(board.getCard("Electrical").getType(), CardType.ROOM);
		assertEquals(board.getCard("Storage").getType(), CardType.ROOM);
		assertEquals(board.getCard("Office").getType(), CardType.ROOM);
	}

}
