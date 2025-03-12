package main;

import controller.TrominoSolver;
import model.Notificacio;
import model.Notificar;
import model.TrominoModel;
import view.TrominoView;

/**
 *
 * @author tonitorres
 */
public class TrominoMain implements Notificar {

    private TrominoView vista;
    private TrominoModel model;
    private TrominoSolver solver;

    public static void main(String[] args) {
        (new TrominoMain()).inicio();

    }

    public void inicio() {
        model = new TrominoModel();
        vista = new TrominoView(this);
    }

    public TrominoModel getModel() {
        return model;
    }

    @Override
    public void notificar(Notificacio n) {
        switch (n) {
            case Notificacio.ARRANCAR -> {
                int size = (int) vista.getSizeSelector().getSelectedItem();
                int fixedX = vista.getBoardPanel().getFixedX();
                int fixedY = vista.getBoardPanel().getFixedY();

                model.preparar(size, fixedX, fixedY);

                if (fixedX == -1 || fixedY == -1) {
                    System.out.println("Please select a missing tile before solving.");
                    return;
                }

                solver = new TrominoSolver(this);
                vista.setSolving(true);
                solver.start();
            }
            case Notificacio.ATURAR -> {
                solver.stopSolver();
            }
            case Notificacio.PINTAR -> {
                vista.notificar(n);
            }
            case Notificacio.FINALITZA -> {
                vista.notificar(n);
            }
            default -> {
            }
        }
    }
}
