
package edu.neu.coe.info6205.mcts.tictactoe;

import edu.neu.coe.info6205.mcts.core.Move;

import edu.neu.coe.info6205.mcts.core.Node;

import edu.neu.coe.info6205.mcts.core.State;



import java.util.ArrayList;

import java.util.Comparator;

import java.util.List;

import java.util.Random;



/**

 * Class to represent a Monte Carlo Tree Search for TicTacToe.

 */

public class MCTS {

    public static TicTacToeNode root;

    public void run(int iterations) {

        for (int i = 0; i < iterations; i++) {

            Node<TicTacToe> node = select(root);

            int result = simulate(node);

            backPropagate(node, result);

        }

    }



    Node<TicTacToe> select(Node<TicTacToe> node) {

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



    Node<TicTacToe> bestChild(Node<TicTacToe> node) {

        if (node.children().isEmpty()) {

            return null;

        }

        return node.children().stream()

                .max(Comparator.comparingDouble(this::ucb1))

                .orElseThrow(() -> new IllegalStateException("No best child found, but children list is not empty"));

    }



    private double ucb1(Node<TicTacToe> node) {

        double c = Math.sqrt(2);

        return node.wins() / (double) node.playouts() +

                c * Math.sqrt(Math.log(node.getParent().playouts()) / (double) node.playouts());

    }



    int simulate(Node<TicTacToe> node) {

        State<TicTacToe> state = node.state();

        Random random = new Random();

        while (!state.isTerminal()) {

            List<Move<TicTacToe>> moves = new ArrayList<>(state.moves(state.player()));

            Move<TicTacToe> move = moves.get(random.nextInt(moves.size()));

            state = state.next(move);

        }

        return state.winner().orElse(-1);

    }



    void backPropagate(Node<TicTacToe> node, int result) {

        while (node != null) {

            int playout = node.playouts();

            node.setPlayouts(playout + 1);

            int win = node.wins();

            if (result == 1) {

                node.setWins(win + 1);

            }

            node = node.getParent();

        }

    }



    public MCTS(TicTacToeNode root) {

        MCTS.root = root;

    }

}
