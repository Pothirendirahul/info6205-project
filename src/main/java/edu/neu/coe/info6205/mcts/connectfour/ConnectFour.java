package edu.neu.coe.info6205.mcts.ConnectFour;

import edu.neu.coe.info6205.mcts.core.Game;
import edu.neu.coe.info6205.mcts.core.Move;
import edu.neu.coe.info6205.mcts.core.Node;
import edu.neu.coe.info6205.mcts.core.State;
import edu.neu.coe.info6205.mcts.ConnectFour.MCTS;
import edu.neu.coe.info6205.mcts.ConnectFour.ConnectFourNode;
import edu.neu.coe.info6205.mcts.ConnectFour.ConnectFourPosition;
import edu.neu.coe.info6205.mcts.ConnectFour.ConnectFourMove;

import java.util.*;

public class ConnectFour implements Game<ConnectFour> {

    public static final int X = 1;
    public static final int O = 0;
    public static final int BLANK = -1;

    private final Random random;

    public ConnectFour(Random random) {
        this.random = random;
    }

    public ConnectFour() {
        this(System.currentTimeMillis());
    }

    public ConnectFour(long seed) {
        this(new Random(seed));
    }

    static ConnectFourPosition startingPosition() {
        return ConnectFourPosition.parsePosition(
                ". . . . . . .\n" +
                        ". . . . . . .\n" +
                        ". . . . . . .\n" +
                        ". . . . . . .\n" +
                        ". . . . . . .\n" +
                        ". . . . . . .", BLANK);
    }

    public State<ConnectFour> runGame() {
        State<ConnectFour> state = start();
        // Initialize MCTS with the starting state
        MCTS mcts = new MCTS(new ConnectFourNode(state));
        while (!state.isTerminal()) {
            if (mctsTurn) {
                mcts.run(100);
                System.out.println("Player: " + state.player() + " Move");
                Node<ConnectFour> bestMove = mcts.bestChild(MCTS.root);
                if (bestMove == null) {
                    throw new Error("Best move is null");
                }
                state = bestMove.state();
                System.out.println(state);
                MCTS.root = new ConnectFourNode(state);
            } else {
                // Random move
                Collection<Move<ConnectFour>> legalMoves = state.moves(state.player());
                Move<ConnectFour> randomMove = legalMoves.stream().skip(random.nextInt(legalMoves.size())).findFirst().orElse(null);
                if (randomMove == null) {
                    throw new Error("Random move is null");
                }
                state = state.next(randomMove);
                System.out.println("Player: " + state.player() + " Move");
                System.out.println(state);
            }
            mctsTurn = !mctsTurn; // Switch turn
        }
        System.out.println(state);

        return state;
    }

    public static void main(String[] args) {
        State<ConnectFour> state = new ConnectFour().runGame();
        if (state.winner().isPresent()) {
            int winner = state.winner().get();
            if (winner == X) {
                System.out.println("ConnectFour: Winner is: X (MCTS)");
            } else {
                System.out.println("ConnectFour: Winner is: O (Random)");
            }
        } else {
            System.out.println("ConnectFour: Draw");
        }
    }

    @Override
    public State<ConnectFour> start() {
        return new ConnectFourState();
    }

    @Override
    public int opener() {
        return X;
    }

    private boolean mctsTurn = true; // Flag to alternate between MCTS and random moves

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
        public Random random() {
            return random;
        }

        @Override
        public Collection<Move<ConnectFour>> moves(int player) {
            if (player == connectFourPosition.last) throw new RuntimeException("Consecutive moves by same player: " + player);
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
        public State<ConnectFour> next(Move<ConnectFour> move) {
            ConnectFourMove connectFourMove = (ConnectFourMove) move;
            int col = connectFourMove.column();

            return new ConnectFourState(connectFourPosition.move(move.player(), col));
        }

        @Override
        public Iterator<Move<ConnectFour>> moveIterator(int player) {
            return moves(player).iterator();
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
    }
}