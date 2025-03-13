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

    private final JButton startButton, clearButton, stopButton;
    private JComboBox<Integer> sizeSelector;
    private final JLabel timeLabel; // Added time label
    private TaulerPanel boardPanel;
    private double estimatedTime = 0.0; // Store estimated time for persistence
    private final Practica2 principal;

    public Vista(Practica2 p) {
        principal = p;
        setTitle("Tromino Tiling");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Control Panel
        JPanel controlPanel = new JPanel();
        sizeSelector = new JComboBox<>(principal.getModel().getSelectableBoardSizes());
        startButton = new JButton("Solve");
        clearButton = new JButton("Clear");
        stopButton = new JButton("Stop");

        startButton.setEnabled(false);
        clearButton.setEnabled(false);
        stopButton.setEnabled(false);

        controlPanel.add(new JLabel("Board Size:"));
        controlPanel.add(sizeSelector);
        controlPanel.add(startButton);
        controlPanel.add(clearButton);
        controlPanel.add(stopButton);

        add(controlPanel, BorderLayout.NORTH);

        boardPanel = new TaulerPanel(principal);
        add(boardPanel, BorderLayout.CENTER);

        // **New Time Display Panel**
        JPanel timePanel = new JPanel();
        timeLabel = new JLabel("Estimated Time: -- s | Actual Time: -- s"); // Default text
        timePanel.add(timeLabel);
        add(timePanel, BorderLayout.SOUTH); // Add to bottom of the window

        // Listener to update board when size changes
        sizeSelector.addActionListener(e -> {
            Model model = principal.getModel();
            if (!model.getEnproces()) { // Prevent changing size while solving
                int newSize = (int) sizeSelector.getSelectedItem();

                principal.getModel().construirTauler(newSize);
                boardPanel.pintar();
            }
        });

        startButton.addActionListener(e -> {
            principal.notificar(Notificacio.ARRANCAR);

            sizeSelector.setEnabled(false);
            startButton.setEnabled(false);
            clearButton.setEnabled(false);
            stopButton.setEnabled(true);

            Model model = principal.getModel();
            setEstimatedTime(model.estimateTrominoExecutionTime());
        });

        clearButton.addActionListener(e -> {
            principal.getModel().construirTauler((int) sizeSelector.getSelectedItem());
            boardPanel.pintar();

            clearButton.setEnabled(false);
        });

        stopButton.addActionListener(e -> {
            principal.notificar(Notificacio.ATURAR);

            sizeSelector.setEnabled(true);
            clearButton.setEnabled(true);
            stopButton.setEnabled(false);
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public JButton getStartButton() {
        return startButton;
    }

    public JButton getClearButton() {
        return clearButton;
    }

    public JButton getStopButton() {
        return stopButton;
    }

    public TaulerPanel getBoardPanel() {
        return boardPanel;
    }

    public void setEstimatedTime(double estimatedTime) {
        this.estimatedTime = estimatedTime; // Store it for later comparison
        updateTimeLabel(null);
    }

    public void setActualTime(double actualTime) {
        updateTimeLabel(actualTime);
    }

    private void updateTimeLabel(Double actualTime) {
        if (actualTime == null) {
            timeLabel.setText(String.format("Estimated Time: %.2f s | Actual Time: -- s", estimatedTime));
        } else {
            timeLabel.setText(String.format("Estimated Time: %.2f s | Actual Time: %.2f s", estimatedTime, actualTime));
        }
    }

    @Override
    public void notificar(Notificacio n) {
        switch (n) {
            case Notificacio.PINTAR -> {
                boardPanel.pintar();
            }
            case Notificacio.FINALITZA -> {
                setActualTime(principal.getModel().getLastExecutionTime());

                sizeSelector.setEnabled(true);
                clearButton.setEnabled(true);
                stopButton.setEnabled(false);
            }
            case Notificacio.SELECCIONA -> {
                startButton.setEnabled(true);
                clearButton.setEnabled(true);
            }
            default -> {
            }
        }
    }
}
