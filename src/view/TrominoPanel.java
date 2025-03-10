package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class TrominoPanel extends JPanel {

    private int[][] board;
    private int fixedX = -1, fixedY = -1;
    private boolean tileSelected = false;
    private boolean solvingStarted = false;

    public TrominoPanel(int size) {
        setPreferredSize(new Dimension(600, 600));
        board = new int[size][size];

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (solvingStarted) {
                    return; // Prevent changes after solving
                }
                int panelSize = Math.min(getWidth(), getHeight()); // Ensure square fit
                int tileSize = panelSize / board.length; // Compute dynamic tile size

                int x = e.getY() / tileSize;
                int y = e.getX() / tileSize;

                // Prevent out-of-bounds clicks
                if (x >= board.length || y >= board.length) {
                    return;
                }

                // Allow changing the missing tile until solving starts
                if (tileSelected) {
                    board[fixedX][fixedY] = 0; // Reset previous tile
                }

                fixedX = x;
                fixedY = y;
                board[fixedX][fixedY] = -1; // Set new missing tile
                tileSelected = true;
                repaint();
            }
        });
    }

    public void updateBoard(int[][] newBoard) {
        board = newBoard;
        solvingStarted = true; // Hide initial grid when solving starts
        repaint();
    }

    public void clearBoard() {
        board = new int[board.length][board.length];
        fixedX = -1;
        fixedY = -1;
        tileSelected = false;
        solvingStarted = false;
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
        int panelSize = Math.min(getWidth(), getHeight()); // Square board fit
        int tileSize = panelSize / board.length; // Compute correct tile size

        // Step 1: Draw the initial grid with black borders
        if (!solvingStarted) {
            g.setColor(Color.BLACK);
            for (int i = 0; i <= board.length; i++) {
                g.drawLine(0, i * tileSize, board.length * tileSize, i * tileSize); // Horizontal lines
                g.drawLine(i * tileSize, 0, i * tileSize, board.length * tileSize); // Vertical lines
            }
        }

        // Step 2: Highlight the selected missing tile (before solving)
        if (!solvingStarted && tileSelected) {
            int x = fixedY * tileSize;
            int y = fixedX * tileSize;
            g.setColor(Color.BLACK);
            g.fillRect(x, y, tileSize, tileSize);
        }

        // Step 3: Draw the trominoes after solving
        if (solvingStarted) {
            g.setColor(Color.BLACK);
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    int x = j * tileSize;
                    int y = i * tileSize;

                    if (board[i][j] == -1) {
                        g.fillRect(x, y, tileSize, tileSize); // Keep the fixed tile black
                    } else if (board[i][j] > 0) { // Part of a tromino
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
}
