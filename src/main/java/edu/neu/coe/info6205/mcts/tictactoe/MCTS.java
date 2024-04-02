package edu.neu.coe.info6205.mcts.tictactoe;

import edu.neu.coe.info6205.mcts.core.Node;

/**
 * Class to represent a Monte Carlo Tree Search for TicTacToe.
 */
public class MCTS {

    public static void main(String[] args) {
        MCTS mcts = new MCTS(new TicTacToeNode(new TicTacToe().new TicTacToeState()));
        Node<TicTacToe> root = mcts.root;


        int iterations = 1200;
        mcts.runMCTS(iterations, root);
    }

    public MCTS(Node<TicTacToe> root) {
        this.root = root;
    }

    private final Node<TicTacToe> root;


    public void runMCTS(int iterations, Node<TicTacToe> root) {
        for (int i = 0; i < iterations; i++) {
            TicTacToeNode promisingNode = selectPromisingNode((TicTacToeNode) root);


            if (!promisingNode.state().isTerminal()) {
                promisingNode.explore();
            }

            TicTacToeNode nodeToExplore = promisingNode;
            if (!promisingNode.children().isEmpty()) {
                nodeToExplore = promisingNode.randomChild();
            }
           // int playoutResult = simulateRandomPlayout(nodeToExplore);

            // Backpropagation phase
            //backPropagate(nodeToExplore, playoutResult);
        }
    }

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

            currentNode = nodeWithMaxUCB1;
        }
        return currentNode;
    }


    private double calculateUCB1(TicTacToeNode node) {
        double c = Math.sqrt(2);
        int ni = node.playouts();
        int wi = node.wins();
        int Ni = node.parent() != null ? node.parent().playouts() : 1;

        if (ni == 0) {
            return Double.MAX_VALUE;
        }

        return ((double) wi / (double) ni) + c * Math.sqrt((2 * Math.log(Ni)) / ni);
    }



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
