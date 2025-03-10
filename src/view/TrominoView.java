package view;

import java.awt.*;
import javax.swing.*;

public class TrominoView extends JFrame {

    private JButton startButton, clearButton, sizeButton, stopButton;
    private JComboBox<Integer> sizeSelector;
    private TrominoPanel boardPanel;

    public TrominoView() {
        setTitle("Tromino Tiling");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        sizeSelector = new JComboBox<>(new Integer[]{4, 8, 16, 32, 64, 128});
        startButton = new JButton("Solve");
        clearButton = new JButton("Clear");
        sizeButton = new JButton("Size");
        stopButton = new JButton("Stop"); // NEW Stop Button

        controlPanel.add(new JLabel("Board Size:"));
        controlPanel.add(sizeSelector);
        controlPanel.add(startButton);
        controlPanel.add(clearButton);
        controlPanel.add(sizeButton);
        controlPanel.add(stopButton); // Add Stop Button to UI

        add(controlPanel, BorderLayout.NORTH);

        boardPanel = new TrominoPanel(4);
        add(boardPanel, BorderLayout.CENTER);

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

    public JButton getSizeButton() {
        return sizeButton;
    }

    public JButton getStopButton() { // Getter for Stop Button
        return stopButton;
    }

    public JComboBox<Integer> getSizeSelector() {
        return sizeSelector;
    }

    public TrominoPanel getBoardPanel() {
        return boardPanel;
    }
}
