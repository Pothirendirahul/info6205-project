package edu.neu.coe.info6205.mcts.tictactoe;

import static org.junit.Assert.*;

import static org.junit.Assert.*;
import org.junit.Test;

import edu.neu.coe.info6205.mcts.core.Node;

public class MCTSTest {


    @Test
    public void testSelectMethod() {

        TicTacToe game = new TicTacToe();
        TicTacToeNode rootNode = new TicTacToeNode(game.start());
        rootNode.explore();

        MCTS mcts = new MCTS(rootNode);
        Node<TicTacToe> selectedNode = mcts.select(rootNode);

        assertNotNull(selectedNode);
    }

    @Test
    public void testBestChildSelection() {
        TicTacToeNode rootNode = new TicTacToeNode(new TicTacToe().start());
        rootNode.explore();
        Node<TicTacToe> bestChild = new MCTS(rootNode).bestChild(rootNode);
        assertNotNull(bestChild);
        assertTrue(rootNode.children().contains(bestChild));
    }

    @Test
    public void testSimulationAndBackPropagation() {
        TicTacToeNode node = new TicTacToeNode(new TicTacToe().start());
        MCTS mcts = new MCTS(node);
        int initialPlayouts = node.playouts();
        int result = mcts.simulate(node);
        mcts.backPropagate(node, result);
        assertEquals(initialPlayouts + 1, node.playouts());
    }

}