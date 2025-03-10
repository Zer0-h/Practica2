package model;

public class TrominoModel {

    private int[][] board;
    private int boardSize;
    private int fixedX, fixedY;
    private int currentNum;

    public TrominoModel(int size, int fixedX, int fixedY) {
        int actualSize = 1;
        while (actualSize < size) {
            actualSize *= 2;
        }
        this.boardSize = actualSize;
        this.fixedX = fixedX;
        this.fixedY = fixedY;
        this.board = new int[boardSize][boardSize];
        this.currentNum = 1;

        // Initialize board with 0s and set fixed tile
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = 0;
            }
        }

        // Fixed tile remains -1
        board[fixedX][fixedY] = -1;
    }

    // Wrapper method for recursion
    public void solveTromino() {
        tileRec(boardSize, 0, 0, fixedX, fixedY);
    }

    private void tileRec(int size, int topX, int topY, int holeX, int holeY) {
        if (size == 2) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (board[topX + i][topY + j] == 0) { // Ensure fixed tile remains untouched
                        board[topX + i][topY + j] = currentNum;
                    }
                }
            }
            currentNum++;
            return;
        }

        int centerX = topX + size / 2 - 1;
        int centerY = topY + size / 2 - 1;

        // Identify which quadrant contains the missing tile
        boolean inUpperLeft = holeX < topX + size / 2 && holeY < topY + size / 2;
        boolean inUpperRight = holeX < topX + size / 2 && holeY >= topY + size / 2;
        boolean inBottomLeft = holeX >= topX + size / 2 && holeY < topY + size / 2;
        boolean inBottomRight = holeX >= topX + size / 2 && holeY >= topY + size / 2;

        // Place a tromino in the center
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

        // Recursively process quadrants
        tileRec(size / 2, topX, topY, inUpperLeft ? holeX : centerX, inUpperLeft ? holeY : centerY); // Upper Left
        tileRec(size / 2, topX, topY + size / 2, inUpperRight ? holeX : centerX, inUpperRight ? holeY : centerY + 1); // Upper Right
        tileRec(size / 2, topX + size / 2, topY, inBottomLeft ? holeX : centerX + 1, inBottomLeft ? holeY : centerY); // Bottom Left
        tileRec(size / 2, topX + size / 2, topY + size / 2, inBottomRight ? holeX : centerX + 1, inBottomRight ? holeY : centerY + 1); // Bottom Right
    }

    public int[][] getBoard() {
        return board;
    }

    public void clearBoard() {
        board = new int[boardSize][boardSize];
        board[fixedX][fixedY] = -1; // Preserve fixed tile
        currentNum = 1;
    }
}
