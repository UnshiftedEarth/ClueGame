package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.*;

import experiment.*;

class BoardTestsExp {
	
	TestBoard board;

	@BeforeEach
	public void setup() {
		//setup board with 3 rows and 3 columns
		board = new TestBoard(3,3);
	}
	
	
	/*
	 * There are 5 tests for the correct adjacency list
	 *
	 *
	 *
	 *  test adjacency list for bottom right corner cell (3,3)
	 */
	@Test
	public void testAdjacencyBottomRightCorner() {
		TestBoardCell cell = board.getCell(3,3);
		Set<TestBoardCell> list = cell.getAdjList();
		assertEquals(2, list.size());
		assertTrue(list.contains(board.getCell(3, 2)));
		assertTrue(list.contains(board.getCell(2, 3)));
	}
	
	/*
	 *  test adjacency list for top left corner cell (0,0)
	 */
	@Test
	public void testAdjacencyTopLeftCorner() {
		TestBoardCell cell = board.getCell(0, 0);
		Set<TestBoardCell> list = cell.getAdjList();
		assertEquals(2, list.size());
		assertTrue(list.contains(board.getCell(0, 1)));
		assertTrue(list.contains(board.getCell(1, 0)));
	}
	
	/*
	 * test adjacency list for left edge cell (2,0)
	 */
	@Test
	public void testAdjacencyLeftEdge() {
		TestBoardCell cell = board.getCell(2, 0);
		Set<TestBoardCell> list = cell.getAdjList();
		assertEquals(3, list.size());
		assertTrue(list.contains(board.getCell(1, 0)));
		assertTrue(list.contains(board.getCell(2, 1)));
		assertTrue(list.contains(board.getCell(3, 0)));
	}
	
	/*
	 * test adjacency list for left edge cell (2,3)
	 */
	@Test
	public void testAdjacencyRightEdge() {
		TestBoardCell cell = board.getCell(2, 3);
		Set<TestBoardCell> list = cell.getAdjList();
		assertEquals(3, list.size());
		assertTrue(list.contains(board.getCell(1, 3)));
		assertTrue(list.contains(board.getCell(2, 2)));
		assertTrue(list.contains(board.getCell(3, 3)));
	}
	
	/*
	 * test adjacency list for middle cell (2,2)
	 */
	@Test
	public void testAdjacencyMiddle() {
		TestBoardCell cell = board.getCell(2,2);
		Set<TestBoardCell> list = cell.getAdjList();
		assertEquals(4, list.size());
		assertTrue(list.contains(board.getCell(2, 3)));
		assertTrue(list.contains(board.getCell(3, 2)));
		assertTrue(list.contains(board.getCell(2, 1)));
		assertTrue(list.contains(board.getCell(1, 2)));
	}
	
	/*
	 * Below are the tests for calcTargets. There are 5 in total
	 *
	 *
	 *
	 * test calcTargets for 3 steps starting at cell (0,0)
	 */
	@Test 
	public void testEmptyBoardTargets1() {
		TestBoardCell cell = board.getCell(0,0);
		board.calcTargets(cell, 3);
		Set<TestBoardCell> targets = board.getTargets();
		assertEquals(targets.size(), 6);
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(0, 3)));
		assertTrue(targets.contains(board.getCell(1, 0)));
	}
	
	/*
	 * test calcTargets for 2 steps starting at cell (1,0)
	 */
	@Test
	public void testEmptyBoardTargets2() {
		TestBoardCell cell = board.getCell(1,0);
		board.calcTargets(cell, 2);
		Set<TestBoardCell> targets = board.getTargets();
		assertEquals(targets.size(), 4);
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(3, 0)));
	}
	
	/*
	 * test calcTargets for 2 steps starting at cell (1,0)
	 * with 2 cells occupied: (0,1) and (1,2)
	 */
	@Test
	public void testSpaceOccupied1() {
		board.getCell(0, 1).setOccupied(true);
		board.getCell(1, 2).setOccupied(true);
		TestBoardCell cell = board.getCell(1,0);
		board.calcTargets(cell, 2);
		Set<TestBoardCell> targets = board.getTargets();
		assertEquals(targets.size(), 2);
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(3, 0)));
	}
	
	/*
	 * test calcTargets for 3 steps starting at cell (0,0)
	 * with 3 cells occupied: (3,0), (2,1), (0,1)
	 */
	@Test
	public void testSpaceOccupied2() {
		board.getCell(3, 0).setOccupied(true);
		board.getCell(2, 1).setOccupied(true);
		board.getCell(0, 1).setOccupied(true);
		TestBoardCell cell = board.getCell(0,0);
		board.calcTargets(cell, 3);
		Set<TestBoardCell> targets = board.getTargets();
		assertEquals(targets.size(), 3);
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(0, 3)));
		assertTrue(targets.contains(board.getCell(1, 0)));
	}
	
	/*
	 * test calcTargets for 3 steps starting at cell (1,0)
	 * with 2 cells as rooms: (1,1) and (2,0)
	 */
	@Test 
	public void testRoomSpace() {
		board.getCell(1, 1).setRoom(true);
		board.getCell(2, 0).setRoom(true);
		TestBoardCell cell = board.getCell(1,0);
		board.calcTargets(cell, 3);
		Set<TestBoardCell> targets = board.getTargets();
		assertEquals(targets.size(), 3);
		assertTrue(targets.contains(board.getCell(0, 2)));
		assertTrue(targets.contains(board.getCell(1, 1)));
		assertTrue(targets.contains(board.getCell(2, 0)));
	}
	
}
