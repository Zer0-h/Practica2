package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class TrominoPanel extends JPanel {

    private int[][] board;
    private int fixedX = -1, fixedY = -1;

    public TrominoPanel(int size) {
        setPreferredSize(new Dimension(600, 600));
        board = new int[size][size];

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int tileSize = getWidth() / board.length;
                fixedX = e.getY() / tileSize;
                fixedY = e.getX() / tileSize;
                board = new int[board.length][board.length]; // Reset board
                board[fixedX][fixedY] = -1; // Set clicked tile as fixed
                repaint();
            }
        });
    }

    public void updateBoard(int[][] newBoard) {
        this.board = newBoard;
        repaint();
    }

    public void clearBoard() {
        board = new int[board.length][board.length];
        fixedX = -1;
        fixedY = -1;
        repaint();
    }

    public int getFixedX() {
        return fixedX;
    }

    public int getFixedY() {
        return fixedY;
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
