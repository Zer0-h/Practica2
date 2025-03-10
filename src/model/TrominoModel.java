package model;

public class TrominoModel {

    private int[][] board;
    private int boardSize;
    private int fixedX, fixedY;
    private int trominoCounter = 1;

    public TrominoModel(int size, int fixedX, int fixedY) {
        this.boardSize = size;
        this.fixedX = fixedX;
        this.fixedY = fixedY;
        this.board = new int[size][size];
        board[fixedX][fixedY] = -1; // Mark fixed tile
    }

    public void solveTromino() {
        tromino(0, 0, boardSize, fixedX, fixedY);
    }

    private void tromino(int x, int y, int size, int fx, int fy) {
        if (size == 2) { // Base case: 2x2 grid
            int t = trominoCounter++;
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    if (board[x + i][y + j] == 0) {
                        board[x + i][y + j] = t;
                    }
                }
            }
            return;
        }

        int mid = size / 2;
        int centerX = x + mid, centerY = y + mid;

        // Determine which quadrant contains the fixed tile
        int[] qx = {x, x, centerX, centerX};
        int[] qy = {y, centerY, y, centerY};
        int quadrant = (fx >= centerX ? 2 : 0) + (fy >= centerY ? 1 : 0);

        // Place tromino at center, leaving one tile open in each sub-board
        for (int i = 0; i < 4; i++) {
            if (i != quadrant) {
                board[qx[i] + (i / 2)][qy[i] + (i % 2)] = trominoCounter;
            }
        }
        trominoCounter++;

        // Recursively fill each quadrant
        for (int i = 0; i < 4; i++) {
            tromino(qx[i], qy[i], mid, i == quadrant ? fx : qx[i] + (i / 2), i == quadrant ? fy : qy[i] + (i % 2));
        }
    }

    public int[][] getBoard() {
        return board;
    }
}
