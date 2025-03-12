package view;

import java.awt.*;
import javax.swing.*;
import main.TrominoMain;

/**
 *
 * @author tonitorres
 */
public class TrominoPanel extends JPanel {

    private int[][] board;
    private int fixedX = -1, fixedY = -1;
    private boolean tileSelected = false;
    private boolean solvingStarted = false;
    private final TrominoMain principal;

    public TrominoPanel(int size, TrominoMain p) {
        principal = p;
        setPreferredSize(new Dimension(768, 768)); // Multiplo de 2
        board = new int[size][size];

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (solvingStarted) {
                    return;
                }

                int panelSize = Math.min(getWidth(), getHeight());
                int tileSize = panelSize / board.length;

                int x = e.getY() / tileSize;
                int y = e.getX() / tileSize;

                if (x >= board.length || y >= board.length) {
                    return;
                }

                if (tileSelected) {
                    board[fixedX][fixedY] = 0;
                }

                fixedX = x;
                fixedY = y;
                board[fixedX][fixedY] = -1;
                tileSelected = true;
                repaint();
            }
        });
    }

    public void updateBoard(int[][] newBoard) {
        board = newBoard;
        solvingStarted = true;
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
        int panelSize = Math.min(getWidth(), getHeight());
        int tileSize = panelSize / board.length;

        if (!solvingStarted) {
            g.setColor(Color.BLACK);
            for (int i = 0; i <= board.length; i++) {
                g.drawLine(0, i * tileSize, board.length * tileSize, i * tileSize);
                g.drawLine(i * tileSize, 0, i * tileSize, board.length * tileSize);
            }
        }

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                int x = j * tileSize;
                int y = i * tileSize;

                if (board[i][j] == -1) {
                    g.setColor(Color.BLACK);
                    g.fillRect(x, y, tileSize, tileSize);
                } else if (board[i][j] > 0) {
                    g.setColor(principal.getModel().getColorForTromino(i, j));
                    g.fillRect(x, y, tileSize, tileSize);
                }
            }
        }

        // Draw borders around each tromino piece
        g.setColor(Color.BLACK);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] > 0) {
                    int x = j * tileSize;
                    int y = i * tileSize;

                    // Check if this is the edge of a tromino (to avoid inner borders)
                    if (i == 0 || board[i - 1][j] != board[i][j]) // Top
                    {
                        g.drawLine(x, y, x + tileSize, y);
                    }
                    if (j == 0 || board[i][j - 1] != board[i][j]) // Left
                    {
                        g.drawLine(x, y, x, y + tileSize);
                    }
                    if (i == board.length - 1 || board[i + 1][j] != board[i][j]) // Bottom
                    {
                        g.drawLine(x, y + tileSize, x + tileSize, y + tileSize);
                    }
                    if (j == board[i].length - 1 || board[i][j + 1] != board[i][j]) // Right
                    {
                        g.drawLine(x + tileSize, y, x + tileSize, y + tileSize);
                    }
                }
            }
        }
    }
}
