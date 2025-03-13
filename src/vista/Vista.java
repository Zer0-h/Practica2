package vista;

import java.awt.*;
import javax.swing.*;
import main.Practica2;
import model.Model;
import model.Notificacio;
import model.Notificar;

public class Vista extends JFrame implements Notificar {

    private final JButton btnIniciar, btnNetejar, btnAturar;
    private final JComboBox<Integer> selectorMida;
    private JLabel lblTemps;
    private final TaulerPanel taulerPanel;
    private final Practica2 principal;
    private double tempsEstimat = 0.0;

    public Vista(Practica2 p) {
        principal = p;
        setTitle("Pràctica 2 - Backtracking");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        selectorMida = new JComboBox<>(principal.getModel().getMidesSeleccionables());
        btnIniciar = new JButton("Iniciar");
        btnNetejar = new JButton("Neteja");
        btnAturar = new JButton("Aturar");

        inicialitzarBotons();
        JPanel panelControl = crearPanellControl();
        JPanel panelTemps = crearPanellTemps();

        add(panelControl, BorderLayout.NORTH);
        taulerPanel = new TaulerPanel(principal);
        add(taulerPanel, BorderLayout.CENTER);
        add(panelTemps, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void inicialitzarBotons() {
        btnIniciar.setEnabled(false);
        btnNetejar.setEnabled(false);
        btnAturar.setEnabled(false);

        selectorMida.addActionListener(e -> {
            Model model = principal.getModel();
            if (!model.getEnExecucio()) {
                model.inicialitzaTauler((int) selectorMida.getSelectedItem());
                taulerPanel.pintar();
            }
        });

        btnIniciar.addActionListener(e -> {
            principal.notificar(Notificacio.ARRANCAR);
            activarModeExecucio();
            setTempsEstimat(principal.getModel().estimaTempsExecucio());
        });

        btnNetejar.addActionListener(e -> {
            principal.getModel().inicialitzaTauler((int) selectorMida.getSelectedItem());
            taulerPanel.pintar();
            btnNetejar.setEnabled(false);
        });

        btnAturar.addActionListener(e -> {
            principal.notificar(Notificacio.ATURAR);
            desactivarModeExecucio();
        });
    }

    private JPanel crearPanellControl() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Mida del tauler:"));
        panel.add(selectorMida);
        panel.add(btnIniciar);
        panel.add(btnNetejar);
        panel.add(btnAturar);
        return panel;
    }

    private JPanel crearPanellTemps() {
        JPanel panel = new JPanel();
        lblTemps = new JLabel("Temps Estimat: -- s | Temps Real: -- s");
        panel.add(lblTemps);
        return panel;
    }

    private void activarModeExecucio() {
        selectorMida.setEnabled(false);
        btnIniciar.setEnabled(false);
        btnNetejar.setEnabled(false);
        btnAturar.setEnabled(true);
    }

    private void desactivarModeExecucio() {
        selectorMida.setEnabled(true);
        btnNetejar.setEnabled(true);
        btnAturar.setEnabled(false);
    }

    public void setTempsEstimat(double tempsEstimat) {
        this.tempsEstimat = tempsEstimat;
        actualitzaEtiquetaTemps(null);
    }

    public void setTempsReal(double tempsReal) {
        actualitzaEtiquetaTemps(tempsReal);
    }

    private void actualitzaEtiquetaTemps(Double tempsReal) {
        lblTemps.setText(
                tempsReal == null
                        ? String.format("Temps Estimat: %.2f s | Temps Real: -- s", tempsEstimat)
                        : String.format("Temps Estimat: %.2f s | Temps Real: %.2f s", tempsEstimat, tempsReal)
        );
    }

    @Override
    public void notificar(Notificacio notificacio) {
        switch (notificacio) {
            case Notificacio.PINTAR ->
                taulerPanel.pintar();
            case Notificacio.FINALITZA -> {
                setTempsReal(principal.getModel().getTempsExecucio());
                desactivarModeExecucio();
            }
            case Notificacio.SELECCIONA -> {
                btnIniciar.setEnabled(true);
                btnNetejar.setEnabled(true);
            }
        }
    }
}
