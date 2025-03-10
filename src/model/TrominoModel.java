package model;

import javax.swing.SwingUtilities;
import view.TrominoPanel;

public class TrominoModel {

    private int[][] board;
    private int boardSize;
    private int fixedX, fixedY;
    private int currentNum;
    private TrominoPanel view;
    private volatile boolean isStopped = false; // Stop flag

    public TrominoModel(int size, int fixedX, int fixedY, TrominoPanel view) {
        int actualSize = 1;
        while (actualSize < size) {
            actualSize *= 2;
        }
        this.boardSize = actualSize;
        this.fixedX = fixedX;
        this.fixedY = fixedY;
        this.board = new int[boardSize][boardSize];
        this.currentNum = 1;
        this.view = view;

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = 0;
            }
        }
        board[fixedX][fixedY] = -1;
    }

    public void solveTromino() {
        isStopped = false; // Reset stop flag
        tileRec(boardSize, 0, 0, fixedX, fixedY);
    }

    private void tileRec(int size, int topX, int topY, int holeX, int holeY) {
        if (isStopped) {
            return; // Stop immediately if requested
        }
        if (size == 2) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (board[topX + i][topY + j] == 0) {
                        board[topX + i][topY + j] = currentNum;
                    }
                }
            }
            currentNum++;
            updateView();
            sleep();
            return;
        }

        int centerX = topX + size / 2 - 1;
        int centerY = topY + size / 2 - 1;

        boolean inUpperLeft = holeX < topX + size / 2 && holeY < topY + size / 2;
        boolean inUpperRight = holeX < topX + size / 2 && holeY >= topY + size / 2;
        boolean inBottomLeft = holeX >= topX + size / 2 && holeY < topY + size / 2;
        boolean inBottomRight = holeX >= topX + size / 2 && holeY >= topY + size / 2;

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
        updateView();
        sleep();

        tileRec(size / 2, topX, topY, inUpperLeft ? holeX : centerX, inUpperLeft ? holeY : centerY);
        tileRec(size / 2, topX, topY + size / 2, inUpperRight ? holeX : centerX, inUpperRight ? holeY : centerY + 1);
        tileRec(size / 2, topX + size / 2, topY, inBottomLeft ? holeX : centerX + 1, inBottomLeft ? holeY : centerY);
        tileRec(size / 2, topX + size / 2, topY + size / 2, inBottomRight ? holeX : centerX + 1, inBottomRight ? holeY : centerY + 1);
    }

    public void stopTromino() {
        isStopped = true;
    }

    public int[][] getBoard() {
        return board;
    }

    private void updateView() {
        SwingUtilities.invokeLater(() -> view.updateBoard(board));
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
