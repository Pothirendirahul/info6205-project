package edu.neu.coe.info6205.mcts.ConnectFour;

import edu.neu.coe.info6205.mcts.core.Node;
import edu.neu.coe.info6205.mcts.core.State;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;

/**
 * ConnectFourNode class represents a node in the MCTS (Monte Carlo Tree Search) algorithm for the Connect Four game.
 */
public class ConnectFourNode implements Node<ConnectFour> {

    private final State<ConnectFour> state; // The state represented by this node
    private final ArrayList<Node<ConnectFour>> children; // List of child nodes
    private int wins; // Number of wins
    private int playouts; // Number of playouts

    private final Random random = new Random(); // Random number generator
    private Node<ConnectFour> parent; // Parent node

    /**
     * Constructor to create a new ConnectFourNode with the specified state.
     * @param state The state represented by this node.
     */
    public ConnectFourNode(State<ConnectFour> state) {
        this.state = state;
        this.children = new ArrayList<>();
        initializeNodeData(); // Initialize node data based on the state
    }

    /**
     * Check if the node is a leaf node.
     * @return True if the node is a leaf node, otherwise false.
     */
    @Override
    public boolean isLeaf() {
        return state().isTerminal();
    }

    /**
     * Get the state represented by this node.
     * @return The state represented by this node.
     */
    @Override
    public State<ConnectFour> state() {
        return state;
    }

    /**
     * Check if the player to move in the state represented by this node is white (the opener).
     * @return True if the player is white (the opener), otherwise false.
     */
    @Override
    public boolean white() {
        return state.player() == state.game().opener();
    }

    /**
     * Get the list of child nodes.
     * @return The list of child nodes.
     */
    @Override
    public Collection<Node<ConnectFour>> children() {
        return children;
    }

    /**
     * Explore the node.
     */
    @Override
    public void explore() {
        Node.super.explore();
    }

    /**
     * Add a child node with the specified state.
     * @param state The state for the child node.
     */
    @Override
    public void addChild(State<ConnectFour> state) {
        ConnectFourNode child = new ConnectFourNode(state);
        child.setParent(this);  // Set the parent of the child
        children.add(child);
    }

    /**
     * Backpropagate the results from the children nodes.
     */
    @Override
    public void backPropagate() {
        playouts = 0;
        wins = 0;
        for (Node<ConnectFour> child : children) {
            wins += child.wins();
            playouts += child.playouts();
        }
    }

    /**
     * Get the number of wins.
     * @return The number of wins.
     */
    @Override
    public int wins() {
        return wins;
    }

    /**
     * Set the number of wins.
     * @param wins The number of wins to set.
     */
    @Override
    public void setWins(int wins) {
        this.wins = wins;
    }

    /**
     * Get the number of playouts.
     * @return The number of playouts.
     */
    @Override
    public int playouts() {
        return playouts;
    }

    /**
     * Set the number of playouts.
     * @param playout The number of playouts to set.
     */
    @Override
    public void setPlayouts(int playout) {
        this.playouts = playout;
    }

    /**
     * Get the parent node.
     * @return The parent node.
     */
    @Override
    public Node<ConnectFour> getParent() {
        return parent;
    }

    /**
     * Set the parent node.
     * @param parent The parent node to set.
     */
    @Override
    public void setParent(Node<ConnectFour> parent) {
        this.parent = parent;
    }

    /**
     * Initialize node data based on the state.
     */
    private void initializeNodeData() {
        if (isLeaf()) {
            playouts = 1;
            Optional<Integer> winner = state.winner();
            if (winner.isPresent())
                wins = 2; // A win is worth 2 points.
            else
                wins = 1; // A draw is worth 1 point.
        }
    }

    /**
     * Increment the number of playouts.
     */
    public void incrementPlayouts() {
        this.playouts++;
    }

    /**
     * Add wins to the total number of wins.
     * @param wins The number of wins to add.
     */
    public void addWins(int wins) {
        this.wins += wins;
    }
}
