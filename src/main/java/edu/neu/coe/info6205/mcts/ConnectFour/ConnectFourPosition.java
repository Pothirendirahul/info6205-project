package edu.neu.coe.info6205.mcts.ConnectFour;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * ConnectFourPosition class represents the current position of the Connect Four game.
 */
public class ConnectFourPosition {

    public final int[][] grid; // The grid representing the game board
    final int last; // The last player who made a move
    final int count; // The count of filled cells on the board

    static final int gridColumns = 7; // Number of columns in the game board
    static final int gridRows = 6; // Number of rows in the game board

    /**
     * Constructor to create a ConnectFourPosition object with the specified grid, count, and last player.
     * @param grid The grid representing the game board.
     * @param count The count of filled cells on the board.
     * @param last The last player who made a move.
     */
    public ConnectFourPosition(int[][] grid, int count, int last) {
        this.grid = grid;
        this.count = count;
        this.last = last;
    }

    /**
     * Method to parse a single cell of the game board.
     *
     * @param cell The string representation of the cell.
     * @return An integer representing the player or empty (-1) cell.
     */
    public static int parseCell(String cell) {
        return switch (cell.toUpperCase()) {
            case "O", "0" -> 0;
            case "X", "1" -> 1;
            default -> -1;
        };
    }

    /**
     * Method to parse the entire game board and create a ConnectFourPosition object.
     *
     * @param grid The string representation of the game board.
     * @param last The last player who made a move.
     * @return A ConnectFourPosition object representing the parsed game board.
     */
    public static ConnectFourPosition parsePosition(final String grid, final int last) {
        int[][] matrix = new int[gridRows][gridColumns];
        int count = 0;
        String[] rows = grid.split("\\n", gridRows);
        for (int i = 0; i < gridRows; i++) {
            String[] cells = rows[i].split(" ", gridColumns);
            for (int j = 0; j < gridColumns; j++) {
                int cell = parseCell(cells[j].trim());
                if (cell >= 0) count++;
                matrix[i][j] = cell;
            }
        }
        return new ConnectFourPosition(matrix, count, last);
    }

    /**
     * Check if there is a winner on the current game board.
     *
     * @return An optional integer representing the winning player, or empty if no winner.
     */
    public Optional<Integer> winner() {
        for (int i = 0; i < gridRows; i++) {
            for (int j = 0; j < gridColumns; j++) {
                int player = grid[i][j];
                if (player == -1) continue; // Skip empty cells
                // Check horizontal
                if (j + 3 < gridColumns && grid[i][j + 1] == player && grid[i][j + 2] == player && grid[i][j + 3] == player) {
                    return Optional.of(player);
                }
                // Check vertical
                if (i + 3 < gridRows && grid[i + 1][j] == player && grid[i + 2][j] == player && grid[i + 3][j] == player) {
                    return Optional.of(player);
                }
                // Check diagonal (up-right)
                if (i + 3 < gridRows && j + 3 < gridColumns && grid[i + 1][j + 1] == player && grid[i + 2][j + 2] == player && grid[i + 3][j + 3] == player) {
                    return Optional.of(player);
                }
                // Check diagonal (up-left)
                if (i + 3 < gridRows && j - 3 >= 0 && grid[i + 1][j - 1] == player && grid[i + 2][j - 2] == player && grid[i + 3][j - 3] == player) {
                    return Optional.of(player);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Check if the game board is full.
     *
     * @return True if the game board is full, otherwise false.
     */
    public boolean full() {
        return count == gridRows * gridColumns;
    }

    // Testing

    private int[][] copyGrid() {
        int[][] result = new int[gridRows][gridColumns];
        for (int i = 0; i < gridRows; i++) {
            System.arraycopy(grid[i], 0, result[i], 0, gridColumns);
        }
        return result;
    }

    /**
     * Make a move on the game board.
     *
     * @param player The player making the move.
     * @param column The column in which to make the move.
     * @return A new ConnectFourPosition object representing the game board after the move.
     * @throws RuntimeException If the position is full or consecutive moves are made by the same player.
     * @throws IllegalArgumentException If an invalid column is provided.
     */
    public ConnectFourPosition move(int player, int column) {
        if (full()) throw new RuntimeException("Position is full");
        if (player == last) throw new RuntimeException("Consecutive moves by the same player: " + player);
        if (column < 0 || column >= gridColumns) throw new IllegalArgumentException("Invalid column: " + column);

        int[][] newGrid = copyGrid();

        for (int i = gridRows - 1; i >= 0; i--) {
            if (newGrid[i][column] == -1) {
                newGrid[i][column] = player;
                return new ConnectFourPosition(newGrid, count + 1, player);
            }
        }
        throw new RuntimeException("Column is full: " + column);
    }

    /**
     * Get the list of possible moves for the given player.
     *
     * @param player The player for whom to get the possible moves.
     * @return A list of integers representing the possible columns where the player can make a move.
     * @throws RuntimeException If consecutive moves are made by the same player.
     */
    public List<Integer> moves(int player) {
        if (player == last) throw new RuntimeException("Consecutive moves by the same player: " + player);

        List<Integer> possibleMoves = new ArrayList<>();
        for (int column = 0; column < gridColumns; column++) {
            if (grid[0][column] == -1) {
                possibleMoves.add(column);
            }
        }
        return possibleMoves;
    }

    /**
     * Reflect the game board along the specified axis.
     *
     * @param axis The axis along which to reflect the game board (0 for horizontal, 1 for vertical).
     * @return A new ConnectFourPosition object representing the reflected game board.
     * @throws IllegalArgumentException If an invalid axis is provided.
     */
    public ConnectFourPosition reflect(int axis) {
        int[][] newGrid = copyGrid();
        switch (axis) {
            case 0:
                for (int i = 0; i < gridRows; i++) {
                    for (int j = 0; j < gridColumns / 2; j++) {
                        int temp = newGrid[i][j];
                        newGrid[i][j] = newGrid[i][gridColumns - 1 - j];
                        newGrid[i][gridColumns - 1 - j] = temp;
                    }
                }
                break;
            case 1:
                for (int i = 0; i < gridRows / 2; i++) {
                    int[] tempRow = newGrid[i];
                    newGrid[i] = newGrid[gridRows - 1 - i];
                    newGrid[gridRows - 1 - i] = tempRow;
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid axis for reflection: " + axis);
        }
        return new ConnectFourPosition(newGrid, count, last);
    }

    /**
     * Rotate the game board clockwise by 90 degrees.
     *
     * @return A new ConnectFourPosition object representing the rotated game board.
     */
    public ConnectFourPosition rotate() {
        int[][] newGrid = new int[gridColumns][gridRows];
        for (int i = 0; i < gridColumns; i++) {
            for (int j = 0; j < gridRows; j++) {
                newGrid[i][j] = grid[gridRows - 1 - j][i];
            }
        }
        return new ConnectFourPosition(newGrid, count, last);
    }
}
