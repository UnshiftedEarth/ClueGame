package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution;

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
	
	
	// this test ensures we correctly loaded in the weapons in the setup file
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
	
	// this test ensures we correctly loaded in the players in the setup file
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
	
		// ensuring the player types are correct
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

	// this test ensures we correctly loaded in the rooms in the setup file
	@Test
	void testLoadRooms() {
		assertEquals(board.getCard("Laboratory").getName(), "Laboratory");
		assertEquals(board.getCard("Specimen").getName(), "Specimen");
		assertEquals(board.getCard("Admin").getName(), "Admin");
		assertEquals(board.getCard("Weapons").getName(), "Weapons");
		assertEquals(board.getCard("Oxygen").getName(), "Oxygen");
		assertEquals(board.getCard("Security").getName(), "Security");
		assertEquals(board.getCard("Electrical").getName(), "Electrical");
		assertEquals(board.getCard("Storage").getName(), "Storage");
		assertEquals(board.getCard("Office").getName(), "Office");
		
		assertEquals(board.getCard("Laboratory").getType(), CardType.ROOM);
		assertEquals(board.getCard("Specimen").getType(), CardType.ROOM);
		assertEquals(board.getCard("Admin").getType(), CardType.ROOM);
		assertEquals(board.getCard("Weapons").getType(), CardType.ROOM);
		assertEquals(board.getCard("Oxygen").getType(), CardType.ROOM);
		assertEquals(board.getCard("Security").getType(), CardType.ROOM);
		assertEquals(board.getCard("Electrical").getType(), CardType.ROOM);
		assertEquals(board.getCard("Storage").getType(), CardType.ROOM);
		assertEquals(board.getCard("Office").getType(), CardType.ROOM);
		
		// check deck size should be 21
		Set<Card> deck = board.getDeck();
		assertEquals(deck.size(), 21);
	}

	@Test 
	public void dealCardsToSolution() {
		Solution sol = board.getSolution();
		assertTrue(sol.person != null);
		assertTrue(sol.weapon != null);
		assertTrue(sol.room != null);
		
		// check the solution card type
		assertEquals(sol.person.getType(), CardType.PLAYER);
		assertEquals(sol.weapon.getType(), CardType.WEAPON);
		assertEquals(sol.room.getType(), CardType.ROOM);
		
		//check if card is in the deck
		assertTrue(board.getDeck().contains(sol.person));
		assertTrue(board.getDeck().contains(sol.weapon));
		assertTrue(board.getDeck().contains(sol.room));
	}
	
	// this test ensure that cards are correctly dealt to players
	@Test
	public void dealCardsToPlayers() {
		int sum = 0;
		ArrayList<Card> cardsSeen = new ArrayList<>();
		Set<Card> deck = board.getDeck();
		
		for (Player player : board.getPlayers()) {
			Set<Card> hand = player.getHand();
			// check if each players hand has 3 cards
			assertEquals(hand.size(), 3); 
			for (Card card : hand) {
				// check if cards was previously dealt
				assertFalse(cardsSeen.contains(card));
				cardsSeen.add(card);
				// check if player's card is in the deck
				assertTrue(deck.contains(card));
			}
			sum += hand.size();
		}
		
		// ensure every card is dealt 
		// players should each have 3 cards
		assertEquals(sum, 18);
	}

}
