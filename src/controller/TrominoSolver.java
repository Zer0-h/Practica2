package controller;

import java.awt.Color;
import javax.swing.SwingUtilities;
import view.TrominoPanel;
import view.TrominoView;

/**
 * Handles the recursive Tromino Tiling solution in a separate thread.
 */
public class TrominoSolver extends Thread {

    private int[][] board;
    private int boardSize;
    private int fixedX, fixedY;
    private int currentNum;
    private TrominoPanel view;
    private TrominoView mainView;
    private volatile boolean isStopped = false;
    private static double CM = 1.0; // Constant for execution time estimation

    public TrominoSolver(int size, int fixedX, int fixedY, TrominoPanel view, TrominoView mainView) {
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
        this.mainView = mainView;

        // Initialize board with all empty tiles
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = 0;
            }
        }
        board[fixedX][fixedY] = -1;
    }

    @Override
    public void run() {
        mainView.setEstimatedTime(estimateExecutionTime()); // Display estimated time
        isStopped = false;
        long startTime = System.currentTimeMillis();

        tileRec(boardSize, 0, 0, fixedX, fixedY);

        if (!isStopped) {
            mainView.setSolving(false);
        }

        long elapsedTime = System.currentTimeMillis() - startTime;
        CM = (elapsedTime * 1.0) / Math.pow(4, Math.log(boardSize) / Math.log(2));

        // Update actual execution time in UI
        mainView.setActualTime(elapsedTime / 1000.0);
    }

    private void tileRec(int size, int topX, int topY, int holeX, int holeY) {
        if (isStopped) {
            return;
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

    public void stopSolver() {
        isStopped = true;
        mainView.setSolving(false);
    }

    private void updateView() {
        SwingUtilities.invokeLater(() -> view.updateBoard(board));
    }

    private void sleep() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public double estimateExecutionTime() {
        long estimatedCalls = (long) Math.pow(4, Math.log(boardSize) / Math.log(2));
        return (CM * estimatedCalls) / 1000.0; // Convert to seconds
    }

    public static Color getColorForTromino(int id) {
        if (id <= 0) {
            return Color.WHITE;
        }
        return new Color[]{
            Color.RED, Color.BLUE, Color.MAGENTA, Color.YELLOW, Color.GREEN,
            Color.ORANGE, Color.CYAN, Color.PINK
        }[(id - 1) % 8];
    }
}
