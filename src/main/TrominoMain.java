package main;

import controller.TrominoController;
import javax.swing.*;
import view.TrominoView;

public class TrominoMain {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TrominoView view = new TrominoView();
            new TrominoController(view);
        });
    }
}
