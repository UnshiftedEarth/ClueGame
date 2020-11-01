package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;
import clueGame.Room;

class FileInitTests {
	
	public static final int LEGEND_SIZE = 11;
	public static final int NUM_ROWS = 23;
	public static final int NUM_COLUMNS = 24;


	private static Board board;
	
	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		// Initialize will load BOTH config files
		board.initialize();
	}

	@Test
	public void testAmongUsRoomLabels() {
		// To ensure data is correctly loaded, test retrieving a few rooms
		// from the hash, including the first and last in the file and a few others
		// from among us map
		assertEquals("Laboratory", board.getRoom('L').getName() );
		assertEquals("Specimen", board.getRoom('P').getName() );
		assertEquals("Admin", board.getRoom('A').getName() );
		assertEquals("Weapons", board.getRoom('W').getName() );
		assertEquals("Oxygen", board.getRoom('O').getName() );
		assertEquals("Security", board.getRoom('S').getName() );
		assertEquals("Electrical", board.getRoom('E').getName() );
		assertEquals("Storage", board.getRoom('G').getName() );
		assertEquals("Office", board.getRoom('F').getName() );
	}
	
	@Test
	public void testAmongUsBoardDimensions() {
		// Ensure we have the proper number of rows and columns for among us map
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLUMNS, board.getNumColumns());
	}
	
	
	@Test
	public void testFourDoorDirections() {
		BoardCell cell = board.getCell(5, 10);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.LEFT, cell.getDoorDirection());
		cell = board.getCell(14, 11);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.UP, cell.getDoorDirection());
		cell = board.getCell(5, 12);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.RIGHT, cell.getDoorDirection());
		cell = board.getCell(15, 13);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.DOWN, cell.getDoorDirection());
		// Test that walkways are not doors
		cell = board.getCell(10, 10);
		assertFalse(cell.isDoorway());
	}

	@Test
	public void testAmongUsNumberOfDoorways() {
		//ensure we have the correct number of doorways on the among us map
		int numDoors = 0;
		for (int row = 0; row < board.getNumRows(); row++)
			for (int col = 0; col < board.getNumColumns(); col++) {
				BoardCell cell = board.getCell(row, col);
				if (cell.isDoorway()) {
					numDoors++;
				}
			}
		Assert.assertEquals(30, numDoors);
	}


	// Test a few room cells to ensure the room initial is correct on among us map
	@Test
	public void testAmongUSRooms() {
		// just test a standard room location
		BoardCell cell = board.getCell(22, 23);
		Room room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Specimen" ) ;
		assertFalse( cell.isLabel() );
		assertFalse( cell.isRoomCenter() ) ;
		assertFalse( cell.isDoorway()) ;

		// this is a label cell to test
		cell = board.getCell(7, 1);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Security" ) ;
		assertTrue( cell.isLabel() );
		assertTrue( room.getLabelCell() == cell );

		// this is a room center cell to test
		cell = board.getCell(21, 8);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Weapons" ) ;
		assertTrue( cell.isRoomCenter() );
		assertTrue( room.getCenterCell() == cell );

		// this is a secret passage test
		cell = board.getCell(22, 1);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Oxygen" ) ;
		assertTrue( cell.getSecretPassage() == 'L' );

		// test a walkway
		cell = board.getCell(7, 11);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Hallway" ) ;
		assertFalse( cell.isRoomCenter() );
		assertFalse( cell.isLabel() );

		// test an unused space
		cell = board.getCell(0, 0);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Unused" ) ;
		assertFalse( cell.isRoomCenter() );
		assertFalse( cell.isLabel() );
	}
	
	// test for different types of rooms in close proximity
	@Test
	public void testAmongUsRoomsCloseProximity() {
		BoardCell cell1 = board.getCell(4,20);
		BoardCell cell2 = board.getCell(4,21);
		BoardCell cell3 = board.getCell(4,22);
		assertTrue(cell1 != null && board.getRoom(cell1) != null);
		assertTrue(cell2 != null && board.getRoom(cell2) != null);
		assertTrue(cell3 != null && board.getRoom(cell3) != null);
		assertEquals(board.getRoom(cell1).getName(), "Laboratory" );
		assertEquals(board.getRoom(cell2).getName(), "Laboratory" );
		assertEquals(board.getRoom(cell3).getName(), "Hallway" );
		assertTrue(cell3.isDoorway());
		assertEquals(DoorDirection.LEFT, cell3.getDoorDirection());
	}
	
	@Test
	public void testAmongUsNumberOfSecretPassages() {
		//ensure we have the correct number of secret passages on the among us map
		int numPassages = 0;
		for (int row = 0; row < board.getNumRows(); row++) {
			for (int col = 0; col < board.getNumColumns(); col++) {
				BoardCell cell = board.getCell(row, col);
				if (cell.getSecretPassage() != '\u0000') {
					numPassages++;
				}
			}
		}
		Assert.assertEquals(4, numPassages);
	}
}
