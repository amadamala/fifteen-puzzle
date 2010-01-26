<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> 3b75b5dd52f00e787cbd63e848d7552f49b29ca1
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


/**
 * 
 * @author Anil Madamala 
 * Copyright (c) 2007,2008 Anil Madamala
 * Any problems contact: amadamala+code@gmail.com
 * 
 */
public class FifteenPuzzle implements ActionListener {

    /* Dimention of the Board */
    private static final int DIM = 4;  // For N * N board DIM = N
    /* Total number of cells in the board */
    private static final int SIZE = DIM * DIM;
    /* Win state */
    final String[] WIN = new String[SIZE-1];
    /* Initial Height of the board*/
    private static final int HEIGHT = 400;
    /* Initial Width of the board*/
    private static final int WIDTH = 400;
    /* Initial empty cell in the board*/
    private int emptyCell = DIM * DIM;
    /* 15 puzzle Board, of size (4 X 4)*/
    private JButton[][] board = new JButton[DIM][DIM];
    private JFrame frame;
    private JPanel panel = new JPanel();

    // Suppresses default constructor, ensuring non-instantiability.
    public FifteenPuzzle() {

        // Initialize the win state
        for (int i = 1; i < SIZE; i++) {
            WIN[i-1] = Integer.toString(i);
        }
        
        System.out.println("Win State:" + Arrays.asList(WIN) );
    }

    public static void main(String[] args) {
        FifteenPuzzle game = new FifteenPuzzle();
        game.initializeBoard(); /* Initializes the 15-puzzle game board */

    }

    /**
     * Gives index value corresponding to [row,col] of a square
     * @param i, row
     * @param j, column
     * @return the index of the corresponding to the row and column
     */
    private int getIndex(int i, int j) {       
        return ((i * DIM) + j);  // i * 4 + j        

    }

