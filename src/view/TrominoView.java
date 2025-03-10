package view;

import java.awt.*;
import javax.swing.*;

public class TrominoView extends JFrame {

    private JButton startButton;
    private JComboBox<Integer> sizeSelector;
    private JLabel statusLabel;
    private TrominoPanel boardPanel;

    public TrominoView() {
        setTitle("Tromino Tiling");
        setSize(600, 700);
        setLayout(new BorderLayout());

        // Control Panel
        JPanel controlPanel = new JPanel();
        sizeSelector = new JComboBox<>(new Integer[]{2, 4, 8, 16, 32, 64, 128});
        startButton = new JButton("Start");
        statusLabel = new JLabel("Select Board Size");

        controlPanel.add(new JLabel("Board Size:"));
        controlPanel.add(sizeSelector);
        controlPanel.add(startButton);
        controlPanel.add(statusLabel);

        add(controlPanel, BorderLayout.NORTH);

        // Board Panel
        boardPanel = new TrominoPanel(8);
        add(boardPanel, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public JButton getStartButton() {
        return startButton;
    }

    public JComboBox<Integer> getSizeSelector() {
        return sizeSelector;
    }

    public TrominoPanel getBoardPanel() {
        return boardPanel;
    }
}
