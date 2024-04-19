package edu.neu.coe.info6205.mcts.ConnectFour;
import edu.neu.coe.info6205.mcts.ConnectFour.ConnectFour;
import edu.neu.coe.info6205.mcts.ConnectFour.ConnectFourPosition;
import edu.neu.coe.info6205.mcts.core.Move;
import edu.neu.coe.info6205.mcts.core.State;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;

import static com.phasmidsoftware.number.core.FP.fail;
import static org.junit.Assert.*;

public class ConnectFourTest {

    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testMainMethod() {
        ConnectFour.main(null);
        String output = outContent.toString().trim();
        assertTrue(output.contains("ConnectFour: Winner is:") || output.contains("ConnectFour: Draw"));
    }

    @Test
    public void runGame() {
        long seed = 8L;
        ConnectFour target = new ConnectFour(seed); // games run here will all be deterministic.
        State<ConnectFour> state = target.runGame();
        Optional<Integer> winner = state.winner();
        if (winner.isPresent()) assertEquals(Integer.valueOf(ConnectFour.X), winner.get());
        else fail("no winner");
    }

    @Test
    public void testPlayerAlternation() {
        ConnectFour game = new ConnectFour();
        State<ConnectFour> initialState = game.start();
        int firstPlayer = initialState.player();

        State<ConnectFour> nextState = initialState.next(initialState.chooseMove(firstPlayer));
        assertNotEquals(firstPlayer, nextState.player());
    }
    @Test
    public void testTerminalState() {
        ConnectFour game = new ConnectFour();
        State<ConnectFour> state = game.runGame();
        assertTrue(state.isTerminal());
    }

    @Test
    public void testConnectFourPosition() {
        ConnectFour connectFour = new ConnectFour();
        State<ConnectFour> state = connectFour.start();
        ConnectFourPosition position = ((ConnectFour.ConnectFourState) state).connectFourPosition();
        assertNotNull(position);

        assertEquals(ConnectFour.BLANK, position.grid[0][0]);
        assertEquals(ConnectFour.BLANK, position.grid[1][1]);
    }

    @Test
    public void testIsTerminal() {
        ConnectFour connectFour = new ConnectFour();
        State<ConnectFour> state = connectFour.start();
        assertFalse(state.isTerminal());
    }

    @Test
    public void testPlayer() {
        ConnectFour connectFour = new ConnectFour();
        State<ConnectFour> state = connectFour.start();
        assertEquals(ConnectFour.X, state.player());
    }

    @Test
    public void testWinner() {
        ConnectFour connectFour = new ConnectFour();
        State<ConnectFour> state = connectFour.start();
        assertFalse(state.winner().isPresent());
    }

    @Test
    public void testRandom() {
        ConnectFour connectFour = new ConnectFour();
        State<ConnectFour> state = connectFour.start();
        Random random = state.random();
        assertNotNull(random);
    }

    @Test
    public void testMoves() {
        ConnectFour connectFour = new ConnectFour();
        State<ConnectFour> state = connectFour.start();
        Collection<Move<ConnectFour>> moves = state.moves(state.player());
        assertFalse(moves.isEmpty());
    }

    @Test
    public void testNext() {
        ConnectFour connectFour = new ConnectFour();
        State<ConnectFour> state = connectFour.start();
        Collection<Move<ConnectFour>> moves = state.moves(state.player());
        Move<ConnectFour> move = moves.iterator().next();
        State<ConnectFour> nextState = state.next(move);
        assertNotNull(nextState);
    }

    @Test
    public void testMoveIterator() {
        ConnectFour connectFour = new ConnectFour();
        State<ConnectFour> state = connectFour.start();
        int player = state.player();
        assertNotNull(state.moveIterator(player));
    }


}