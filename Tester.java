import java.awt.*;
import javax.swing.*;

class A1 {

    public static boolean solveSudoku(int[][] board, JButton[][] button) {
        int[] emptyCell = findEmptyCell(board);
        if (emptyCell == null) {
            return true; // Sudoku solved
        } else {
            int row = emptyCell[0];
            int col = emptyCell[1];

            for (int num = 1; num <= 9; num++) {
                if (isValid(board, row, col, num)) {
                    board[row][col] = num;
                    updateButton(button, row, col, num); // Update the button text in real time
                    sleep(2); // Add delay to visualize progress

                    if (solveSudoku(board, button)) {
                        return true;
                    }

                    board[row][col] = 0; // Backtrack
                    updateButton(button, row, col, 0); // Reset button during backtracking
                }
            }
            return false; // No solution found
        }
    }

    // Helper function to pause execution to show real-time updates
    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Updates the button in the grid
    private static void updateButton(JButton[][] button, int row, int col, int num) {
        button[row][col].setText(num == 0 ? "" : String.valueOf(num));
    }

    public static boolean isValid(int[][] board, int row, int col, int num) {
        for (int x = 0; x < 9; x++) {
            if (board[row][x] == num || board[x][col] == num) {
                return false;
            }
        }

        int startRow = 3 * (row / 3);
        int startCol = 3 * (col / 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i + startRow][j + startCol] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int[] findEmptyCell(int[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Sudoku Solver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        int[][] sudokuBoard = {
                {5, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };

        JButton[][] button = new JButton[9][9];
        frame.setLayout(new GridLayout(9, 9));

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                button[i][j] = new JButton(sudokuBoard[i][j] == 0 ? "" : String.valueOf(sudokuBoard[i][j]));
                button[i][j].setFont(new Font("Arial", Font.BOLD, 18));
                frame.add(button[i][j]);
            }
        }

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        // Solve Sudoku in a new thread to avoid freezing the UI
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                solveSudoku(sudokuBoard, button); // Solve Sudoku in background
                return null;
            }
        };

        worker.execute(); // Start the solving process
    }
}
