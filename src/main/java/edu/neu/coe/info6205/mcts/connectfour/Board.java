package edu.neu.coe.info6205.mcts.connectfour;

public class Board {
    private final int ROWS = 6;
    private final int COLS = 7;
    private char[][] grid;

    public Board() {
        grid = new char[ROWS][COLS];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                grid[row][col] = '-';
            }
        }
    }

    public void printBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                System.out.print(grid[row][col] + " ");
            }
            System.out.println();
        }
        System.out.println("---------------");
    }

    public boolean addDisc(int col, char symbol) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (grid[row][col] == '-') {
                grid[row][col] = symbol;
                return true;
            }
        }
        return false; // Column is full
    }

    public boolean checkWin(char symbol) {
        // Check horizontally
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col <= COLS - 4; col++) {
                if (grid[row][col] == symbol &&
                        grid[row][col + 1] == symbol &&
                        grid[row][col + 2] == symbol &&
                        grid[row][col + 3] == symbol) {
                    return true;
                }
            }
        }

        // Check vertically
        for (int row = 0; row <= ROWS - 4; row++) {
            for (int col = 0; col < COLS; col++) {
                if (grid[row][col] == symbol &&
                        grid[row + 1][col] == symbol &&
                        grid[row + 2][col] == symbol &&
                        grid[row + 3][col] == symbol) {
                    return true;
                }
            }
        }

        // Check diagonally (positive slope)
        for (int row = 0; row <= ROWS - 4; row++) {
            for (int col = 0; col <= COLS - 4; col++) {
                if (grid[row][col] == symbol &&
                        grid[row + 1][col + 1] == symbol &&
                        grid[row + 2][col + 2] == symbol &&
                        grid[row + 3][col + 3] == symbol) {
                    return true;
                }
            }
        }

        // Check diagonally (negative slope)
        for (int row = 0; row <= ROWS - 4; row++) {
            for (int col = 3; col < COLS; col++) {
                if (grid[row][col] == symbol &&
                        grid[row + 1][col - 1] == symbol &&
                        grid[row + 2][col - 2] == symbol &&
                        grid[row + 3][col - 3] == symbol) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isFull() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (grid[row][col] == '-') {
                    return false;
                }
            }
        }
        return true;
    }
}
