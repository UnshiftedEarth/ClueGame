package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.ComputerPlayer;
import clueGame.Solution;

class ComputerAITest {

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
	
	/*
	 *  This method tests the basic logic for a computer selecting targets
	 *  The computer choose to move to rooms that aren't in its seen list
	 *  otherwise it moves randomly
	 */
	@Test
	public void testSelectTarget() {
		// ensure computer picks room over walkway
		ComputerPlayer computer = new ComputerPlayer();
		board.calcTargets(board.getCell(0, 15), 2);
		BoardCell selected = computer.selectTarget(board.getTargets());
		assertEquals(board.getCell(1, 21), selected);
		
		// ensure the computer picks room that isn't seen
		computer.updateSeen(board.getCard("Security"));
		board.calcTargets(board.getCell(6, 2), 2);
		selected = computer.selectTarget(board.getTargets());
		assertEquals(board.getCell(4, 6), selected);
		
		// test multiple rooms randomly picked both not seen
		computer.clearSeen();
		board.calcTargets(board.getCell(6, 2), 2);
		int sum1 = 0;
		int sum2 = 0; 
		for (int i = 0; i < 14; i++) {
			selected = computer.selectTarget(board.getTargets());
			if (selected.equals(board.getCell(4, 6))) {
				sum1++;
			}
			else if (selected.equals(board.getCell(8, 2))) {
				sum2++;
			}
		}
		assertTrue(sum1 > 0);
		assertTrue(sum2 > 0);
		
		// test computer chooses secret passage room not seen
		computer.updateSeen(board.getCard("Laboratory"));
		board.calcTargets(board.getCell(1, 21), 1);
		selected = computer.selectTarget(board.getTargets());
		assertEquals(board.getCell(17, 1), selected);
		
		//test computer choosing random walkway 
		computer.clearSeen();
		board.calcTargets(board.getCell(9, 9), 1);
		sum1 = 0;
		sum2 = 0; 
		int sum3 = 0;
		int sum4 = 0;
		for (int i = 0; i < 25; i++) {
			selected = computer.selectTarget(board.getTargets());
			if (selected.equals(board.getCell(9, 8))) {
				sum1++;
			}
			else if (selected.equals(board.getCell(8, 9))) {
				sum2++;
			}
			else if (selected.equals(board.getCell(9, 10))) {
				sum3++;
			}
			else if (selected.equals(board.getCell(10, 9))) {
				sum4++;
			}
		}
		assertTrue(sum1 > 0);
		assertTrue(sum2 > 0);
		assertTrue(sum3 > 0);
		assertTrue(sum4 > 0);
	}
	
	/*
	 *  this method tests that the computer correctly creates a suggestion
	 *  and learns something
	 */ 
	@Test
	public void testComputerCreateSuggestion() {
		ComputerPlayer computer = new ComputerPlayer();
		// ensure the computer suggests the one weapon card it hasn't seen 
		computer.updateSeen(board.getCard("Gun"));
		computer.updateSeen(board.getCard("Bat"));
		computer.updateSeen(board.getCard("Knife"));
		computer.updateSeen(board.getCard("Hands"));
		computer.updateSeen(board.getCard("Spike"));
		Solution suggestion = computer.createSuggestion();
		Card weapon = suggestion.weapon;
		assertTrue(weapon.equals(board.getCard("Bow")));////
		computer.clearSeen();
		
		// ensure the computer suggests the one player card it hasn't seen
		computer.updateSeen(board.getCard("Blue"));
		computer.updateSeen(board.getCard("Green"));
		computer.updateSeen(board.getCard("Pink"));
		computer.updateSeen(board.getCard("Yellow"));
		computer.updateSeen(board.getCard("Red"));
		suggestion = computer.createSuggestion();
		Card person = suggestion.person;
		assertTrue(person.equals(board.getCard("Orange")));
		computer.clearSeen();
		
		
		// ensure every other card not seen is returned at least once 
		computer.updateSeen(board.getCard("Gun"));
		computer.updateSeen(board.getCard("Knife"));
		int sum1 = 0;
		int sum2 = 0; 
		int sum3 = 0;
		int sum4 = 0; 
		for (int i = 0; i < 30; i++) {
			suggestion = computer.createSuggestion();
			weapon = suggestion.weapon;
			if (weapon.equals(board.getCard("Hands"))) {
				sum1++;
			}
			else if (weapon.equals(board.getCard("Spike"))) {
				sum2++;
			}
			else if (weapon.equals(board.getCard("Bat"))) {
				sum3++;
			}
			else if (weapon.equals(board.getCard("Bow"))) {
				sum4++;
			}
		}
		assertTrue(sum1 > 0);
		assertTrue(sum2 > 0);
		assertTrue(sum3 > 0);
		assertTrue(sum4 > 0);
		computer.clearSeen();
		
		//ensure that the suggestion contains the room the player is in
		computer.setLocation(1, 21);
		suggestion = computer.createSuggestion();
		assertEquals(board.getCard("Laboratory"), suggestion.room);
		computer.setLocation(6, 15);
		suggestion = computer.createSuggestion();
		assertEquals(board.getCard("Storage"), suggestion.room);
	}

}
