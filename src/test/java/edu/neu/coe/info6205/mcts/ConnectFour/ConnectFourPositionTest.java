package edu.neu.coe.info6205.mcts.ConnectFour;

import org.junit.Test;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.*;

public class ConnectFourPositionTest {

    @Test
    public void testParseCell() {
        assertEquals("Parsing 'O' should return 0", 0, ConnectFourPosition.parseCell("O"));
        assertEquals("Parsing '0' should return 0", 0, ConnectFourPosition.parseCell("0"));
        assertEquals("Parsing 'X' should return 1", 1, ConnectFourPosition.parseCell("X"));
        assertEquals("Parsing '1' should return 1", 1, ConnectFourPosition.parseCell("1"));
        assertEquals("Parsing ' ' should return -1", -1, ConnectFourPosition.parseCell(" "));
        assertEquals("Parsing '-' should return -1", -1, ConnectFourPosition.parseCell("-"));
    }

    @Test
    public void testParsePosition() {
        String grid = "O - O - O - O\n" +
                "X - X - X - X\n" +
                "O - O - O - O\n" +
                "X - X - X - X\n" +
                "O - O - O - O\n" +
                "X - X - X - X";
        ConnectFourPosition position = ConnectFourPosition.parsePosition(grid, 1);
        assertNotNull("Parsed position should not be null", position);
        assertFalse("Position should not be full", position.full());
    }

    @Test
    public void testWinner() {
        String grid = "O O O O - - -\n" +
                "X - - - X - -\n" +
                "X - - X - - -\n" +
                "X - X - - - -\n" +
                "O - - - - - -\n" +
                "O - - - - - -";
        ConnectFourPosition position = ConnectFourPosition.parsePosition(grid, 0);
        Optional<Integer> winner = position.winner();
        assertTrue("Winner should be present", winner.isPresent());
        assertEquals("Winner should be player 0", 0, (int) winner.get());
    }

    @Test
    public void testFull() {
        String grid = "O O O O O O O\n" +
                "X X X X X X X\n" +
                "O O O O O O O\n" +
                "X X X X X X X\n" +
                "O O O O O O O\n" +
                "X X X X X X X";
        ConnectFourPosition position = ConnectFourPosition.parsePosition(grid, 1);
        assertTrue("Position should be full", position.full());
    }

    @Test
    public void testMove() {
        int[][] grid = {
                {-1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1}
        };
        ConnectFourPosition position = new ConnectFourPosition(grid, 0, -1);
        ConnectFourPosition newPosition = position.move(0, 3);
        assertEquals("New position should have player 0's piece at column 3", 0, newPosition.grid[5][3]);
    }

    @Test(expected = RuntimeException.class)
    public void testMoveColumnFull() {
        int[][] grid = {
                {-1, -1, -1, -1, -1, -1, 1},
                {-1, -1, -1, -1, -1, -1, 0},
                {-1, -1, -1, -1, -1, -1, 1},
                {-1, -1, -1, -1, -1, -1, 0},
                {-1, -1, -1, -1, -1, -1, 1},
                {-1, -1, -1, -1, -1, -1, 0}
        };
        ConnectFourPosition position = new ConnectFourPosition(grid, 0, -1);
        position.move(1, 6);
    }

    @Test
    public void testMoves() {
        int[][] grid = {
                {-1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1},
                {-1, -1, -1, -1, -1, -1, -1}
        };
        ConnectFourPosition position = new ConnectFourPosition(grid, 0, -1);
        List<Integer> moves = position.moves(0);
        assertEquals("Should have 7 possible moves", 7, moves.size());
        assertTrue("Moves should contain column 0", moves.contains(0));
        assertTrue("Moves should contain column 1", moves.contains(1));
        assertTrue("Moves should contain column 2", moves.contains(2));
        assertTrue("Moves should contain column 3", moves.contains(3));
        assertTrue("Moves should contain column 4", moves.contains(4));
        assertTrue("Moves should contain column 5", moves.contains(5));
        assertTrue("Moves should contain column 6", moves.contains(6));
    }

    @Test
    public void testReflect() {
        int[][] grid = {
                {0, 1, 0, 1, 0, 1, 0},
                {1, 0, 1, 0, 1, 0, 1},
                {0, 1, 0, 1, 0, 1, 0},
                {1, 0, 1, 0, 1, 0, 1},
                {0, 1, 0, 1, 0, 1, 0},
                {1, 0, 1, 0, 1, 0, 1}
        };
        ConnectFourPosition position = new ConnectFourPosition(grid, 0, -1);
        ConnectFourPosition reflectedPosition = position.reflect(0);
        assertArrayEquals("Reflected position should be identical to original", position.grid, reflectedPosition.grid);
    }
}
//