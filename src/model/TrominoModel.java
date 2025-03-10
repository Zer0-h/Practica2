package model;

public class TrominoModel {

    private int[][] board;
    private int boardSize;
    private int fixedX, fixedY;
    private int currentNum;

    // Constructor: Initializes board, sets fixed tile
    public TrominoModel(int size, int fixedX, int fixedY) {
        // Ensure size is a power of 2
        int actualSize = 1;
        while (actualSize < size) {
            actualSize *= 2;
        }
        this.boardSize = actualSize;
        this.fixedX = fixedX;
        this.fixedY = fixedY;
        this.board = new int[boardSize][boardSize];
        this.currentNum = 1;

        // Fill board with empty values
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = 0;
            }
        }

        // Set the fixed black tile
        board[fixedX][fixedY] = -1;
    }

    // Wrapper method for recursion
    public void solveTromino() {
        tileRec(boardSize, 0, 0);
    }

    // Recursive method to fill board with trominoes
    private void tileRec(int size, int topX, int topY) {
        if (size == 2) {
            // Base case: fill a single tromino
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (board[topX + i][topY + j] == 0) {
                        board[topX + i][topY + j] = currentNum;
                    }
                }
            }
            currentNum++;
            return;
        }

        // Find the quadrant with the missing tile
        int saveX = topX, saveY = topY;
        for (int x = topX; x < topX + size; x++) {
            for (int y = topY; y < topY + size; y++) {
                if (board[x][y] != 0) {
                    saveX = x;
                    saveY = y;
                }
            }
        }

        // Identify which quadrant contains the missing tile
        boolean inUpperLeft = saveX < topX + size / 2 && saveY < topY + size / 2;
        boolean inUpperRight = saveX < topX + size / 2 && saveY >= topY + size / 2;
        boolean inBottomLeft = saveX >= topX + size / 2 && saveY < topY + size / 2;
        boolean inBottomRight = saveX >= topX + size / 2 && saveY >= topY + size / 2;

        // Place a tromino in the center
        int centerX = topX + size / 2 - 1;
        int centerY = topY + size / 2 - 1;
        board[centerX][centerY + 1] = currentNum;
        board[centerX + 1][centerY] = currentNum;
        board[centerX + 1][centerY + 1] = currentNum;

        // Mark missing tile in each quadrant
        if (!inUpperLeft) {
            board[centerX][centerY] = currentNum;
        }
        if (!inUpperRight) {
            board[centerX][centerY + 1] = currentNum;
        }
        if (!inBottomLeft) {
            board[centerX + 1][centerY] = currentNum;
        }
        if (!inBottomRight) {
            board[centerX + 1][centerY + 1] = currentNum;
        }

        currentNum++;

        // Recursive calls for each quadrant
        tileRec(size / 2, topX, topY); // Upper Left
        tileRec(size / 2, topX, topY + size / 2); // Upper Right
        tileRec(size / 2, topX + size / 2, topY); // Bottom Left
        tileRec(size / 2, topX + size / 2, topY + size / 2); // Bottom Right
    }

    public int[][] getBoard() {
        return board;
    }

    public void clearBoard() {
        board = new int[boardSize][boardSize];
        board[fixedX][fixedY] = -1; // Keep fixed tile
        currentNum = 1;
    }
}
