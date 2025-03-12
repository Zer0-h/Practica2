package model;

import java.awt.Color;

/**
 *
 * @author tonitorres
 */
public class TrominoModel {

    private int[][] board;
    private int boardSize;
    public double trominoConstant;
    public double lastExecutionTime;
    public int currentNum;
    public int initialEmptySquareX;
    public int initialEmptySquareY;

    public TrominoModel() {
        trominoConstant = 1.0;
    }

    public int getInitialEmptySquareX() {
        return initialEmptySquareX;
    }

    public int getInitialEmptySquareY() {
        return initialEmptySquareY;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setLastExecutionTime(double value) {
        lastExecutionTime = value;
    }

    public double getLastExecutionTime() {
        return lastExecutionTime;
    }

    public void setTrominoConstant(double elapsedTime) {
        trominoConstant = (elapsedTime * 1.0) / Math.pow(4, Math.log(boardSize) / Math.log(2));
    }

    public double estimateTrominoExecutionTime() {
        long estimatedCalls = (long) Math.pow(4, Math.log(boardSize) / Math.log(2));
        return (trominoConstant * estimatedCalls) / 1000.0;
    }

    public void preparar(int size, int fixedX, int fixedY) {
        int actualSize = 1;
        while (actualSize < size) {
            actualSize *= 2;
        }
        this.boardSize = actualSize;
        this.board = new int[boardSize][boardSize];
        this.currentNum = 1;
        this.initialEmptySquareX = fixedX;
        this.initialEmptySquareY = fixedY;

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = 0;
            }
        }
        board[fixedX][fixedY] = -1;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public boolean isBoardSpaceEmpty(int x, int y) {
        return board[x][y] == 0;
    }

    public void setTrominoToBoard(int x, int y) {
        board[x][y] = currentNum;
    }

    public void increaseCurrentTromino() {
        currentNum++;
    }

    public Color getColorForTromino(int x, int y) {
        int trominoNumber = board[x][y];

        if (trominoNumber <= 0) {
            return Color.WHITE;
        }
        return new Color[]{
            Color.RED, Color.BLUE, Color.MAGENTA, Color.YELLOW, Color.GREEN,
            Color.ORANGE, Color.CYAN, Color.PINK
        }[(trominoNumber - 1) % 8];
    }
}
