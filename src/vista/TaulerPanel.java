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
                if (model.getEnProces()) {
                    return;
                }

                int midaPanel = Math.min(getWidth(), getHeight());
                int midaCasella = midaPanel / model.tamanyTauler();

                int x = e.getY() / midaCasella;
                int y = e.getX() / midaCasella;

                if (x >= model.tamanyTauler() || y >= model.tamanyTauler()) {
                    return;
                }

                if (model.seleccionatEspaiBuit()) {
                    model.llevaForatAnterior();
                }

                model.colocaForat(x, y);
                principal.notificar(Notificacio.SELECCIONA);
                repaint();
            }
        });
    }

    public void pintar() {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int midaPanel = Math.min(getWidth(), getHeight());
        int midaCasella = midaPanel / model.tamanyTauler();

        // Draw grid lines
        g2.setColor(Color.BLACK);
        for (int i = 0; i <= model.tamanyTauler(); i++) {
            g2.drawLine(0, i * midaCasella, model.tamanyTauler() * midaCasella, i * midaCasella);
            g2.drawLine(i * midaCasella, 0, i * midaCasella, model.tamanyTauler() * midaCasella);
        }

        // Fill board tiles
        for (int i = 0; i < model.tamanyTauler(); i++) {
            for (int j = 0; j < model.tamanyTauler(); j++) {
                int x = j * midaCasella;
                int y = i * midaCasella;

                if (model.esEspaiAmbForat(i, j)) {
                    g2.setColor(Color.BLACK);
                    g2.fillRect(x, y, midaCasella, midaCasella);
                } else if (model.esEspaiAmbTromino(i, j)) {
                    g2.setColor(principal.getModel().getColorPerTromino(i, j));
                    g2.fillRect(x, y, midaCasella, midaCasella);
                }
            }
        }

        // Draw borders around each tromino piece
        g2.setColor(Color.BLACK);
        for (int i = 0; i < model.tamanyTauler(); i++) {
            for (int j = 0; j < model.tamanyTauler(); j++) {
                if (model.esEspaiAmbTromino(i, j)) {
                    int x = j * midaCasella;
                    int y = i * midaCasella;

                    // Draw only external edges of the tromino (avoid internal borders)
                    if (model.esVoraSuperiorTromino(i, j)) {
                        g2.drawLine(x, y, x + midaCasella, y);
                    }
                    if (model.esVoraEsquerraTromino(i, j)) {
                        g2.drawLine(x, y, x, y + midaCasella);
                    }
                    if (model.esVoraInferiorTromino(i, j)) {
                        g2.drawLine(x, y + midaCasella, x + midaCasella, y + midaCasella);
                    }
                    if (model.esVoraDretaTromino(i, j)) {
                        g2.drawLine(x + midaCasella, y, x + midaCasella, y + midaCasella);
                    }
                }
            }
        }
    }
}
