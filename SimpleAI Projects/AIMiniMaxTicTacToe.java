import java.util.Stack;

/**
 * Node class for each state of the tic tac toe board.
 */
class Node {
    public enum Turn {
        x, o
    }

    Node parentNode = null;
    String[] state = new String[9];
    int utility;
    Turn move;
    int movesLeft;
}

public final class AIMiniMaxTicTacToe extends Node {

    /**
     * Default constructor--private to prevent instantiation.
     */
    private AIMiniMaxTicTacToe() {
        // no code needed here
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) {
        Node initialState = new Node();
        Stack<Node> finished = new Stack<Node>();
        initialState.state[0] = ".";
        initialState.state[1] = ".";
        initialState.state[2] = ".";
        initialState.state[3] = ".";
        initialState.state[4] = ".";
        initialState.state[5] = ".";
        initialState.state[6] = ".";
        initialState.state[7] = ".";
        initialState.state[8] = ".";

        initialState.move = Turn.x;
        initialState.movesLeft = 9;
        Node finalized = findMax(initialState);
        createStack(finalized, finished);
        printStack(finished);
    }

    /**
     * Finds the Max/best move for X
     * 
     * @param state
     *            the current state of the board
     * @return the best move for X
     */
    public static Node findMax(Node state) {
        Node max = null;
        int[] minimax = new int[9];
        //look at every possible move
        for (int k = 0; k < state.state.length; k++) {
            if (state.state[k] == ".") {
                //create a state with a move of the next open spot
                Node next = createChild(state, k);
                Node mins;
                int win = checkWin(next);
                if (win == 1) {
                    mins = findMin(next);
                    next = mins;
                } else {
                    next.utility = win;
                }
                if (state.movesLeft == 9) {
                    minimax[k] = next.utility;
                }
                //find the highest utility value
                if (max == null) {
                    max = next;
                } else {
                    if (max.utility < next.utility) {
                        max = next;
                    }
                }

            }
        }
        if (state.movesLeft == 9) {
            printMiniMax(minimax);
        }
        return max;
    }

    /**
     * Finds the Min/ best move for O.
     * 
     * @param state
     *            current state of the board
     * @return best move for O
     */
    public static Node findMin(Node state) {
        Node min = null;
        for (int k = 0; k < state.state.length; k++) {
            if (state.state[k] == ".") {
                //Creates a child in the next open spot
                Node next = createChild(state, k);
                Node maxes;
                int win = checkWin(next);
                if (win == 1) {
                    maxes = findMax(next);
                    next = maxes;
                } else {
                    next.utility = win;
                }
                //find the lowest utility value
                if (min == null) {
                    min = next;
                } else {
                    if (min.utility > next.utility) {
                        min = next;
                    }
                }
            }
        }
        return min;
    }

    /**
     * Creates the next board state.
     *
     * @param parent
     *            the current board state
     * @param index
     *            the next possible open move
     * @return the Child of the parent state at the position specified.
     */
    public static Node createChild(Node parent, int index) {
        Node child = new Node();
        System.arraycopy(parent.state, 0, child.state, 0, parent.state.length);
        if (parent.move == Turn.x) {
            child.state[index] = "X";
            child.move = Turn.o;
        } else {
            child.state[index] = "O";
            child.move = Turn.x;
        }
        child.movesLeft = parent.movesLeft - 1;
        child.parentNode = parent;
        return child;
    }

    /**
     * Check if the state is a Terminal State
     *
     * @param state
     *            the current board state
     * @return 10,-10 or 0 if Terminal. 1 otherwise.
     */
    public static int checkWin(Node state) {
        int score = 1;
        String winner = "-";
        String value;

        for (int k = 0; k < state.state.length; k++) {
            value = state.state[k];
            if (k % 3 == 0) {
                if (value == state.state[k + 1]) {
                    if (value == state.state[k + 2]) {
                        winner = value;
                    }
                }
            }
            if (k < 3 && winner == "-") {
                if (value == state.state[k + 3]) {
                    if (value == state.state[k + 6]) {
                        winner = value;
                    }
                }
            }
            if (k == 0 && winner == "-") {
                if (value == state.state[4]) {
                    if (value == state.state[8]) {
                        winner = value;
                    }
                }
            }
            if (k == 2 && winner == "-") {
                if (value == state.state[4]) {
                    if (value == state.state[6]) {
                        winner = value;
                    }
                }
            }
        }
        if (winner == "X") {
            score = 10;
        } else if (winner == "O") {
            score = -10;
        } else if (state.movesLeft == 0) {
            score = 0;
        }
        return score;
    }

    /**
     * Prints out the minimax of the first move.
     *
     * @param minimax
     *            the minimax values of each move
     */
    public static void printMiniMax(int[] minimax) {
        System.out.println("-------minimax values for X's first Move-------");
        for (int k = 0; k < minimax.length; k++) {
            if (k % 3 == 0) {
                System.out.println();
            }
            System.out.print(minimax[k] + " ");
        }
        System.out.println();
        System.out.println();
        System.out.println("----------------Optimal Play-------------------");
    }

    /**
     * Creates a stack of the solution to print later.
     *
     * @param finalized
     *            the last state
     * @param finished
     *            the stack of all the moves.
     */
    public static void createStack(Node finalized, Stack<Node> finished) {
        Node next = finalized;
        while (next.parentNode != null) {
            finalized = next.parentNode;
            finished.push(next);
            next = finalized;
        }
    }

    /**
     * Prints the stack of all the moves made.
     *
     * @param finished
     *            the stack of all the moves made.
     */
    public static void printStack(Stack<Node> finished) {
        while (!finished.empty()) {
            Node next = finished.pop();
            movePrinter(next);
            System.out.println();
        }
    }

    /**
     * Prints out the state of the board from entered.
     *
     * @param move
     *            the state of the board to print.
     */
    public static void movePrinter(Node move) {
        for (int k = 0; k < move.state.length; k++) {
            if (k % 3 == 0) {
                System.out.println();
            }
            System.out.print(move.state[k] + "  ");
        }
    }
}
