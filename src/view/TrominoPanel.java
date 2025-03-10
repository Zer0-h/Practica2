package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class TrominoPanel extends JPanel {

    private int[][] board;
    private int fixedX = -1, fixedY = -1;
    private boolean tileSelected = false;

    public TrominoPanel(int size) {
        setPreferredSize(new Dimension(600, 600));
        board = new int[size][size];

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int tileSize = getWidth() / board.length;
                int x = e.getY() / tileSize;
                int y = e.getX() / tileSize;

                // Set the missing tile if not already selected
                if (!tileSelected) {
                    fixedX = x;
                    fixedY = y;
                    board = new int[board.length][board.length]; // Reset board
                    board[fixedX][fixedY] = -1; // Set clicked tile as fixed
                    tileSelected = true;
                    repaint();
                }
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
        tileSelected = false;
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
        g.setColor(Color.BLACK);

        // Step 1: Draw the fixed tile (black square)
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                int x = j * tileSize;
                int y = i * tileSize;

                if (board[i][j] == -1) {
                    g.setColor(Color.BLACK);
                    g.fillRect(x, y, tileSize, tileSize);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, tileSize, tileSize);
                }
            }
        }

        // Step 2: Draw the trominoes' borders
        g.setColor(Color.BLACK);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] > 0) { // Part of a tromino
                    int x = j * tileSize;
                    int y = i * tileSize;

                    // Draw only the borders of tromino tiles
                    if (i == 0 || board[i - 1][j] != board[i][j]) // Top border
                    {
                        g.drawLine(x, y, x + tileSize, y);
                    }
                    if (j == 0 || board[i][j - 1] != board[i][j]) // Left border
                    {
                        g.drawLine(x, y, x, y + tileSize);
                    }
                    if (i == board.length - 1 || board[i + 1][j] != board[i][j]) // Bottom border
                    {
                        g.drawLine(x, y + tileSize, x + tileSize, y + tileSize);
                    }
                    if (j == board[i].length - 1 || board[i][j + 1] != board[i][j]) // Right border
                    {
                        g.drawLine(x + tileSize, y, x + tileSize, y + tileSize);
                    }
                }
            }
        }
    }
}
