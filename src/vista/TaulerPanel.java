package vista;

import java.awt.*;
import javax.swing.*;
import main.Practica2;
import model.Model;
import model.Notificacio;

/**
 *
 * @author tonitorres
 */
public class TaulerPanel extends JPanel {

    private final Practica2 principal;
    private final Model model;

    public TaulerPanel(Practica2 p) {
        principal = p;
        model = principal.getModel();
        setPreferredSize(new Dimension(768, 768)); // Múltiple de 2

        // Enable double buffering
        setDoubleBuffered(true);

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (model.getEnproces()) {
                    return;
                }

                int panelSize = Math.min(getWidth(), getHeight());
                int tileSize = panelSize / model.getBoardLength();

                int x = e.getY() / tileSize;
                int y = e.getX() / tileSize;

                if (x >= model.getBoardLength() || y >= model.getBoardLength()) {
                    return;
                }

                if (model.seleccionatEspaiBuit()) {
                    model.resetPreviousEmptySpace();
                }

                model.setInitialEmptySquare(x, y);
                principal.notificar(Notificacio.SELECCIONA);
                repaint();
            }
        });
    }

    public void pintar() {
        repaint(); // Use repaint() instead of directly calling paintComponent()
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Clears the panel properly before redrawing

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int panelSize = Math.min(getWidth(), getHeight());
        int tileSize = panelSize / model.getBoardLength();

        // Draw grid lines
        g2.setColor(Color.BLACK);
        for (int i = 0; i <= model.getBoardLength(); i++) {
            g2.drawLine(0, i * tileSize, model.getBoardLength() * tileSize, i * tileSize);
            g2.drawLine(i * tileSize, 0, i * tileSize, model.getBoardLength() * tileSize);
        }

        // Fill board tiles
        for (int i = 0; i < model.getBoardLength(); i++) {
            for (int j = 0; j < model.getBoardLength(); j++) {
                int x = j * tileSize;
                int y = i * tileSize;

                if (model.isBoardSpaceVoid(i, j)) {
                    g2.setColor(Color.BLACK);
                    g2.fillRect(x, y, tileSize, tileSize);
                } else if (model.isBoardSpaceWithTromino(i, j)) {
                    g2.setColor(principal.getModel().getColorForTromino(i, j));
                    g2.fillRect(x, y, tileSize, tileSize);
                }
            }
        }

        // Draw borders around each tromino piece
        g2.setColor(Color.BLACK);
        for (int i = 0; i < model.getBoardLength(); i++) {
            for (int j = 0; j < model.getBoardLength(); j++) {
                if (model.isBoardSpaceWithTromino(i, j)) {
                    int x = j * tileSize;
                    int y = i * tileSize;

                    // Draw only external edges of the tromino (avoid internal borders)
                    if (model.isTrominoPieceTopEdge(i, j)) {
                        g2.drawLine(x, y, x + tileSize, y);
                    }
                    if (model.isTrominoPieceLeftEdge(i, j)) {
                        g2.drawLine(x, y, x, y + tileSize);
                    }
                    if (model.isTrominoPieceBottomEdge(i, j)) {
                        g2.drawLine(x, y + tileSize, x + tileSize, y + tileSize);
                    }
                    if (model.isTrominoPieceRightEdge(i, j)) {
                        g2.drawLine(x + tileSize, y, x + tileSize, y + tileSize);
                    }
                }
            }
        }
    }
}
