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
        view.getSizeButton().addActionListener(new SizeButtonListener());
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
            new Thread(() -> model.solveTromino()).start(); // Run in a separate thread
        }
    }

    class ClearButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            view.getBoardPanel().clearBoard();
        }
    }

    class SizeButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            view.getBoardPanel().updateBoard(new int[(int) view.getSizeSelector().getSelectedItem()][(int) view.getSizeSelector().getSelectedItem()]);
            view.getBoardPanel().clearBoard();
        }
    }
}
