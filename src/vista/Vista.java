package vista;

import java.awt.*;
import javax.swing.*;
import main.Practica2;
import model.Model;
import model.Notificacio;
import model.Notificar;

/**
 * Classe Vista que representa la interfície gràfica de l'usuari (GUI).
 * Controla la interacció amb l'usuari i mostra l'estat del procés de resolució
 * del problema del Tromino.
 * Implementa el patró MVC com a part de la Vista.
 *
 * @author tonitorres
 */
public class Vista extends JFrame implements Notificar {

    private final JButton btnIniciar, btnNetejar, btnAturar;
    private final JComboBox<Integer> selectorMida;
    private final JComboBox<String> selectorColor;
    private JLabel lblTemps;
    private final TaulerPanel taulerPanel;
    private final Practica2 principal;
    private double tempsEstimat = 0.0;

    /**
     * Constructor que inicialitza la interfície gràfica.
     *
     * @param p Instància principal de l'aplicació
     */
    public Vista(Practica2 p) {
        principal = p;
        setTitle("Pràctica 2 - Backtracking");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Creació dels components principals de la interfície
        selectorMida = new JComboBox<>(principal.getModel().getMidesSeleccionables());
        selectorColor = new JComboBox<>(new String[]{"Blanc", "Vermell", "Blau", "Magenta", "Groc", "Verd", "Taronja", "Cian"});
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

    /**
     * Inicialitza els botons i els seus listeners per gestionar les accions de
     * l'usuari.
     */
    private void inicialitzarBotons() {
        btnIniciar.setEnabled(false);
        btnNetejar.setEnabled(false);
        btnAturar.setEnabled(false);

        // Canviar mida del tauler quan l'usuari selecciona una nova mida
        selectorMida.addActionListener(e -> {
            Model model = principal.getModel();
            if (!model.getEnExecucio()) {
                model.inicialitzaTauler((int) selectorMida.getSelectedItem());
                taulerPanel.pintar();
            }
        });

        // Canviar els colors dels trominos
        selectorColor.addActionListener(e -> {
            String colorSeleccionat = (String) selectorColor.getSelectedItem();
            Model model = principal.getModel();

            Color selectedColor;

            switch (colorSeleccionat) {
                case "Blanc" -> selectedColor = Color.WHITE;
                case "Vermell" -> selectedColor = Color.RED;
                case "Blau" -> selectedColor = Color.BLUE;
                case "Magenta" -> selectedColor = Color.MAGENTA;
                case "Groc" -> selectedColor = Color.YELLOW;
                case "Verd" -> selectedColor = Color.GREEN;
                case "Taronja" -> selectedColor = Color.ORANGE;
                case "Cian" -> selectedColor = Color.CYAN;
                default -> selectedColor = Color.WHITE;
            }
            
            model.setTrominoColor(selectedColor);
        });

        // Iniciar la resolució del problema quan es prem el botó "Iniciar"
        btnIniciar.addActionListener(e -> {
            principal.notificar(Notificacio.ARRANCAR);
            activarModeExecucio();
            setTempsEstimat(principal.getModel().estimaTempsExecucio());
        });

        // Netejar el tauler quan es prem el botó "Neteja"
        btnNetejar.addActionListener(e -> {
            principal.getModel().inicialitzaTauler((int) selectorMida.getSelectedItem());
            taulerPanel.pintar();
            btnNetejar.setEnabled(false);
        });

        // Aturar l'execució del procés quan es prem el botó "Aturar"
        btnAturar.addActionListener(e -> {
            principal.notificar(Notificacio.ATURAR);
            desactivarModeExecucio();
        });
    }

    /**
     * Crea el panell de control amb la selecció de mida i els botons.
     *
     * @return JPanel amb els controls de l'usuari
     */
    private JPanel crearPanellControl() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Mida del tauler:"));
        panel.add(selectorMida);
        panel.add(new JLabel("Color:"));
        panel.add(selectorColor);
        panel.add(btnIniciar);
        panel.add(btnNetejar);
        panel.add(btnAturar);
        return panel;
    }

    /**
     * Crea el panell de visualització del temps estimat i real.
     *
     * @return JPanel amb la informació del temps d'execució
     */
    private JPanel crearPanellTemps() {
        JPanel panel = new JPanel();
        lblTemps = new JLabel("Temps Estimat: -- s | Temps Real: -- s");
        panel.add(lblTemps);
        return panel;
    }

    /**
     * Activa el mode d'execució, desactivant alguns botons per evitar
     * interaccions incorrectes.
     */
    private void activarModeExecucio() {
        selectorMida.setEnabled(false);
        btnIniciar.setEnabled(false);
        btnNetejar.setEnabled(false);
        btnAturar.setEnabled(true);
    }

    /**
     * Desactiva el mode d'execució i permet a l'usuari interaccionar de nou amb
     * els controls.
     */
    private void desactivarModeExecucio() {
        selectorMida.setEnabled(true);
        btnNetejar.setEnabled(true);
        btnAturar.setEnabled(false);
    }

    /**
     * Estableix el temps estimat d'execució.
     *
     * @param tempsEstimat Temps estimat en segons
     */
    public void setTempsEstimat(double tempsEstimat) {
        this.tempsEstimat = tempsEstimat;
        actualitzaEtiquetaTemps(null);
    }

    /**
     * Estableix el temps real d'execució un cop finalitzada la resolució.
     *
     * @param tempsReal Temps real en segons
     */
    public void setTempsReal(double tempsReal) {
        actualitzaEtiquetaTemps(tempsReal);
    }

    /**
     * Actualitza la visualització del temps d'execució a la interfície gràfica.
     *
     * @param tempsReal Temps real d'execució, o null si encara no s'ha
     *                  finalitzat
     */
    private void actualitzaEtiquetaTemps(Double tempsReal) {
        lblTemps.setText(
                tempsReal == null
                        ? String.format("Temps Estimat: %.2f s | Temps Real: -- s", tempsEstimat)
                        : String.format("Temps Estimat: %.2f s | Temps Real: %.2f s", tempsEstimat, tempsReal)
        );
    }

    /**
     * Gestiona les notificacions rebudes del controlador i actualitza la
     * interfície gràfica en conseqüència.
     *
     * @param notificacio Tipus de notificació rebuda
     */
    @Override
    public void notificar(Notificacio notificacio) {
        switch (notificacio) {
            case Notificacio.PINTAR ->
                taulerPanel.pintar();
            case Notificacio.FINALITZA -> {
                setTempsReal(principal.getModel().getTempsExecucio());
                desactivarModeExecucio();
            }
            case Notificacio.SELECCIONA_FORAT -> {
                btnIniciar.setEnabled(true);
                btnNetejar.setEnabled(true);
            }
        }
    }
}
