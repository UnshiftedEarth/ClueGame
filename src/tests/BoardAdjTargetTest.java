package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;

class BoardAdjTargetTest {

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


	// test only walkways and unused spaces next to cells
	// these tests are light orange on spreadsheet
	@Test
	public void testAdjacenciesWalkways()
	{
		// Test top edge of board next to unused space
		Set<BoardCell> testList = board.getAdjList(0, 14);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(0, 15)));
		assertTrue(testList.contains(board.getCell(1, 14)));

		// Test close to a door, not adjacent
		testList = board.getAdjList(6, 23);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(6, 22)));
		assertTrue(testList.contains(board.getCell(5, 23)));
		assertTrue(testList.contains(board.getCell(7, 23)));

		// Test only walkways
		testList = board.getAdjList(12, 9);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(12, 10)));
		assertTrue(testList.contains(board.getCell(12, 8)));
		assertTrue(testList.contains(board.getCell(11, 9)));
		assertTrue(testList.contains(board.getCell(13, 9)));
	}


	// test to ensure that room cells not center have no adjacencies
	// these tests are light orange on spreadsheet
	@Test 
	public void testAdjacencyRoomsNotCenter() {
		// Test laboratory next to center
		Set<BoardCell> testList = board.getAdjList(1, 22);
		assertEquals(0, testList.size());

		// Test specimen room next to center
		testList = board.getAdjList(8, 3);
		assertEquals(0, testList.size());

		// Test oxygen room bottom left corner
		testList = board.getAdjList(22, 0);
		assertEquals(0, testList.size());

		//test office next to doorway
		testList = board.getAdjList(11, 11);
		assertEquals(0, testList.size());
	}


	// test to ensure adjacency next to room cells that aren't a doorway
	// these tests are light orange on spreadsheet
	@Test
	public void testAdjacencyBesideRoomsNotDoorway()
	{
		// Test bottom edge of board next to weapons
		Set<BoardCell> testList = board.getAdjList(22, 6);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(22, 5)));
		assertTrue(testList.contains(board.getCell(21, 6)));

		// Test adjacent to storage and unused
		testList = board.getAdjList(6, 18);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(6, 17)));
		assertTrue(testList.contains(board.getCell(6, 19)));

		// Test adjacent to label cell
		testList = board.getAdjList(10, 14);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(10, 13)));
		assertTrue(testList.contains(board.getCell(10, 15)));
		assertTrue(testList.contains(board.getCell(9, 14)));
	}


	// test adjacencies for doorways
	// these tests are light orange on spreadsheet
	@Test 
	public void testAdjacencyDoorways() {
		// Test doorway to specimen room
		Set<BoardCell> testList = board.getAdjList(21, 19);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(21, 18)));
		assertTrue(testList.contains(board.getCell(22, 19)));
		assertTrue(testList.contains(board.getCell(21, 21)));

		// test doorway between electrical and security
		testList = board.getAdjList(6, 2);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(8, 2)));
		assertTrue(testList.contains(board.getCell(6, 1)));

		// test other doorway between electrical and security
		testList = board.getAdjList(6, 1);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(4, 6)));
		assertTrue(testList.contains(board.getCell(6, 2)));

		// test other doorway to oxygen
		testList = board.getAdjList(19, 3);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(17, 1)));
		assertTrue(testList.contains(board.getCell(19, 4)));
		assertTrue(testList.contains(board.getCell(20, 3)));
	}


	// test secret passage adjacencies
	// these tests are light orange on spreadsheet
	@Test
	public void testAdjacencySecretPassages()
	{
		// Test secret passage from electrical to admin
		Set<BoardCell> testList = board.getAdjList(4, 6);
		assertEquals(7, testList.size());
		assertTrue(testList.contains(board.getCell(6, 1)));
		assertTrue(testList.contains(board.getCell(10, 5)));
		assertTrue(testList.contains(board.getCell(10, 6)));
		assertTrue(testList.contains(board.getCell(6, 8)));
		assertTrue(testList.contains(board.getCell(5, 10)));
		assertTrue(testList.contains(board.getCell(4, 10)));
		// secret passage center cell
		assertTrue(testList.contains(board.getCell(18, 15)));

		// Test secret passage from laboratory to oxygen
		testList = board.getAdjList(1, 21);
		assertEquals(5, testList.size());
		assertTrue(testList.contains(board.getCell(1, 15)));
		assertTrue(testList.contains(board.getCell(2, 15)));
		assertTrue(testList.contains(board.getCell(4, 22)));
		assertTrue(testList.contains(board.getCell(5, 22)));
		// secret passage center cell
		assertTrue(testList.contains(board.getCell(17, 1)));
	}
}
