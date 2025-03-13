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
        setPreferredSize(new Dimension(768, 768)); // Multiplo de 2

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
        if (this.getGraphics() != null) {
            paintComponent(this.getGraphics());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int panelSize = Math.min(getWidth(), getHeight());
        int tileSize = panelSize / model.getBoardLength();

        if (!model.getEnproces()) {
            g.setColor(Color.BLACK);
            for (int i = 0; i <= model.getBoardLength(); i++) {
                g.drawLine(0, i * tileSize, model.getBoardLength() * tileSize, i * tileSize);
                g.drawLine(i * tileSize, 0, i * tileSize, model.getBoardLength() * tileSize);
            }
        }

        for (int i = 0; i < model.getBoardLength(); i++) {
            for (int j = 0; j < model.getBoardLength(); j++) {
                int x = j * tileSize;
                int y = i * tileSize;

                if (model.isBoardSpaceVoid(i, j)) {
                    g.setColor(Color.BLACK);
                    g.fillRect(x, y, tileSize, tileSize);
                } else if (model.isBoardSpaceWithTromino(i, j)) {
                    g.setColor(principal.getModel().getColorForTromino(i, j));
                    g.fillRect(x, y, tileSize, tileSize);
                }
            }
        }

        // Draw borders around each tromino piece
        g.setColor(Color.BLACK);
        for (int i = 0; i < model.getBoardLength(); i++) {
            for (int j = 0; j < model.getBoardLength(); j++) {
                if (model.isBoardSpaceWithTromino(i, j)) {
                    int x = j * tileSize;
                    int y = i * tileSize;

                    // Check if this is the edge of a tromino (to avoid inner borders)
                    if (model.isBoardSpaceTopEdge(i, j)) {
                        g.drawLine(x, y, x + tileSize, y);
                    }
                    if (model.isBoardSpaceLeftEdge(i, j)) {
                        g.drawLine(x, y, x, y + tileSize);
                    }
                    if (model.isBoardSpaceBottomEdge(i, j)) {
                        g.drawLine(x, y + tileSize, x + tileSize, y + tileSize);
                    }
                    if (model.isBoardSpaceRightEdge(i, j)) {
                        g.drawLine(x + tileSize, y, x + tileSize, y + tileSize);
                    }
                }
            }
        }
    }
}
