package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.Solution;

class GameSolutionTest {
	
	private static Board board;
	
	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");	
		// clear the deck
		board.clearDeck();
		// Initialize will load config files 
		board.initialize();
	}
	
	// This method tests the checkAccusation() method
	@Test
	public void testAccusation() {
		Solution sol = board.getSolution();
		Card player = new Card("Blue", CardType.PLAYER);
		Card room = new Card("Electrical", CardType.ROOM);
		Card weapon = new Card("Gun", CardType.WEAPON);
		
		sol.person = player;
		sol.room = room;
		sol.weapon = weapon;
		
		assertTrue(board.checkAccusation(player, room, weapon));
		assertFalse(board.checkAccusation(new Card(), new Card(), new Card()));
		assertFalse(board.checkAccusation(player, room, new Card("Spike", CardType.WEAPON)));
		assertFalse(board.checkAccusation(player, new Card("Laboratory", CardType.ROOM), weapon));
		assertFalse(board.checkAccusation(new Card("Yellow", CardType.PLAYER), room, weapon));
		assertFalse(board.checkAccusation(room, weapon, player));
	}

}
