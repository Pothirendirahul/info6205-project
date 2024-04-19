package edu.neu.coe.info6205.mcts.ConnectFour;

import java.util.*;
import edu.neu.coe.info6205.mcts.core.*;
import edu.neu.coe.info6205.mcts.ConnectFour.*;

/**
 * ConnectFour class implements the Game interface for the Connect Four game.
 */
public class ConnectFour implements Game<ConnectFour> {

    // Constants for player symbols
    public static final int X = 1;
    public static final int O = 0;
    public static final int BLANK = -1;

    private final Random random;

    /**
     * Constructor to initialize ConnectFour with a specific random seed.
     * @param random The random number generator.
     */
    public ConnectFour(Random random) {
        this.random = random;
    }

    /**
     * Default constructor that initializes ConnectFour with the current system time as seed.
     */
    public ConnectFour() {
        this(System.currentTimeMillis());
    }

    /**
     * Constructor to initialize ConnectFour with a specific seed.
     * @param seed The seed for the random number generator.
     */
    public ConnectFour(long seed) {
        this(new Random(seed));
    }

    /**
     * Creates the starting position for the Connect Four game.
     * @return The starting position.
     */
    static ConnectFourPosition startingPosition() {
        return ConnectFourPosition.parsePosition(
                ". . . . . . .\n" +
                        ". . . . . . .\n" +
                        ". . . . . . .\n" +
                        ". . . . . . .\n" +
                        ". . . . . . .\n" +
                        ". . . . . . .", BLANK);
    }

    /**
     * Starts a new Connect Four game.
     * @return The initial state of the game.
     */
    public State<ConnectFour> start() {
        return new ConnectFourState();
    }

    /**
     * Specifies which player makes the first move.
     * @return The player symbol for the opener.
     */
    @Override
    public int opener() {
        return X;
    }

    /**
     * Main method to run a Connect Four game.
     * @param args The command-line arguments (not used).
     */
    public static void main(String[] args) {
        State<ConnectFour> state = new ConnectFour().runGame();
        if (state.winner().isPresent()) {
            int winner = state.winner().get();
            if (winner == X) {
                System.out.println("ConnectFour: The Winner is: X");
            } else {
                System.out.println("ConnectFour: The Winner is: O");
            }
        } else {
            System.out.println("ConnectFour: Match is a Draw");
        }
    }

    /**
     * Runs a Connect Four game until completion.
     * @return The final state of the game.
     */
    public State<ConnectFour> runGame() {
        State<ConnectFour> state = start();
        MCTS mcts = new MCTS(new ConnectFourNode(state));
        boolean mctsWon = false;
        while (!state.isTerminal()) {
            if (mctsTurn) {
                mcts.run(100);
                System.out.println("Player: " + state.player() + " Move");
                Node<ConnectFour> bestMove = mcts.bestChild(MCTS.root);
                if (bestMove == null) {
                    throw new IllegalStateException("Best move is null");
                }
                state = bestMove.state();
                System.out.println(state);
                MCTS.root = new ConnectFourNode(state);
            } else {
                // Random move
                Collection<Move<ConnectFour>> legalMoves = state.moves(state.player());
                Move<ConnectFour> randomMove = legalMoves.stream().skip(random.nextInt(legalMoves.size())).findFirst().orElse(null);
                if (randomMove == null) {
                    throw new IllegalStateException("Random move is null");
                }
                state = state.next(randomMove);
                System.out.println("Player: " + state.player() + " Move");
                System.out.println(state);
            }
            mctsTurn = !mctsTurn;
        }
        System.out.println(state);
        if (state.winner().isPresent()) {
            int winner = state.winner().get();
            if (winner == X) {
                System.out.println("ConnectFour: Winner is: X (MCTS)");
                mctsWon = true;
            } else {
                System.out.println("ConnectFour: Winner is: O (Random)");
            }
        } else {
            System.out.println("ConnectFour: Draw");
        }
        return state;
    }

    private boolean mctsTurn = true; // Flag to alternate between MCTS and random moves

    /**
     * Inner class representing the state of a Connect Four game.
     */
    public class ConnectFourState implements State<ConnectFour> {

        private final ConnectFourPosition connectFourPosition;

        public ConnectFourState(ConnectFourPosition position) {
            this.connectFourPosition = position;
        }

        public ConnectFourState() {
            this(startingPosition());
        }

        @Override
        public ConnectFour game() {
            return ConnectFour.this;
        }

        @Override
        public boolean isTerminal() {
            return connectFourPosition.full() || connectFourPosition.winner().isPresent();
        }

        @Override
        public int player() {
            return switch (connectFourPosition.last) {
                case BLANK, O -> X;
                case X -> O;
                default -> BLANK;
            };
        }

        public ConnectFourPosition connectFourPosition() {
            return this.connectFourPosition;
        }

        @Override
        public Optional<Integer> winner() {
            return connectFourPosition.winner();
        }

        @Override
        public Collection<Move<ConnectFour>> moves(int player) {
            if (player == connectFourPosition.last) throw new IllegalStateException("Consecutive moves by same player: " + player);
            List<Integer> moves = connectFourPosition.moves(player);
            ArrayList<Move<ConnectFour>> legalMoves = new ArrayList<>();
            for (Integer col: moves) {
                if (connectFourPosition.grid[0][col] == BLANK) {
                    legalMoves.add(new ConnectFourMove(player, col));
                }
            }
            return legalMoves;
        }

        @Override
        public Random random() {
            return random;
        }

        @Override
        public State<ConnectFour> next(Move<ConnectFour> move) {
            ConnectFourMove connectFourMove = (ConnectFourMove) move;
            int col = connectFourMove.column();

            return new ConnectFourState(connectFourPosition.move(move.player(), col));
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int[] row : connectFourPosition.grid) {
                for (int cell : row) {
                    sb.append(cell == BLANK ? "." : (cell == X ? "X" : "O")).append(" ");
                }
                sb.append("\n");
            }
            return sb.toString();
        }

        @Override
        public Iterator<Move<ConnectFour>> moveIterator(int player) {
            return moves(player).iterator();
        }
    }
}
