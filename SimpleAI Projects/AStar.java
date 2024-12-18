import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

/**
 * Node Subclass
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
    int cost2Go;

}

/**
 * A star Search method
 */
public final class AStar extends Node {

    /**
     * Default constructor--private to prevent instantiation.
     */
    private AStar() {
        // no code needed here
    }

    /**
     * Finds Child Node produced by given action.
     *
     * @param parent
     *            Node to branch from
     * @param move
     *            the direction to move in
     * @return the child node
     */
    public static Node childNode(Node parent, Actions move) {
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
     * Method to Write the State of a node in the given format.
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
     * A* Search of the puzzle.
     *
     * @param selected
     *            Stack of the solution
     * @param initialState
     *            Node to branch from
     * @param goalState
     *            the endpoint
     * @param allNodes
     *            the frontier of Nodes
     */
    public static void aStarSearch(Stack<Node> selected, Node initialState, int[] goalState, ArrayList<Node> allNodes) {
        if (!Arrays.equals(initialState.state, goalState)) {
            Node bestNode;
            Node child1 = childNode(initialState, Actions.up);
            Node child2 = childNode(initialState, Actions.down);
            Node child3 = childNode(initialState, Actions.left);
            Node child4 = childNode(initialState, Actions.right);

            if (child1 != null) {
                child1.cost2Go = cost2Go(child1, goalState);
                child1.pathCost = initialState.pathCost + 1;
                allNodes.add(child1);

            }
            if (child2 != null) {
                child2.cost2Go = cost2Go(child2, goalState);
                child2.pathCost = initialState.pathCost + 1;
                allNodes.add(child2);

            }
            if (child3 != null) {
                child3.cost2Go = cost2Go(child3, goalState);
                child3.pathCost = initialState.pathCost + 1;
                allNodes.add(child3);

            }
            if (child4 != null) {
                child4.cost2Go = cost2Go(child4, goalState);
                child4.pathCost = initialState.pathCost + 1;
                allNodes.add(child4);

            }
            bestNode = getSmallest(allNodes);
            allNodes.remove(bestNode);
            aStarSearch(selected, bestNode, goalState, allNodes);

        }
        //Make stack of the found solution
        if (Arrays.equals(initialState.state, goalState)) {

            makeStack(selected, initialState);
        }
    }

    /**
     * builds the final Stack of the found path.
     *
     * @param selected
     *            stack of nodes in the final path
     * @param fin
     *            Node to be pushed on to the stack
     */
    public static void makeStack(Stack<Node> selected, Node fin) {
        selected.push(fin);
        if (fin.parentNode != null) {
            fin = fin.parentNode;
            makeStack(selected, fin);
        }
    }

    /**
     * returns smallest cost.
     *
     * @param allNodes
     *            the frontier of nodes
     * @return smallest cost
     */
    public static Node getSmallest(ArrayList<Node> allNodes) {
        Node bestNode = allNodes.get(0);
        int lowestCost = allNodes.get(0).cost2Go;
        for (int k = 1; k < allNodes.size(); k++) {
            if (allNodes.get(k).cost2Go < lowestCost) {
                bestNode = allNodes.get(k);
                lowestCost = bestNode.cost2Go;
            }
        }

        return bestNode;
    }

    /**
     * writes the node states out of the Stack.
     *
     * @param selected
     *            solution of the search
     */
    public static void stackWriter(Stack<Node> selected) {
        while (!selected.empty()) {
            Node next = selected.pop();
            writeState(next);
        }
    }

    /**
     * Finds number of misplaced numbers in the state.
     *
     * @param toFind
     *            Node to check how far off it is
     * @param goalState
     *            destination state
     * @return number of misplaced values
     */
    public static int cost2Go(Node toFind, int[] goalState) {
        int misplaced = 0;
        for (int k = 0; k < goalState.length; k++) {
            if (toFind.state[k] != goalState[k]) {
                misplaced += heuristic(toFind, k, goalState);
            }
        }
        return misplaced;
    }

    /**
     * Finds the heuristic cost of a single value in the node state.
     * 
     * @param toFind
     *            Node state to compare
     * @param index1
     *            index of the value in question
     * @param goalState
     *            final state
     * @return heuristic cost of the value.
     */
    public static int heuristic(Node toFind, int index1, int[] goalState) {
        int index2 = 0;
        for (int k = 0; k < goalState.length; k++) {
            if (goalState[k] == toFind.state[index1]) {
                index2 = k;
            }
        }
        int column1 = index1 % 3;
        int column2 = index2 % 3;
        int row1 = index1 / 3;
        int row2 = index2 / 3;
        int travel = 0;
        travel = Math.abs(row1 - row2) + Math.abs(column1 - column2);
        return travel;
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) {

        int[] goalState = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
        ArrayList<Node> allNodes = new ArrayList<Node>();
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
        initialState.cost2Go = cost2Go(initialState, goalState);
        aStarSearch(finished, initialState, goalState, allNodes);
        stackWriter(finished);

    }

}
