package edu.neu.coe.info6205.mcts.ConnectFour;

import edu.neu.coe.info6205.mcts.core.Move;
import edu.neu.coe.info6205.mcts.core.Node;
import edu.neu.coe.info6205.mcts.core.State;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class MCTS {

    static Node<ConnectFour> root;

    public MCTS(Node<ConnectFour> root) {
        MCTS.root = root;
    }

    public void backPropagate(Node<ConnectFour> node, int result) {
        while (node != null) {
            int playout = node.playouts();
            node.setPlayouts(playout+1);
            int win = node.wins();
            if ((node.state().player() == 1 && result == 1) ||
                    (node.state().player() == 0 && result == -1)) {
                node.setWins(win + 2);

            } else if (result == 0) {
                node.setWins(win + 1);

            }
            node = node.getParent();
        }
    }

    public int simulate(Node<ConnectFour> node) {
        State<ConnectFour> state = node.state();
        Random random = new Random();
        while (!state.isTerminal()) {
            List<Move<ConnectFour>> moves = new ArrayList<>(state.moves(state.player()));
            Move<ConnectFour> move = moves.get(random.nextInt(moves.size()));
            state = state.next(move);
        }
        return state.winner().orElse(0);
    }

    public double ucb1(Node<ConnectFour> node) {
        double c = 1.44;
        int playouts = node.playouts();
        if (playouts == 0) {
            return Double.POSITIVE_INFINITY;
        }
        return node.wins() / (double) playouts +
                c * Math.sqrt(Math.log(node.getParent().playouts()) / (double) playouts);
    }


    public Node<ConnectFour> bestChild(Node<ConnectFour> node) {
        if (node.children().isEmpty()) {
            return null;
        }

        return node.children().stream()
                .max(Comparator.comparingDouble(this::ucb1))
                .orElseThrow(() -> new IllegalStateException("No best child found, but children list is not empty"));
    }

    public Node<ConnectFour> select(Node<ConnectFour> node) {
        while (!node.isLeaf()) {
            if (!node.children().isEmpty()) {
                node = bestChild(node);
            } else {
                node.explore();
                return node;
            }
        }
        return node;
    }

    public void run(int iterations) {
        for (int i = 0; i < iterations; i++) {
            Node<ConnectFour> node = select(root);
            int result = simulate(node);
            backPropagate(node, result);
        }
    }

}
