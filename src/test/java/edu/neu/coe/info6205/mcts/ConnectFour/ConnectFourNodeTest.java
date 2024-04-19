package edu.neu.coe.info6205.mcts.ConnectFour;

import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectFourNodeTest {

    @Test
    public void testWinsAndPlayouts() {
        ConnectFour.ConnectFourState state = new ConnectFour().new ConnectFourState(ConnectFourPosition.parsePosition(" . . . . . .\n . . . . . .\n . . . . . .\n . . . . . .\n . . . . . .\n X X X X . .",ConnectFour.X));
        ConnectFourNode node = new ConnectFourNode(state);
        assertTrue("Node should be a leaf", node.isLeaf());
        assertEquals("Wins should be 2", 2, node.wins());
        assertEquals("Playouts should be 1", 1, node.playouts());
    }

    @Test
    public void testState() {
        ConnectFour.ConnectFourState state = new ConnectFour().new ConnectFourState();
        ConnectFourNode node = new ConnectFourNode(state);
        assertEquals("Node state should match provided state", state, node.state());
    }

    @Test
    public void testWhite() {
        ConnectFour.ConnectFourState state = new ConnectFour().new ConnectFourState();
        ConnectFourNode node = new ConnectFourNode(state);
        assertTrue("Player should be white", node.white());
    }

    @Test
    public void testChildren() {
        ConnectFour.ConnectFourState state = new ConnectFour().new ConnectFourState();
        ConnectFourNode node = new ConnectFourNode(state);
        assertTrue("Node should have no children", node.children().isEmpty());
    }

    @Test
    public void testAddChild() {
        ConnectFour.ConnectFourState state = new ConnectFour().new ConnectFourState();
        ConnectFourNode parentNode = new ConnectFourNode(state);
        ConnectFourNode childNode = new ConnectFourNode(state);
        parentNode.addChild(state);
        assertFalse("Parent node should have children", parentNode.children().isEmpty());
        assertEquals("Child node state should match added state", childNode.state(), parentNode.children().iterator().next().state());
    }

    @Test
    public void testBackPropagate() {
        ConnectFour.ConnectFourState state = new ConnectFour().new ConnectFourState();
        ConnectFourNode parentNode = new ConnectFourNode(state);

        ConnectFourNode child1 = new ConnectFourNode(state);
        child1.addWins(3);
        child1.incrementPlayouts();
        child1.incrementPlayouts();

        ConnectFourNode child2 = new ConnectFourNode(state);
        child2.addWins(1);
        child2.incrementPlayouts();

        parentNode.addChild(state);
        parentNode.addChild(state);

        parentNode.children().add(child1);
        parentNode.children().add(child2);

        parentNode.backPropagate();

        assertEquals("Playouts should be sum of children's playouts", 3, parentNode.playouts());
        assertEquals("Wins should be sum of children's wins", 4, parentNode.wins());
    }
}
//