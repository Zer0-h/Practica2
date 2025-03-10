package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class TrominoView extends JFrame {

    private static final Integer[] BOARD_SIZES = {4, 8, 16, 32, 64, 128};

    private JButton startButton, clearButton, stopButton;
    private JComboBox<Integer> sizeSelector;
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
        stopButton.setEnabled(false);

        controlPanel.add(new JLabel("Board Size:"));
        controlPanel.add(sizeSelector);
        controlPanel.add(startButton);
        controlPanel.add(clearButton);
        controlPanel.add(stopButton);

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

    public void setSolving(boolean isSolving) {
        sizeSelector.setEnabled(!isSolving);
        startButton.setEnabled(!isSolving);
        clearButton.setEnabled(!isSolving);
        stopButton.setEnabled(isSolving);
    }

    public boolean isSolving() {
        return !sizeSelector.isEnabled(); // If disabled, it means solving is in progress
    }
}
