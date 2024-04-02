package edu.neu.coe.info6205.mcts.tictactoe;

import edu.neu.coe.info6205.mcts.core.Node;

/**
 * Class to represent a Monte Carlo Tree Search for TicTacToe.
 */
public class MCTS {

    public static void main(String[] args) {
        MCTS mcts = new MCTS(new TicTacToeNode(new TicTacToe().new TicTacToeState()));
        Node<TicTacToe> root = mcts.root;

        // This is where you process the MCTS to try to win the game.
        int iterations = 1000; // Number of iterations for MCTS
        mcts.runMCTS(iterations, root);
    }

    public MCTS(Node<TicTacToe> root) {
        this.root = root;
    }

    private final Node<TicTacToe> root;

    // Method to run the Monte Carlo Tree Search algorithm
    public void runMCTS(int iterations, Node<TicTacToe> root) {
        for (int i = 0; i < iterations; i++) {
            // Selection phase
            TicTacToeNode promisingNode = selectPromisingNode((TicTacToeNode) root);

            // Expansion phase
            if (!promisingNode.state().isTerminal()) {
                promisingNode.explore();
            }

            // Simulation phase
            TicTacToeNode nodeToExplore = promisingNode;
            if (!promisingNode.children().isEmpty()) {
                nodeToExplore = promisingNode.randomChild();
            }
           // int playoutResult = simulateRandomPlayout(nodeToExplore);

            // Backpropagation phase
            //backPropagate(nodeToExplore, playoutResult);
        }
    }

    // Method to select the most promising node according to UCB1
    private TicTacToeNode selectPromisingNode(TicTacToeNode rootNode) {
        TicTacToeNode currentNode = rootNode;
        while (!currentNode.children().isEmpty()) {
            double maxUCB1 = Double.MIN_VALUE;
            TicTacToeNode nodeWithMaxUCB1 = null;
            for (Node<TicTacToe> child : currentNode.children()) {
                TicTacToeNode ticTacToeChild = (TicTacToeNode) child;
                double ucb1Value = calculateUCB1(ticTacToeChild);
                if (ucb1Value > maxUCB1) {
                    maxUCB1 = ucb1Value;
                    nodeWithMaxUCB1 = ticTacToeChild;
                }
            }
            // Update the current node to the most promising child node
            currentNode = nodeWithMaxUCB1;
        }
        return currentNode;
    }

    // Method to calculate the UCB1 value for a node
    private double calculateUCB1(TicTacToeNode node) {
        double c = Math.sqrt(2);
        int ni = node.playouts(); // Number of simulations for the node
        int wi = node.wins(); // Number of wins for the node
        int Ni = node.parent() != null ? node.parent().playouts() : 1; // Total simulations for the parent node, avoid division by zero

        if (ni == 0) {
            return Double.MAX_VALUE; // Always select unvisited nodes first
        }

        return ((double) wi / (double) ni) + c * Math.sqrt((2 * Math.log(Ni)) / ni);
    }


    // Method to backpropagate the result of a playout through the tree
    private void backPropagate(TicTacToeNode nodeToExplore, int playerNo) {
        TicTacToeNode tempNode = nodeToExplore;
        while (tempNode != null) {
            tempNode.setPlayouts(tempNode.playouts() + 1);
            if (tempNode.state().player() == playerNo) {
                tempNode.setWins(tempNode.wins() + 1);
            }
            tempNode = (TicTacToeNode) tempNode.parent(); // Move to the parent node
        }
    }
}
