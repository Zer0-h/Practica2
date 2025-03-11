package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import view.TrominoView;

public class TrominoController {

    private TrominoView view;
    private TrominoSolver solver;

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

            solver = new TrominoSolver(size, fixedX, fixedY, view.getBoardPanel(), view);
            view.setSolving(true);
            solver.start();
        }
    }

    class ClearButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            view.getBoardPanel().clearBoard();
            view.setSolving(false);
        }
    }

    class StopButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (solver != null) {
                solver.stopSolver();
                view.setSolving(false);
            }
        }
    }
}