    /**
     * Generates the random intitial state for the game.
     * Assigns unique random number to each square
     */
    private void initializeBoard() {
        ArrayList<Integer> intialList = new ArrayList<Integer>(SIZE);

        // Repeat until creation of solvable initial board
        for (boolean isSolvable = false; isSolvable == false;) {

            // create ordered list
            intialList = new ArrayList<Integer>(SIZE);
            for (int i = 0; i < SIZE; i++) {
                intialList.add(i, i);
            }

            // Shuffle the list
            Collections.shuffle(intialList);

            // Check list can be solvable or not
            isSolvable = isSolvable(intialList);
        }
        System.out.println("Initial Board state:" + intialList);

        // Assigns unique random number to each square        
        for (int index = 0; index < SIZE; index++) {
            final int ROW = index / DIM;  // row number from index
            final int COL = index % DIM;   // column number from index 
            board[ROW][COL] = new JButton(String.valueOf(intialList.get(index)));
            // intializes the empty square and hide it
            if (intialList.get(index) == 0) {
                emptyCell = index;
                board[ROW][COL].setVisible(false);
            }

            // Decorating each square
            board[ROW][COL].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            board[ROW][COL].setBackground(Color.BLACK);
            board[ROW][COL].setForeground(Color.GREEN);
            board[ROW][COL].addActionListener(this);
            panel.add(board[ROW][COL]);
        }

        // Initializes the Frame
        frame = new JFrame("Shuffle Game");
        frame.setLocation(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(HEIGHT, WIDTH);

        // Initializes the panel
        panel.setLayout(new GridLayout(DIM, DIM));
        panel.setBackground(Color.GRAY);

        // Initializes the content pane
        java.awt.Container content = frame.getContentPane();
        content.add(panel, BorderLayout.CENTER);
        content.setBackground(Color.GRAY);
        frame.setVisible(true);
    }

    /**
     * Verifies the board for solvability.
     * For more detals of solvability goto URL:
     * http://mathworld.wolfram.com/15Puzzle.html 
     * @param list, 16 elements from 0-15, no repetition of elements
     * @return true, if the initial board can be solvable
     *         false, if the initial board can't be solvable
     */
    private boolean isSolvable(ArrayList<Integer> list) {
    	
    	if(list.size() != 16)
    	{
    		System.err.println("isSolvable function works only" +
    			    "with a list having 0-16 as values");
    	}
    	
        int inversionSum = 0;  // If this sum is even it is solvable
        for (int i = 0; i < list.size(); i++) {
            // For empty square add row number to inversionSum                
            if (list.get(i) == 0) {
                inversionSum += ((i / DIM) + 1);  //add Row number
                continue;
            }

            int count = 0;
            for (int j = i + 1; j < list.size(); j++) {
                // No need need to count for empty square
                if (list.get(j) == 0) {
                    continue;
                } else if (list.get(i) > list.get(j)) { // If any element greater 
                    count++;                            // than seed increse the 
                }                                       // inversionSum                    
            }
            inversionSum += count;
        }

        // if inversionSum is even return true, otherwise false
        return ((inversionSum & 1) == 0) ? true : false;
    }

    /**
     * If any button in the board is pressed, it will perform the
     * required actions associated with the button. Actions like
     * checking isAdjacent(), swapping using swapWithEmpty() and also
     * checks to see whether the game is finished or not.
     * 
     * @param event, event performed by the player
     * @throws IllegalArgumentException, if the <tt>index = -1 </tt>
     */
    public void actionPerformed(ActionEvent event) throws IllegalArgumentException {
        JButton buttonPressed = (JButton) event.getSource();
        int index = indexOf(buttonPressed.getText());
        if (index == -1) {
            throw (new IllegalArgumentException("Index should be between 0-15"));
        }
        int row = index / DIM;
        int column = index % DIM;

        // If pressed button in same row or same column
        makeMove(row, column);

        // If the game is finished, "You Win the Game" dialog will appear
        if (isFinished()) {
            JOptionPane.showMessageDialog(null, "You Win The Game.");
        }
    }

    /**
     * Gives the index by processing the text on sqare
     * @param cellNum, number on the button
     * @return the index of the button
     */
    private int indexOf(String cellNum) {

        for (int ROW = 0; ROW < board.length; ROW++) {
            for (int COL = 0; COL < board[ROW].length; COL++) {
                if (board[ROW][COL].getText().equals(cellNum)) {
                    return (getIndex(ROW, COL));
                }
            }
        }
        return -1;   // Wrong input returns -1

    }

    /**
     * Checks the row or column with empty square
     * @return true, if we pressed the button in same row or column 
     *              as empty square
     *         false, otherwise
     */
    private boolean makeMove(int row, int col) {
        final int emptyRow = emptyCell / DIM;  // Empty cell row number
        final int emptyCol = emptyCell % DIM;   // Empty cell column number
        int rowDiff = emptyRow - row;
        int colDiff = emptyCol - col;
        boolean isInRow = (row == emptyRow);
        boolean isInCol = (col == emptyCol);
        boolean isNotDiagonal = (isInRow || isInCol);

        if (isNotDiagonal) {
            int diff = Math.abs(colDiff);
    
            // -ve diff, move row left
            if (colDiff < 0 & isInRow) {
                for (int i = 0; i < diff; i++) {
                    board[emptyRow][emptyCol + i].setText(
                            board[emptyRow][emptyCol + (i + 1)].getText());
                }

            } // + ve Diff, move row right
            else if (colDiff > 0 & isInRow) {
                for (int i = 0; i < diff; i++) {
                    board[emptyRow][emptyCol - i].setText(
                            board[emptyRow][emptyCol - (i + 1)].getText());
                }
            }

            diff = Math.abs(rowDiff);

            // -ve diff, move column up
            if (rowDiff < 0 & isInCol) {
                for (int i = 0; i < diff; i++) {
                    board[emptyRow + i][emptyCol].setText(
                            board[emptyRow + (i + 1)][emptyCol].getText());
                }

            } // + ve Diff, move column down
            else if (rowDiff > 0 & isInCol) {
                for (int i = 0; i < diff; i++) {
                    board[emptyRow - i][emptyCol].setText(
                            board[emptyRow - (i + 1)][emptyCol].getText());
                }
            }

            // Swap the empty square with the given square
            board[emptyRow][emptyCol].setVisible(true);
            board[row][col].setText(Integer.toString(0));
            board[row][col].setVisible(false);
            emptyCell = getIndex(row, col);
        }

        return true;
    }

    /**
     * Checks whehere game is finished or not
     * @return true, if the board is in final state
     *         false, if the board is not in final state 
     */
    private boolean isFinished() {
        // Check 1-15 elements whether they are in right position or not
        for (int index = WIN.length - 1; index >= 0; index--) {
            String number = board[index / DIM][index % DIM].getText();           
            if (!number.equals(WIN[index])) {
                return false;       // If any of the index is not aligned 

            }
        }
        return true;
    }
<<<<<<< HEAD
=======
=======
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * It is a sliding puzzle that consists of a grid of numbered squares 
 * with one square missing, and the labels on the squares jumbled up
 *  
 * The n-puzzle is known in various versions,including the 8 puzzle, the 15 puzzle, 
 * and with various names. If the grid is 3*3, the puzzle is called the 8-puzzle or 
 * 9-puzzle. If the grid is 4*4, the puzzle is called the 15-puzzle or 16-puzzle. 
 * The goal of the puzzle is to un-jumble the squares by only making moves which slide 
 * squares into the empty space, in turn revealing another empty space 
 * in the position of the moved piece.
 */

/**
 * 
 * @author Anil Madamala 
 * Copyright (c) 2007,2008 Anil Madamala
 * Any problems contact: amadamala+code@gmail.com
 * 
 */
public class FifteenPuzzle implements ActionListener {

    /* Dimention of the Board */
    private static final int DIM = 4;  // For N * N board DIM = N
    /* Total number of cells in the board */
    final int SIZE = DIM * DIM;
    /* Win state */
    final String[] WIN = new String[SIZE-1];
    /* Initial Height of the board*/
    private static final int HEIGHT = 400;
    /* Initial Width of the board*/
    private static final int WIDTH = 400;
    /* Initial empty cell in the board*/
    private int emptyCell = DIM * DIM;
    /* 15 puzzle Board, of size (4 X 4)*/
    private JButton[][] board = new JButton[DIM][DIM];
    private JFrame frame;
    private JPanel panel = new JPanel();

    // Suppresses default constructor, ensuring non-instantiability.
    public FifteenPuzzle() {
        if(DIM <= 1){
            JOptionPane.showMessageDialog(null, "You Win The Game.");
            System.err.println("Dimention Should be greater than 1 to play");
            System.exit(0);
        }
        // Initialize the win state
        for (int i = 1; i < SIZE; i++) {
            WIN[i-1] = Integer.toString(i);
        }
        
        System.out.println("Win State:" + Arrays.asList(WIN) );
    }

    public static void main(String[] args) {
        FifteenPuzzle game = new FifteenPuzzle();
        game.initializeBoard(); /* Initializes the 15-puzzle game board */

    }

    /**
     * Gives index value corresponding to [row,col] of a square
     * @param i, row
     * @param j, column
     * @return the index of the corresponding to the row and column
     */
    private int getIndex(int i, int j) {       
        return ((i * DIM) + j);  // i * 4 + j        

    }

    /**
     * Generates the random intitial state for the game.
     * Assigns unique random number to each square
     */
    private void initializeBoard() {
        ArrayList<Integer> intialList = new ArrayList<Integer>(SIZE);

        // Repeat until creation of solvable initial board
        for (boolean isSolvable = false; isSolvable == false;) {

            // create ordered list
            intialList = new ArrayList<Integer>(SIZE);
            for (int i = 0; i < SIZE; i++) {
                intialList.add(i, i);
            }

            // Shuffle the list
            Collections.shuffle(intialList);

            // Check list can be solvable or not
            isSolvable = isSolvable(intialList);
        }
        System.out.println("Initial Board state:" + intialList);

        // Assigns unique random number to each square        
        for (int index = 0; index < SIZE; index++) {
            final int ROW = index / DIM;  // row number from index
            final int COL = index % DIM;   // column number from index 
            board[ROW][COL] = new JButton(String.valueOf(intialList.get(index)));
            // intializes the empty square and hide it
            if (intialList.get(index) == 0) {
                emptyCell = index;
                board[ROW][COL].setVisible(false);
            }

            // Decorating each square
            board[ROW][COL].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            board[ROW][COL].setBackground(Color.BLACK);
            board[ROW][COL].setForeground(Color.GREEN);
            board[ROW][COL].addActionListener(this);
            panel.add(board[ROW][COL]);
        }

        // Initializes the Frame
        frame = new JFrame("Shuffle Game");
        frame.setLocation(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(HEIGHT, WIDTH);

        // Initializes the panel
        panel.setLayout(new GridLayout(DIM, DIM));
        panel.setBackground(Color.GRAY);

        // Initializes the content pane
        java.awt.Container content = frame.getContentPane();
        content.add(panel, BorderLayout.CENTER);
        content.setBackground(Color.GRAY);
        frame.setVisible(true);
    }

    /**
     * Verifies the board for solvability.
     * For more detals of solvability goto URL:
     * http://mathworld.wolfram.com/15Puzzle.html 
     * @param initialList
     * @return true, if the initial board can be solvable
     *         false, if the initial board can't be solvable
     */
    private boolean isSolvable(ArrayList<Integer> list) {
        int inversionSum = 0;  // If this sum is even it is solvable
        for (int i = 0; i < list.size(); i++) {
            // For empty square add row number to inversionSum                
            if (list.get(i) == 0) {
                inversionSum += ((i / DIM) + 1);  //add Row number
                continue;
            }

            int count = 0;
            for (int j = i + 1; j < list.size(); j++) {
                // No need need to count for empty square
                if (list.get(j) == 0) {
                    continue;
                } else if (list.get(i) > list.get(j)) { // If any element greater 
                    count++;                            // than seed increse the 
                }                                       // inversionSum                    
            }
            inversionSum += count;
        }

        // if inversionSum is even return true, otherwise false
        return ((inversionSum & 1) == 0) ? true : false;
    }

    /**
     * If any button in the board is pressed, it will perform the
     * required actions associated with the button. Actions like
     * checking isAdjacent(), swapping using swapWithEmpty() and also
     * checks to see whether the game is finished or not.
     * 
     * @param event, event performed by the player
     * @throws IllegalArgumentException, if the <tt>index = -1 </tt>
     */
    public void actionPerformed(ActionEvent event) throws IllegalArgumentException {
        JButton buttonPressed = (JButton) event.getSource();
        int index = indexOf(buttonPressed.getText());
        if (index == -1) {
            throw (new IllegalArgumentException("Index should be between 0-15"));
        }
        int row = index / DIM;
        int column = index % DIM;

        // If pressed button in same row or same column
        makeMove(row, column);

        // If the game is finished, "You Win the Game" dialog will appear
        if (isFinished()) {
            JOptionPane.showMessageDialog(null, "You Win The Game.");
        }
    }

    /**
     * Gives the index by processing the text on sqare
     * @param cellNum, number on the button
     * @return the index of the button
     */
    private int indexOf(String cellNum) {

        for (int ROW = 0; ROW < board.length; ROW++) {
            for (int COL = 0; COL < board[ROW].length; COL++) {
                if (board[ROW][COL].getText().equals(cellNum)) {
                    return (getIndex(ROW, COL));
                }
            }
        }
        return -1;   // Wrong input returns -1

    }

    /**
     * Checks the row or column with empty square
     * @return true, if we pressed the button in same row or column 
     *              as empty square
     *         false, otherwise
     */
    private boolean makeMove(int row, int col) {
        final int emptyRow = emptyCell / DIM;  // Empty cell row number
        final int emptyCol = emptyCell % DIM;   // Empty cell column number
        int rowDiff = emptyRow - row;
        int colDiff = emptyCol - col;
        boolean isInRow = (row == emptyRow);
        boolean isInCol = (col == emptyCol);
        boolean isNotDiagonal = (isInRow || isInCol);

        if (isNotDiagonal) {
            int diff = Math.abs(colDiff);
    
            // -ve diff, move row left
            if (colDiff < 0 & isInRow) {
                for (int i = 0; i < diff; i++) {
                    board[emptyRow][emptyCol + i].setText(
                            board[emptyRow][emptyCol + (i + 1)].getText());
                }

            } // + ve Diff, move row right
            else if (colDiff > 0 & isInRow) {
                for (int i = 0; i < diff; i++) {
                    board[emptyRow][emptyCol - i].setText(
                            board[emptyRow][emptyCol - (i + 1)].getText());
                }
            }

            diff = Math.abs(rowDiff);

            // -ve diff, move column up
            if (rowDiff < 0 & isInCol) {
                for (int i = 0; i < diff; i++) {
                    board[emptyRow + i][emptyCol].setText(
                            board[emptyRow + (i + 1)][emptyCol].getText());
                }

            } // + ve Diff, move column down
            else if (rowDiff > 0 & isInCol) {
                for (int i = 0; i < diff; i++) {
                    board[emptyRow - i][emptyCol].setText(
                            board[emptyRow - (i + 1)][emptyCol].getText());
                }
            }

            // Swap the empty square with the given square
            board[emptyRow][emptyCol].setVisible(true);
            board[row][col].setText(Integer.toString(0));
            board[row][col].setVisible(false);
            emptyCell = getIndex(row, col);
        }

        return true;
    }

    /**
     * Checks whehere game is finished or not
     * @return true, if the board is in final state
     *         false, if the board is not in final state 
     */
    private boolean isFinished() {
        // Check 1-15 elements whether they are in right position or not
        for (int index = WIN.length - 1; index >= 0; index--) {
            String number = board[index / DIM][index % DIM].getText();           
            if (!number.equals(WIN[index])) {
                return false;       // If any of the index is not aligned 

            }
        }
        return true;
    }
>>>>>>> 994a18fd3e3f34bcbce7c3046f8f6d262b7c1588
>>>>>>> 3b75b5dd52f00e787cbd63e848d7552f49b29ca1
} 