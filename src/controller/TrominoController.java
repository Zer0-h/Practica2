package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.TrominoModel;
import view.TrominoView;

public class TrominoController {

    private TrominoView view;
    private TrominoModel model;

    public TrominoController(TrominoView view) {
        this.view = view;
        view.getStartButton().addActionListener(new StartButtonListener());
        view.getClearButton().addActionListener(new ClearButtonListener());
        view.getStopButton().addActionListener(new StopButtonListener());
    }

    class StartButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            int size = (int) view.getSizeSelector().getSelectedItem();
            int fixedX = view.getBoardPanel().getFixedX();
            int fixedY = view.getBoardPanel().getFixedY();

            if (fixedX == -1 || fixedY == -1) {
                System.out.println("Please select a missing tile before solving.");
                return;
            }

            model = new TrominoModel(size, fixedX, fixedY, view.getBoardPanel());
            view.setSolving(true); // Disable size selector
            new Thread(() -> {
                model.solveTromino();
                view.setSolving(false); // Re-enable selector after solving
            }).start();
        }
    }

    class ClearButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            view.getBoardPanel().clearBoard();
            view.setSolving(false); // Ensure selector is enabled after clearing
        }
    }

    class StopButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (model != null) {
                model.stopTromino();
                view.setSolving(false); // Re-enable selector when stopping
            }
        }
    }
}
