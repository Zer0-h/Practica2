package vista;

import java.awt.*;
import javax.swing.*;
import main.Practica2;
import model.Model;
import model.Notificacio;

public class TaulerPanel extends JPanel {

    private final Practica2 principal;
    private final Model model;

    public TaulerPanel(Practica2 p) {
        principal = p;
        model = principal.getModel();
        setPreferredSize(new Dimension(768, 768));

        setDoubleBuffered(true);

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (model.getEnExecucio()) {
                    return;
                }

                int midaPanel = Math.min(getWidth(), getHeight());
                int midaCasella = midaPanel / model.getMidaTauler();

                int fila = e.getY() / midaCasella;
                int columna = e.getX() / midaCasella;

                if (fila >= model.getMidaTauler() || columna >= model.getMidaTauler()) {
                    return;
                }

                if (model.hiHaForatSeleccionat()) {
                    model.netejarForat();
                }

                model.assignarForat(fila, columna);
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
        int midaCasella = midaPanel / model.getMidaTauler();

        dibuixarQuadrícula(g2, midaCasella);
        omplirCaselles(g2, midaCasella);
        dibuixarBordesTrominos(g2, midaCasella);
    }

    private void dibuixarQuadrícula(Graphics2D g2, int midaCasella) {
        g2.setColor(Color.BLACK);
        int midaTauler = model.getMidaTauler();

        for (int i = 0; i <= midaTauler; i++) {
            g2.drawLine(0, i * midaCasella, midaTauler * midaCasella, i * midaCasella);
            g2.drawLine(i * midaCasella, 0, i * midaCasella, midaTauler * midaCasella);
        }
    }

    private void omplirCaselles(Graphics2D g2, int midaCasella) {
        int midaTauler = model.getMidaTauler();

        for (int fila = 0; fila < midaTauler; fila++) {
            for (int columna = 0; columna < midaTauler; columna++) {
                int x = columna * midaCasella;
                int y = fila * midaCasella;

                if (model.esCasellaForat(fila, columna)) {
                    g2.setColor(Color.BLACK);
                } else if (model.esCasellaTromino(fila, columna)) {
                    g2.setColor(principal.getModel().getColorPerTromino(fila, columna));
                } else {
                    continue;
                }

                g2.fillRect(x, y, midaCasella, midaCasella);
            }
        }
    }

    private void dibuixarBordesTrominos(Graphics2D g2, int midaCasella) {
        g2.setColor(Color.BLACK);
        int midaTauler = model.getMidaTauler();

        for (int fila = 0; fila < midaTauler; fila++) {
            for (int columna = 0; columna < midaTauler; columna++) {
                if (model.esCasellaTromino(fila, columna)) {
                    int x = columna * midaCasella;
                    int y = fila * midaCasella;

                    if (model.esVoraSuperiorTromino(fila, columna)) {
                        g2.drawLine(x, y, x + midaCasella, y);
                    }
                    if (model.esVoraEsquerraTromino(fila, columna)) {
                        g2.drawLine(x, y, x, y + midaCasella);
                    }
                    if (model.esVoraInferiorTromino(fila, columna)) {
                        g2.drawLine(x, y + midaCasella, x + midaCasella, y + midaCasella);
                    }
                    if (model.esVoraDretaTromino(fila, columna)) {
                        g2.drawLine(x + midaCasella, y, x + midaCasella, y + midaCasella);
                    }
                }
            }
        }
    }
}
