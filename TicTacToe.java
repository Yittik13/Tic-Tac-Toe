/**
 * Tic Tac Toe game + a computer AI opponent that does not make mistakes
 *
 * Code for the minimax algorithm and evaluation methods was found at:
 * http://www3.ntu.edu.sg/home/ehchua/programming/java/javagame_tictactoe_ai.html
 *
 * The code from this site has been modified slightly to fit the way the game is written
 */
import java.util.*;
public class TicTacToe {
    public static final char NONE = ' ';
    public static final char x = 'X';
    public static final char o = 'O';
    char[][] board;
    int moves;
    char currentPlayer;
    Random rand = new Random();

    public TicTacToe() {
        board = new char[3][3];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = NONE;
            }
        }
    }

    //Returns a copy of the current board
    public char[][] getBoard() {
        return board;
    }

    public boolean validMove(int row, int column) {
        if (board[row][column] == NONE) {
            return true;
        } else {
            return false;
        }
    }

    public void makeMove(int row, int column, char color) {
        this.board[row][column] = color;
    }

    public void printBoard() {
        System.out.println("Moves: " + moves + "\n");
        System.out.println("   0" + " 1 " + "2");
        System.out.print("0  ");
        System.out.println(board[0][0] + "|" + board[0][1] + "|" + board[0][2]);
        System.out.println("   -----");
        System.out.print("1  ");
        System.out.println(board[1][0] + "|" + board[1][1] + "|" + board[1][2]);
        System.out.println("   -----");
        System.out.print("2  ");
        System.out.println(board[2][0] + "|" + board[2][1] + "|" + board[2][2] + "\n");
    }

    public boolean checkGameCondition() {
        boolean gameOver = false;
        //check rows
        for (int i = 0; i < board.length; i++) {
            if (board[i][0] == x && board[i][1] == x && board[i][2] == x) {
                System.out.println(x + " wins!");
                gameOver = true;
            } else if (board[i][0] == o && board[i][1] == o && board[i][2] == o) {
                System.out.println(o + " wins!");
                gameOver = true;
            }
        }

        //check columns
        for (int i = 0; i < board[0].length; i++) {
            if (board[0][i] == x && board[1][i] == x && board[2][i] == x) {
                System.out.println(x + " wins!");
                gameOver = true;
            } else if (board[0][i] == o && board[1][i] == o && board[2][i] == o) {
                System.out.println(o + " wins!");
                gameOver = true;
            }
        }

        //check diagonals
        if (board[0][0] == x && board[1][1] == x && board[2][2] == x) {
            System.out.println(x + " wins!");
            gameOver = true;
        }
        if (board[0][2] == x && board[1][1] == x && board[2][0] == x) {
            System.out.println(x + " wins!");
            gameOver = true;
        }
        if (board[0][0] == o && board[1][1] == o && board[2][2] == o) {
            System.out.println(o + " wins!");
            gameOver = true;
        }
        if (board[0][2] == o && board[1][1] == o && board[2][0] == o) {
            System.out.println(o + " wins!");
            gameOver = true;
        }

        if (moves == 9) {
            gameOver = true;
            System.out.println("Tie game!");
        }
        return gameOver;
    }

    public void play() {
        boolean turn;

        //turn = true; /X's turn
        //turn = false; //O's turn
        turn = Math.random() > .5;


        Scanner in = new Scanner(System.in);
        int column;
        int row;

        do {
            if (turn) {
                currentPlayer = x;
            } else {
                currentPlayer = o;
            }

            if (currentPlayer == o) {
                this.printBoard();
                System.out.println("Current player: " + currentPlayer);

                do {
                    System.out.println("Enter a row (0 - 2):");
                    row = in.nextInt();
                    System.out.println("Enter a column (0 - 2):");
                    column = in.nextInt();
                    if (!this.validMove(row, column)) {
                        System.out.println("Invalid move." + "\n");
                    } else {
                        System.out.println();
                    }
                } while (!this.validMove(row, column));
                this.makeMove(row, column, currentPlayer);
            } else {
                ai();
            }
            turn = !turn;
            moves++;


        } while (!this.checkGameCondition());
        this.printBoard();
        in.close();
    }

    public List<int[]> getPossibleMoves() {
        List<int[]> possibleMoves = new ArrayList<int[]>();

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                if (board[row][col] == NONE) {
                    possibleMoves.add(new int[]{row, col});
                }
            }
        }
        return possibleMoves;
    }

    /**
     * Minimax algorithm which is what decides the move the computer will play
     *
     * @param depth  The number of turns which the computer looks ahead (I think)
     * @param player The current player
     * @return An array containing the best row and column based on the best score
     */
    public int[] minimax(int depth, char player) {
        List<int[]> possibleMoves = getPossibleMoves();


        //X is maximizing; O is minimizing
        int bestScore;
        if (player == o) {
            bestScore = Integer.MAX_VALUE;
        } else {
            bestScore = Integer.MIN_VALUE;
        }
        int currentScore;
        int bestRow = -1;
        int bestCol = -1;

        if (possibleMoves.isEmpty() || depth == 0) {
            //Game over or depth reached, evaluate score
            bestScore = evaluate();
        } else {
            for (int[] move : possibleMoves) {
                //try this move for the current "player"
                board[move[0]][move[1]] = player;
                if (player == x) { //x (computer) is maximizing
                    currentScore = minimax(depth - 1, o)[0];
                    if (currentScore > bestScore) {
                        bestScore = currentScore;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                } else { //O is minimizing player
                    currentScore = minimax(depth - 1, x)[0];
                    if (currentScore < bestScore) {
                        bestScore = currentScore;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                }
                System.out.println(player + ": " + "(" + move[0] + ")" + "(" + move[1] + ")" + " Score: " + bestScore);
                board[move[0]][move[1]] = NONE;
            }
        }

        //System.out.println(player + ": " + "(" + bestRow + ")" + "(" + bestCol + ")" + " Score: " + bestScore);
        return new int[]{bestScore, bestRow, bestCol};
    }

    /**
     * The heuristic evaluation function for the current board
     *
     * @Return +100, +10, +1 for EACH 3-, 2-, 1-in-a-line for computer.
     * -100, -10, -1 for EACH 3-, 2-, 1-in-a-line for opponent.
     * 0 otherwise
     */
    private int evaluate() {
        int score = 0;
        // Evaluate score for each of the 8 lines (3 rows, 3 columns, 2 diagonals)
        score += evaluateLine(0, 0, 0, 1, 0, 2);  // row 0
        score += evaluateLine(1, 0, 1, 1, 1, 2);  // row 1
        score += evaluateLine(2, 0, 2, 1, 2, 2);  // row 2
        score += evaluateLine(0, 0, 1, 0, 2, 0);  // col 0
        score += evaluateLine(0, 1, 1, 1, 2, 1);  // col 1
        score += evaluateLine(0, 2, 1, 2, 2, 2);  // col 2
        score += evaluateLine(0, 0, 1, 1, 2, 2);  // diagonal
        score += evaluateLine(0, 2, 1, 1, 2, 0);  // alternate diagonal
        return score;
    }

    /**
     * The heuristic evaluation function for the given line of 3 cells
     *
     * @Return +100, +10, +1 for 3-, 2-, 1-in-a-line for computer.
     * -100, -10, -1 for 3-, 2-, 1-in-a-line for opponent.
     * 0 otherwise
     */
    private int evaluateLine(int row1, int col1, int row2, int col2, int row3, int col3) {
        int score = 0;

        // First cell
        if (board[row1][col1] == x) {
            score = 1;
        } else if (board[row1][col1] == o) {
            score = -1;
        }

        // Second cell
        if (board[row2][col2] == x) {
            if (score == 1) {   // cell1 is mySeed
                score = 10;
            } else if (score == -1) {  // cell1 is oppSeed
                return 0;
            } else {  // cell1 is empty
                score = 1;
            }
        } else if (board[row2][col2] == o) {
            if (score == -1) { // cell1 is oppSeed
                score = -10;
            } else if (score == 1) { // cell1 is mySeed
                return 0;
            } else {  // cell1 is empty
                score = -1;
            }
        }

        // Third cell
        if (board[row3][col3] == x) {
            if (score > 0) {  // cell1 and/or cell2 is mySeed
                score *= 10;
            } else if (score < 0) {  // cell1 and/or cell2 is oppSeed
                return 0;
            } else {  // cell1 and cell2 are empty
                score = 1;
            }
        } else if (board[row3][col3] == o) {
            if (score < 0) {  // cell1 and/or cell2 is oppSeed
                score *= 10;
            } else if (score > 1) {  // cell1 and/or cell2 is mySeed
                return 0;
            } else {  // cell1 and cell2 are empty
                score = -1;
            }
        }
        return score;
    }

    public void ai() {
        int[] result = minimax(2, x);
        System.out.println("(" + result[1] + ")" + "(" + result[2] + ")" + " Score: " + result[0] + "\n");
        makeMove(result[1], result[2], x);
    }
}