package vista;

import java.awt.*;
import javax.swing.*;
import main.Practica2;
import model.Notificacio;
import model.Notificar;
import model.Model;

/**
 *
 * @author tonitorres
 */
public class Vista extends JFrame implements Notificar {

    private static final Integer[] BOARD_SIZES = {4, 8, 16, 32, 64, 128};

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
        sizeSelector = new JComboBox<>(BOARD_SIZES);
        startButton = new JButton("Solve");
        clearButton = new JButton("Clear");
        stopButton = new JButton("Stop");
        stopButton.setEnabled(false);

        controlPanel.add(new JLabel("Board Size:"));
        controlPanel.add(sizeSelector);
        controlPanel.add(startButton);
        controlPanel.add(clearButton);
        controlPanel.add(stopButton);

        add(controlPanel, BorderLayout.NORTH);

        boardPanel = new TaulerPanel(BOARD_SIZES[0], principal);
        add(boardPanel, BorderLayout.CENTER);

        // **New Time Display Panel**
        JPanel timePanel = new JPanel();
        timeLabel = new JLabel("Estimated Time: -- s | Actual Time: -- s"); // Default text
        timePanel.add(timeLabel);
        add(timePanel, BorderLayout.SOUTH); // Add to bottom of the window

        // Listener to update board when size changes
        sizeSelector.addActionListener(e -> {
            if (!isSolving()) { // Prevent changing size while solving
                int newSize = (int) sizeSelector.getSelectedItem();
                boardPanel.updateBoard(new int[newSize][newSize]);
                boardPanel.clearBoard();
            }
        });

        startButton.addActionListener(e -> {
            principal.notificar(Notificacio.ARRANCAR);
            setSolving(true);
            Model model = principal.getModel();
            setEstimatedTime(model.estimateTrominoExecutionTime());
        });

        clearButton.addActionListener(e -> {
            getBoardPanel().clearBoard();
            setSolving(false);
        });

        stopButton.addActionListener(e -> {
            principal.notificar(Notificacio.ATURAR);
            setSolving(false);
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

    public JComboBox<Integer> getSizeSelector() {
        return sizeSelector;
    }

    public TaulerPanel getBoardPanel() {
        return boardPanel;
    }

    public void setSolving(boolean isSolving) {
        sizeSelector.setEnabled(!isSolving);
        startButton.setEnabled(!isSolving);
        clearButton.setEnabled(!isSolving);
        stopButton.setEnabled(isSolving);
    }

    public boolean isSolving() {
        return !sizeSelector.isEnabled(); // If disabled, solving is in progress
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
                Model model = principal.getModel();
                this.boardPanel.updateBoard(model.getBoard());
            }
            case Notificacio.FINALITZA -> {
                Model model = principal.getModel();

                setActualTime(model.getLastExecutionTime());
                setSolving(false);
            }

            default -> {
            }
        }
    }
}
