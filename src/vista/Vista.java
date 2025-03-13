package vista;

import java.awt.*;
import javax.swing.*;
import main.Practica2;
import model.Model;
import model.Notificacio;
import model.Notificar;

/**
 *
 * @author tonitorres
 */
public class Vista extends JFrame implements Notificar {

    private final JButton iniciarButton, netejarButton, aturarButton;
    private JComboBox<Integer> selectorMida;
    private final JLabel tempsLabel;
    private TaulerPanel taulerPanel;
    private double tempsEstimat = 0.0;
    private final Practica2 principal;

    public Vista(Practica2 p) {
        principal = p;
        setTitle("Pràctica 2 - Backtracking");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Control Panel
        JPanel panelControl = new JPanel();
        selectorMida = new JComboBox<>(principal.getModel().getMidesSeleccionables());
        iniciarButton = new JButton("Iniciar");
        netejarButton = new JButton("Neteja");
        aturarButton = new JButton("Aturar");

        iniciarButton.setEnabled(false);
        netejarButton.setEnabled(false);
        aturarButton.setEnabled(false);

        panelControl.add(new JLabel("Mida del tauler:"));
        panelControl.add(selectorMida);
        panelControl.add(iniciarButton);
        panelControl.add(netejarButton);
        panelControl.add(aturarButton);

        add(panelControl, BorderLayout.NORTH);

        taulerPanel = new TaulerPanel(principal);
        add(taulerPanel, BorderLayout.CENTER);

        JPanel panelTemps = new JPanel();
        tempsLabel = new JLabel("Temps Estimat: -- s | Temps Real: -- s"); // Text per defecte

        panelTemps.add(tempsLabel);
        add(panelTemps, BorderLayout.SOUTH);

        selectorMida.addActionListener(e -> {
            Model model = principal.getModel();
            if (!model.getEnProces()) {
                principal.getModel().construirTauler((int) selectorMida.getSelectedItem());
                taulerPanel.pintar();
            }
        });

        iniciarButton.addActionListener(e -> {
            principal.notificar(Notificacio.ARRANCAR);

            selectorMida.setEnabled(false);
            iniciarButton.setEnabled(false);
            netejarButton.setEnabled(false);
            aturarButton.setEnabled(true);

            Model model = principal.getModel();
            setTempsEstimat(model.estimaTempsExecucio());
        });

        netejarButton.addActionListener(e -> {
            principal.getModel().construirTauler((int) selectorMida.getSelectedItem());
            taulerPanel.pintar();

            netejarButton.setEnabled(false);
        });

        aturarButton.addActionListener(e -> {
            principal.notificar(Notificacio.ATURAR);

            selectorMida.setEnabled(true);
            netejarButton.setEnabled(true);
            aturarButton.setEnabled(false);
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setTempsEstimat(double tempsEstimat) {
        this.tempsEstimat = tempsEstimat;
        actualitzaEtiquetaTemps(null);
    }

    public void setTempsReal(double tempsReal) {
        actualitzaEtiquetaTemps(tempsReal);
    }

    private void actualitzaEtiquetaTemps(Double tempsReal) {
        if (tempsReal == null) {
            tempsLabel.setText(String.format("Temps Estimat: %.2f s | Temps Real: -- s", tempsEstimat));
        } else {
            tempsLabel.setText(String.format("Temps Estimat: %.2f s | Temps Real: %.2f s", tempsEstimat, tempsReal));
        }
    }

    @Override
    public void notificar(Notificacio n) {
        switch (n) {
            case Notificacio.PINTAR -> {
                taulerPanel.pintar();
            }
            case Notificacio.FINALITZA -> {
                setTempsReal(principal.getModel().getTempsExecucio());

                selectorMida.setEnabled(true);
                netejarButton.setEnabled(true);
                aturarButton.setEnabled(false);
            }
            case Notificacio.SELECCIONA -> {
                iniciarButton.setEnabled(true);
                netejarButton.setEnabled(true);
            }
        }
    }
}
