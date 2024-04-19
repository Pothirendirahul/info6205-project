package edu.neu.coe.info6205.mcts.ConnectFour;

import edu.neu.coe.info6205.mcts.core.Move;

/**
 * ConnectFourMove class represents a move made by a player in the Connect Four game.
 */
public class ConnectFourMove implements Move<ConnectFour> {

    private final int player; // Player making the move (X or O)
    private final int column; // Column where the move is made

    /**
     * Constructor to create a new ConnectFourMove.
     * @param player The player making the move (X or O).
     * @param column The column where the move is made.
     */
    public ConnectFourMove(int player, int column) {
        this.player = player;
        this.column = column;
    }

    /**
     * Get the player making the move.
     * @return The player making the move (X or O).
     */
    @Override
    public int player() {
        return player;
    }

    /**
     * Get the column where the move is made.
     * @return The column where the move is made.
     */
    public int column() {
        return column;
    }
}
