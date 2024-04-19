package edu.neu.coe.info6205.mcts.ConnectFour;

import org.junit.Test;
import static org.junit.Assert.*;

public class MCTSTest {

    @Test
    public void testInitialization() {
        ConnectFour connectFourGame = new ConnectFour();
        ConnectFourNode root = new ConnectFourNode(connectFourGame.start());
        MCTS mcts = new MCTS(root);
        assertNotNull("MCTS instance should not be null", mcts);
        assertNotNull("Root  should not be null", root);
    }

    @Test
    public void testBackPropagate() {
        ConnectFour connectFourGame = new ConnectFour();
        ConnectFourNode root = new ConnectFourNode(connectFourGame.start());
        MCTS mcts = new MCTS(root);
        ConnectFourNode childNode = new ConnectFourNode(connectFourGame.start());
        mcts.backPropagate(childNode, 1); // Simulate a win
        assertEquals("Child node should have 2 wins after backpropagation", 2, childNode.wins());
    }

    @Test
    public void testMainRunMethod() {
        ConnectFour connectFourGame = new ConnectFour();
        ConnectFourNode root = new ConnectFourNode(connectFourGame.start());
        MCTS mcts = new MCTS(root);
        mcts.run(200);
        assertNotNull("Best child node should not be null after running MCTS", mcts.bestChild(root));
    }
}
