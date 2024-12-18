import java.util.Stack;

/**
 * Node class for each state of the tic tac toe board.
 *
 * @author Albert Maah
 *
 */
class Node {
    public enum Turn {
        x, o
    }

    //Parent Node
    Node parentNode = null;
    //The state of this branch
    String[] state = new String[9];
    //Utility value of this state
    int utility;
    //Who's turn is it
    Turn move;
    //Moves Left till full board
    int movesLeft;
}

/**
 * H MiniMax on a TicTacToe board
 *
 * @author Albert Maah
 */
public final class AIHMiniMaxTicTacToe extends Node {

    /**
     * Default constructor--private to prevent instantiation.
     */
    private AIHMiniMaxTicTacToe() {
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
        Node finalized = miniMaxCut(initialState);
        createStack(finalized, finished);
        printStack(finished);
    }

    /**
     * Cycles through each side's turns.
     *
     * @param state
     *            initial board state
     * @return the final game board
     */
    public static Node miniMaxCut(Node state) {
        Node finalized = state;
        Node past = finalized;
        //get move for each player with depth 4 branching
        for (int k = 0; k < 9; k++) {
            if (k % 2 == 0) {
                //X's Turn
                finalized = findMax(finalized, 0);
            } else {
                //O's Turn
                finalized = findMin(finalized, 0);
            }
            while (finalized.movesLeft != past.movesLeft - 1) {
                Node temp = finalized.parentNode;
                finalized = temp;
            }
            past = finalized;
        }
        return finalized;
    }

    /**
     * Finds the Max/ best move for X.
     *
     * @param state
     *            The current board state
     * @param depth
     *            The depth it has searched so far.
     * @return the Max Utility value State.
     */
    public static Node findMax(Node state, int depth) {
        Node max = null;
        int[] minimax = new int[9];

        for (int k = 0; k < state.state.length; k++) {
            if (state.state[k] == ".") {
                Node next = createChild(state, k);
                Node mins;
                int win = checkWin(next);
                //Check terminal state
                if (win == 1) {
                    //Check cutoff
                    if (depth <= 4) {
                        mins = findMin(next, depth + 1);
                        next = mins;
                    } else {
                        int util = utilityHeuristic(next);
                        next.utility = util;
                    }
                } else {
                    next.utility = win;
                }
                if (state.movesLeft == 9) {
                    minimax[k] = next.utility;

                }
                //find the Max utility
                if (max == null) {
                    max = next;
                } else {
                    if (max.utility < next.utility) {
                        max = next;
                    }
                }

            }
        }
        //print the utility values for the first move
        if (state.movesLeft == 9) {
            printMiniMax(minimax);
        }
        return max;
    }

    /**
     * Find the Min/Best move for O.
     *
     * @param state
     *            Current board state.
     * @param depth
     *            Current searched depth.
     * @return the state with the best Utility for O.
     */
    public static Node findMin(Node state, int depth) {
        Node min = null;
        for (int k = 0; k < state.state.length; k++) {
            if (state.state[k] == ".") {
                Node next = createChild(state, k);
                Node maxes;
                int win = checkWin(next);
                if (win == 1) {
                    if (depth <= 4) {
                        maxes = findMax(next, depth + 1);
                        next = maxes;
                    } else {
                        int util = utilityHeuristic(next);
                        next.utility = util;
                    }
                } else {
                    next.utility = win;
                }
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
            if (k < 3) {
                if (value == state.state[k + 3]) {
                    if (value == state.state[k + 6]) {
                        winner = value;
                    }
                }
            }
            if (k == 0) {
                if (value == state.state[4]) {
                    if (value == state.state[8]) {
                        winner = value;
                    }
                }
            }
            if (k == 2) {
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
     * Calculates the Utility value for a non terminal location
     *
     * @param state
     *            current board state
     * @return the utility value of the current board state.
     */
    public static int utilityHeuristic(Node state) {
        int score = 0;
        int x2 = 0;
        int x = 0;
        int o2 = 0;
        int o = 0;
        for (int k = 0; k < state.state.length; k++) {
            boolean takenoff = false;
            if (state.state[k] != ".") {
                //Check X for Non terminal
                if (state.state[k] == "X") {
                    x++;
                    if (k % 3 == 0 || k % 3 == 1) {
                        if (state.state[k + 1] == "X") {
                            x2++;
                            takenoff = true;
                            x--;

                        }
                    }
                    if (k < 6) {
                        if (state.state[k + 3] == "X") {
                            x2++;
                            if (!takenoff) {
                                takenoff = true;
                                x--;
                            }
                        }
                    }
                    if (k == 0 || k == 4) {
                        if (state.state[k + 4] == "X") {
                            x2++;
                            if (!takenoff) {
                                takenoff = true;
                                x--;
                            }
                        }
                    }
                    if (k == 2 || k == 4) {
                        if (state.state[k + 2] == "X") {
                            x2++;
                            if (!takenoff) {
                                takenoff = true;
                                x--;
                            }
                        }
                    }
                }
                //Check O for Non terminal
                if (state.state[k] == "O") {
                    o++;
                    if (k % 3 == 0 || k % 3 == 1) {
                        if (state.state[k + 1] == "O") {
                            o2++;
                            takenoff = true;
                            o--;

                        }
                    }
                    if (k < 6) {
                        if (state.state[k + 3] == "O") {
                            o2++;
                            if (!takenoff) {
                                takenoff = true;
                                o--;
                            }
                        }
                    }
                    if (k == 0 || k == 4) {
                        if (state.state[k + 4] == "O") {
                            o2++;
                            if (!takenoff) {
                                takenoff = true;
                                o--;
                            }
                        }
                    }
                    if (k == 2 || k == 4) {
                        if (state.state[k + 2] == "O") {
                            o2++;
                            if (!takenoff) {
                                takenoff = true;
                                o--;
                            }
                        }
                    }
                }
            }
        }
        score = (3 * x2 + x) - (3 * o2 + o);
        return score;
    }

    /**
     * Prints out the minimax of the first move.
     *
     * @param minimax
     *            the minimax values of each move
     */
    public static void printMiniMax(int[] minimax) {
        System.out.println("-------Hminimax values for X's first Move-------");
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
