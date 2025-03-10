package view;

import java.awt.*;
import javax.swing.*;

public class TrominoView extends JFrame {

    private JButton startButton, clearButton, sizeButton;
    private JComboBox<Integer> sizeSelector;
    private JLabel statusLabel;
    private TrominoPanel boardPanel;

    public TrominoView() {
        setTitle("Tromino Tiling");
        setSize(700, 800);
        setLayout(new BorderLayout());

        // Control Panel
        JPanel controlPanel = new JPanel();
        sizeSelector = new JComboBox<>(new Integer[]{4, 8, 16, 32, 64, 128});
        startButton = new JButton("Solve");
        clearButton = new JButton("Clear");
        sizeButton = new JButton("Size");
        statusLabel = new JLabel("Select Board Size & Click to Set Fixed Tile");

        controlPanel.add(new JLabel("Board Size:"));
        controlPanel.add(sizeSelector);
        controlPanel.add(startButton);
        controlPanel.add(clearButton);
        controlPanel.add(sizeButton);
        controlPanel.add(statusLabel);

        add(controlPanel, BorderLayout.NORTH);

        // Board Panel
        boardPanel = new TrominoPanel(4);
        add(boardPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

    public JComboBox<Integer> getSizeSelector() {
        return sizeSelector;
    }

    public TrominoPanel getBoardPanel() {
        return boardPanel;
    }
}
