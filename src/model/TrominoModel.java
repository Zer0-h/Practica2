package model;

import java.awt.Color;

public class TrominoModel {

    private int[][] board;
    private int boardSize;
    private int fixedX, fixedY;
    private int trominoCounter = 1;
    private final Color[] colors = {Color.RED, Color.GREEN, Color.YELLOW, Color.MAGENTA};

    public TrominoModel(int size, int fixedX, int fixedY) {
        this.boardSize = size;
        this.fixedX = fixedX;
        this.fixedY = fixedY;
        this.board = new int[size][size];
        board[fixedX][fixedY] = -1; // Mark fixed tile
    }

    public void solveTromino() {
        placeTromino(0, 0);
    }

    private boolean placeTromino(int row, int col) {
        if (row >= boardSize) {
            return true;
        }

        int nextRow = (col + 1) >= boardSize ? row + 1 : row;
        int nextCol = (col + 1) >= boardSize ? 0 : col + 1;

        if (board[row][col] != 0) {
            return placeTromino(nextRow, nextCol);
        }

        // Try placing each L-shaped tromino
        for (int[][] shape : new int[][][]{
            {{0, 0}, {0, 1}, {1, 0}}, // L-shape 1
            {{0, 0}, {0, 1}, {1, 1}}, // L-shape 2
            {{0, 0}, {1, 0}, {1, 1}}, // L-shape 3
            {{0, 1}, {1, 0}, {1, 1}} // L-shape 4
        }) {
            if (canPlace(row, col, shape)) {
                for (int[] s : shape) {
                    board[row + s[0]][col + s[1]] = trominoCounter;
                }
                trominoCounter++;

                if (placeTromino(nextRow, nextCol)) {
                    return true;
                }

                // Backtrack
                for (int[] s : shape) {
                    board[row + s[0]][col + s[1]] = 0;
                }
                trominoCounter--;
            }
        }

        return false;
    }

    private boolean canPlace(int row, int col, int[][] shape) {
        for (int[] s : shape) {
            int r = row + s[0];
            int c = col + s[1];
            if (r >= boardSize || c >= boardSize || board[r][c] != 0) {
                return false;
            }
        }
        return true;
    }

    public int[][] getBoard() {
        return board;
    }

    public void clearBoard() {
        board = new int[boardSize][boardSize];
        board[fixedX][fixedY] = -1; // Reset fixed tile
        trominoCounter = 1;
    }
}
