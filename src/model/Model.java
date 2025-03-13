package model;

import java.awt.Color;

/**
 *
 * @author tonitorres
 */
public class Model {

    private Integer[] boardSizes;
    private int[][] board;
    private int boardSize;
    private double trominoConstant;
    private double lastExecutionTime;
    private int currentNum;
    private int initialEmptySquareX;
    private int initialEmptySquareY;
    private boolean enproces;

    public Model() {
        trominoConstant = 1.0;
        boardSizes = new Integer[]{4, 8, 16, 32, 64, 128};
        enproces = false;
    }

    public Integer[] getSelectableBoardSizes() {
        return boardSizes;
    }

    public int getInitialEmptySquareX() {
        return initialEmptySquareX;
    }

    public void setInitialEmptySquare(int x, int y) {
        initialEmptySquareX = x;
        initialEmptySquareY = y;

        board[this.initialEmptySquareX][this.initialEmptySquareY] = -1;
    }

    public int getInitialEmptySquareY() {
        return initialEmptySquareY;
    }

    public int[][] getBoard() {
        return board;
    }

    public int getBoardLength() {
        return board.length;
    }

    public boolean seleccionatEspaiBuit() {
        return initialEmptySquareX != -1 && initialEmptySquareY != -1;
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

    public void construirTauler(int size) {
        this.boardSize = size;
        this.board = new int[boardSize][boardSize];
        this.currentNum = 1;

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = 0;
            }
        }

        initialEmptySquareX = -1;
        initialEmptySquareY = -1;
    }

    public void setEnproces(boolean value) {
        enproces = value;
    }

    public boolean getEnproces() {
        return enproces;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void resetPreviousEmptySpace() {
        board[initialEmptySquareX][initialEmptySquareY] = 0;
    }

    public boolean isBoardSpaceEmpty(int x, int y) {
        return board[x][y] == 0;
    }

    public boolean isBoardSpaceVoid(int x, int y) {
        return board[x][y] == -1;
    }

    public boolean isBoardSpaceWithTromino(int x, int y) {
        return board[x][y] > 0;
    }

    public boolean isBoardSpaceTopEdge(int x, int y) {
        return x == 0 || board[x - 1][y] != board[x][y];
    }

    public boolean isBoardSpaceBottomEdge(int x, int y) {
        return x == board.length - 1 || board[x + 1][y] != board[x][y];
    }

    public boolean isBoardSpaceLeftEdge(int x, int y) {
        return y == 0 || board[x][y - 1] != board[x][y];
    }

    public boolean isBoardSpaceRightEdge(int x, int y) {
        return y == board[x].length - 1 || board[x][y + 1] != board[x][y];
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
