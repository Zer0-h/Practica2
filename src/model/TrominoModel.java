package model;

import java.awt.Color;
import javax.swing.SwingUtilities;
import view.TrominoPanel;
import view.TrominoView;

public class TrominoModel {

    private int[][] board;
    private int boardSize;
    private int fixedX, fixedY;
    private int currentNum;
    private TrominoPanel view;
    private TrominoView mainView; // Reference to the UI for speed control
    private volatile boolean isStopped = false;

    private static final Color[] TROMINO_COLORS = {
        Color.RED, Color.BLUE, Color.MAGENTA, Color.YELLOW, Color.GREEN,
        Color.ORANGE, Color.CYAN, Color.PINK
    };

    public TrominoModel(int size, int fixedX, int fixedY, TrominoPanel view, TrominoView mainView) {
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

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = 0;
            }
        }
        board[fixedX][fixedY] = -1;
    }

    public void solveTromino() {
        isStopped = false;
        tileRec(boardSize, 0, 0, fixedX, fixedY);
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
            double sleepTime = mainView.getSelectedSpeed() * 1000; // Convert to milliseconds
            Thread.sleep((long) sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static Color getColorForTromino(int id) {
        if (id <= 0) {
            return Color.WHITE;
        }
        return TROMINO_COLORS[(id - 1) % TROMINO_COLORS.length];
    }
}
