package main;

import controlador.TrominoRecursiu;
import model.Model;
import model.Notificacio;
import model.Notificar;
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
        (new Practica2()).inici();

    }

    public void inici() {
        model = new Model();
        model.construirTauler(model.getMidesSeleccionables()[0]);
        vista = new Vista(this);
    }

    public Model getModel() {
        return model;
    }

    @Override
    public void notificar(Notificacio n) {
        switch (n) {
            case Notificacio.ARRANCAR -> {
                model.setEnProces(true);
                solver = new TrominoRecursiu(this);
                solver.start();
            }
            case Notificacio.ATURAR -> {
                model.setEnProces(false);
                solver.atura();
            }
            case Notificacio.PINTAR -> {
                vista.notificar(n);
            }
            case Notificacio.FINALITZA -> {
                model.setEnProces(false);
                vista.notificar(n);
            }
            case Notificacio.SELECCIONA -> {
                vista.notificar(n);
            }
            default -> {
            }
        }
    }
}
