package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution;

class GameSolutionTest {
	
	private static Board board;
	private static Card person;
	private static Card room;
	private static Card weapon;
	
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
		
		person = new Card("Blue", CardType.PLAYER);
		room = new Card("Electrical", CardType.ROOM);
		weapon = new Card("Gun", CardType.WEAPON);
	}
	
	// This method tests the checkAccusation() method
	@Test
	public void testAccusation() {
		Solution sol = board.getSolution();
		sol.person = person;
		sol.room = room;
		sol.weapon = weapon;
		// ensure that accusation is only correct if all three cards are correct
		assertTrue(board.checkAccusation(new Solution(person, room, weapon)));
		assertFalse(board.checkAccusation(new Solution(new Card(), new Card(), new Card())));
		assertFalse(board.checkAccusation(new Solution(person, room, new Card("Spike", CardType.WEAPON))));
		assertFalse(board.checkAccusation(new Solution(person, new Card("Laboratory", CardType.ROOM), weapon)));
		assertFalse(board.checkAccusation(new Solution(new Card("Yellow", CardType.PLAYER), room, weapon)));
		assertFalse(board.checkAccusation(new Solution(room, weapon, person)));
	}
	
	// This method tests the disproveSuggestion() method
	@Test
	public void testDisproveSuggestion() {
		HumanPlayer player = new HumanPlayer();
		
		player.updateHand(person);
		player.updateHand(room);
		player.updateHand(weapon);
		
		// ensure each card is returned, only card in hand that matches suggestion
		assertEquals(person, player.disproveSuggestion(new Solution(person, new Card(), new Card())));
		assertEquals(room, player.disproveSuggestion(new Solution(new Card(), room, new Card())));
		assertEquals(weapon, player.disproveSuggestion(new Solution(new Card(), new Card(), weapon)));
		// ensure null with no matching cards
		assertEquals(null, player.disproveSuggestion(new Solution(new Card(), new Card(), new Card())));
		
		int sum1 = 0;
		int sum2 = 0; 
		int sum3 = 0;
		for (int i = 0; i < 20; i++) {
			Card returned = player.disproveSuggestion(new Solution(person, room, weapon));
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
	
	// This method tests the handleSuggestion() method
	@Test
	public void testHandleSuggestion() {
		// writing the tests to handle a suggestion, following the hints on the assignment
		HumanPlayer human = new HumanPlayer();
		ComputerPlayer comp1 = new ComputerPlayer();
		ComputerPlayer comp2 = new ComputerPlayer();
		ComputerPlayer comp3 = new ComputerPlayer();
		ArrayList<Player> players = new ArrayList<>();
		players.add(human);
		players.add(comp1);
		players.add(comp2);
		players.add(comp3);
		board.setPlayers(players);
		Solution suggestion = new Solution(person, room, weapon);
		
		// ensure that null is returned because no match found
		assertTrue(board.handleSuggestion(human, suggestion) == null);
		comp1.updateHand(person);
		assertTrue(board.handleSuggestion(comp2, new Solution(new Card(), room, weapon)) == null);
		comp1.clearHand();
		
		//ensure that null is returned if person making suggestion has matching card
		human.updateHand(person);
		assertTrue(board.handleSuggestion(human, suggestion) == null);
		human.clearHand();
		comp3.updateHand(person);
		comp3.updateHand(room);
		comp3.updateHand(weapon);
		assertTrue(board.handleSuggestion(comp3, suggestion) == null);
		comp3.clearHand();

		//ensure that players are queried in order
		comp3.updateHand(room);
		comp1.updateHand(weapon);
		assertTrue(board.handleSuggestion(human, suggestion) == weapon);
		comp1.clearHand();
		comp3.clearHand();
		
		// Computer 2 makes suggestion, human has card that matches suggestion, and so does computer 1
		// human card match should be returned 
		comp2.updateHand(person);
		human.updateHand(weapon);
		comp1.updateHand(room);
		assertTrue(board.handleSuggestion(comp2, suggestion) == weapon);
		comp2.clearHand();
		human.clearHand();
	}

}
