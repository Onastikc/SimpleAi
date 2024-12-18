import java.util.Arrays;
import java.util.Stack;

/**
 * Node Subclass
 *
 * @author Albert Maah
 *
 */
class Node {
    public enum Actions {
        up, down, left, right
    }

    int[] state = new int[9];
    Node parentNode;
    Actions action;
    int pathCost;
    int depth;
    private int cost2Go;

}

/**
 * Depth First Search of an 8 puzzle.
 * 
 * @author Albert Maah
 */
public final class DepthFirstSearch extends Node {

    /**
     * Default constructor--private to prevent instantiation.
     */
    private DepthFirstSearch() {
        // no code needed here
    }

    /**
     * Finds Child Node.
     *
     * @param parent
     *            Node to branch
     * @param move
     *            direction to move
     * @return child or null if illegal move
     */
    public static Node childNode(Node parent, Actions move) {
        //setting up the child node
        Node child = new Node();
        int blankIndex = 0;
        int column = 0;
        int nextIndex = 0;
        child.depth = parent.depth + 1;
        child.parentNode = parent;
        System.arraycopy(parent.state, 0, child.state, 0, parent.state.length);
        for (int k = 0; k < parent.state.length; k++) {
            if (parent.state[k] == 0) {
                blankIndex = k;
            }
        }
        column = blankIndex % 3;
        //check that move is legal and not repetitive
        switch (move) {
            case up:
                nextIndex = blankIndex - 3;
                child.action = Actions.up;
                if (parent.action == Actions.down) {
                    nextIndex = -1;
                }
                break;
            case down:
                nextIndex = blankIndex + 3;
                child.action = Actions.down;
                if (parent.action == Actions.up) {
                    nextIndex = -1;
                }
                break;
            case left:
                if (column == 0 || parent.action == Actions.right) {
                    nextIndex = -1;
                } else {
                    nextIndex = blankIndex - 1;
                }
                child.action = Actions.left;
                break;
            case right:
                if (column == 2 || parent.action == Actions.left) {
                    nextIndex = -1;
                } else {
                    nextIndex = blankIndex + 1;
                }
                child.action = Actions.right;
                break;
        }
        if (nextIndex >= 0 && nextIndex < 9) {
            child.state[blankIndex] = child.state[nextIndex];
            child.state[nextIndex] = 0;
        } else {
            child = null;
        }

        return child;
    }

    /**
     * Method to Write the State of a node.
     */
    public static void writeState(Node node2Write) {
        System.out.print("Step " + node2Write.pathCost + " : ");
        if (node2Write.action == null) {
            System.out.println("None");
        } else {
            System.out.println(node2Write.action);
        }
        for (int k = 0; k < node2Write.state.length; k++) {
            if (k % 3 == 0 && k != 0) {
                System.out.println();
            }
            System.out.print(node2Write.state[k] + " ");
        }
        System.out.println();
    }

    /**
     * Uses recursion to find the solution to the puzzle in IDDFS.
     * 
     * @param selected
     *            Solution
     * @param initialState
     *            node to branch from
     * @param goalState
     *            destination
     * @param actionsMade
     *            last action made
     */
    public static void depthSearch(Stack<Node> selected, Node initialState, int[] goalState, int actionsMade) {
        int stepLimit = 12;
        if (!Arrays.equals(initialState.state, goalState) && initialState.pathCost < stepLimit) {

            Node child1 = childNode(initialState, Actions.up);
            Node child2 = childNode(initialState, Actions.down);
            Node child3 = childNode(initialState, Actions.left);
            Node child4 = childNode(initialState, Actions.right);
            //finds legal children
            if (child1 != null) {
                child1.pathCost = initialState.pathCost + 1;
                depthSearch(selected, child1, goalState, child1.pathCost);
            }

            if (child2 != null && selected.empty()) {
                child2.pathCost = initialState.pathCost + 1;
                depthSearch(selected, child2, goalState, child2.pathCost);
            }

            if (child3 != null && selected.empty()) {
                child3.pathCost = initialState.pathCost + 1;
                depthSearch(selected, child3, goalState, child3.pathCost);
            }

            if (child4 != null && selected.empty()) {
                child4.pathCost = initialState.pathCost + 1;
                depthSearch(selected, child4, goalState, child4.pathCost);
            }
        }
        if (!selected.empty() || Arrays.equals(initialState.state, goalState)) {
            selected.push(initialState);
        }

    }

    /**
     * writes the node states out of the Stack
     *
     * @param selected
     */
    public static void stackWriter(Stack<Node> selected) {
        while (!selected.empty()) {
            Node next = selected.pop();
            writeState(next);
        }
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) {

        int[] goalState = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
        Node initialState = new Node();
        Stack<Node> finished = new Stack<Node>();
        initialState.state[0] = 0;
        initialState.state[1] = 3;
        initialState.state[2] = 5;
        initialState.state[3] = 4;
        initialState.state[4] = 2;
        initialState.state[5] = 7;
        initialState.state[6] = 6;
        initialState.state[7] = 8;
        initialState.state[8] = 1;

        int actionsMade = 0;
        initialState.pathCost = actionsMade;

        depthSearch(finished, initialState, goalState, actionsMade);
        stackWriter(finished);

    }

}
