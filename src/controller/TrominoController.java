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
    }

    class StartButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            int size = (int) view.getSizeSelector().getSelectedItem();
            model = new TrominoModel(size, size / 2, size / 2); // Default fixed tile at center
            model.solveTromino();
            view.getBoardPanel().updateBoard(model.getBoard());
        }
    }
}
