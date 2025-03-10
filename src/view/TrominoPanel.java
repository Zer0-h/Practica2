package view;

import java.awt.*;
import javax.swing.*;

public class TrominoPanel extends JPanel {

    private int[][] board;

    public TrominoPanel(int size) {
        setPreferredSize(new Dimension(600, 600));
        board = new int[size][size];
    }

    public void updateBoard(int[][] newBoard) {
        this.board = newBoard;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int tileSize = getWidth() / board.length;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == -1) {
                    g.setColor(Color.BLACK);
                } else if (board[i][j] > 0) {
                    g.setColor(Color.BLUE);
                } else {
                    g.setColor(Color.WHITE);
                }

                g.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);
                g.setColor(Color.BLACK);
                g.drawRect(j * tileSize, i * tileSize, tileSize, tileSize);
            }
        }
    }
}
