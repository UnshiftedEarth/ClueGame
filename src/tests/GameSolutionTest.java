package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.HumanPlayer;
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
	
	// This method tests the disproveSuggestion() method
	@Test
	public void testDisproveSuggestion() {
		HumanPlayer player = new HumanPlayer();
		Card person = new Card("Blue", CardType.PLAYER);
		Card room = new Card("Electrical", CardType.ROOM);
		Card weapon = new Card("Gun", CardType.WEAPON);
		
		player.updateHand(person);
		player.updateHand(room);
		player.updateHand(weapon);
		
		// ensure each card is returned, only card in hand that matches suggestion
		assertEquals(person, player.disproveSuggestion(person, new Card(), new Card()));
		assertEquals(room, player.disproveSuggestion(new Card(), room, new Card()));
		assertEquals(weapon, player.disproveSuggestion(new Card(), new Card(), weapon));
		// ensure null with no matching cards
		assertEquals(null, player.disproveSuggestion(new Card(), new Card(), new Card()));
		
		int sum1 = 0;
		int sum2 = 0; 
		int sum3 = 0;
		for (int i = 0; i < 20; i++) {
			Card returned = player.disproveSuggestion(person, room, weapon);
			if (returned.getType() == person.getType()) {
				sum1++;
			}
			else if (returned.getType() == room.getType()) {
				sum2++;
			}
			else {
				sum3++;
			}
		}
		// ensure that each card was returned at least once
		assertTrue(sum1 > 0);
		assertTrue(sum2 > 0);
		assertTrue(sum3 > 0);
	}

}
