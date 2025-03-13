package main;

import controlador.TrominoRecursiu;
import model.Notificacio;
import model.Notificar;
import model.Model;
import vista.Vista;

/**
 *
 * @author tonitorres
 */
public class Practica2 implements Notificar {

    private Vista vista;
    private Model model;
    private TrominoRecursiu solver;

    public static void main(String[] args) {
        (new Practica2()).inicio();

    }

    public void inicio() {
        model = new Model();
        vista = new Vista(this);
    }

    public Model getModel() {
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

                solver = new TrominoRecursiu(this);
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
