package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TrominoView extends JFrame {

    private static final Integer[] BOARD_SIZES = {4, 8, 16, 32, 64, 128};

    private JButton startButton, clearButton, stopButton;
    private JComboBox<Integer> sizeSelector;
    private JSlider speedSlider;
    private JLabel speedLabel;
    private TrominoPanel boardPanel;

    public TrominoView() {
        setTitle("Tromino Tiling");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Control Panel
        JPanel controlPanel = new JPanel();
        sizeSelector = new JComboBox<>(BOARD_SIZES);
        startButton = new JButton("Solve");
        clearButton = new JButton("Clear");
        stopButton = new JButton("Stop");

        speedSlider = new JSlider(0, 50, 5); // Min: 0s, Max: 1s, Default: 0.1s
        speedSlider.setMajorTickSpacing(25);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(false);
        speedLabel = new JLabel("Speed: 0.05s");

        // Update speed label dynamically
        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double speed = speedSlider.getValue() / 100.0;
                speedLabel.setText("Speed: " + String.format("%.2f", speed) + "s");
            }
        });

        controlPanel.add(new JLabel("Board Size:"));
        controlPanel.add(sizeSelector);
        controlPanel.add(startButton);
        controlPanel.add(clearButton);
        controlPanel.add(stopButton);
        controlPanel.add(speedLabel);
        controlPanel.add(speedSlider);

        add(controlPanel, BorderLayout.NORTH);

        boardPanel = new TrominoPanel(BOARD_SIZES[0]);
        add(boardPanel, BorderLayout.CENTER);

        // Listener to update board when size changes
        sizeSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isSolving()) { // Prevent changing size while solving
                    int newSize = (int) sizeSelector.getSelectedItem();
                    boardPanel.updateBoard(new int[newSize][newSize]);
                    boardPanel.clearBoard();
                }
            }
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

    public TrominoPanel getBoardPanel() {
        return boardPanel;
    }

    public double getSelectedSpeed() {
        return speedSlider.getValue() / 100.0; // Convert to seconds
    }

    public void setSolving(boolean isSolving) {
        sizeSelector.setEnabled(!isSolving);
        startButton.setEnabled(!isSolving);
    }

    public boolean isSolving() {
        return !sizeSelector.isEnabled(); // If disabled, it means solving is in progress
    }
}
