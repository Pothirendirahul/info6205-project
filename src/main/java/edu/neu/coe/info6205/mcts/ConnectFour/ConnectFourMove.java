package edu.neu.coe.info6205.mcts.ConnectFour;

import edu.neu.coe.info6205.mcts.core.Move;

public class ConnectFourMove implements Move<ConnectFour> {

    private final int player;
    private final int column;

    public ConnectFourMove(int player, int column) {
        this.player = player;
        this.column = column;
    }


    @Override
    public int player() {
        return player;
    }

    public int column() {
        return column;
    }
}