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
        new Practica2().iniciar();
    }

    public void iniciar() {
        model = new Model();
        model.inicialitzaTauler(model.getMidesSeleccionables()[0]); // Comença amb la mida mínima
        vista = new Vista(this);
    }

    public Model getModel() {
        return model;
    }

    @Override
    public void notificar(Notificacio n) {
        switch (n) {
            case Notificacio.ARRANCAR ->
                iniciarResolucio();
            case Notificacio.ATURAR ->
                aturarResolucio();
            case Notificacio.PINTAR, Notificacio.SELECCIONA, Notificacio.FINALITZA ->
                vista.notificar(n);
        }
    }

    private void iniciarResolucio() {
        model.setEnExecucio(true);
        solver = new TrominoRecursiu(this);
        solver.start();
    }

    private void aturarResolucio() {
        model.setEnExecucio(false);
        solver.atura();
    }
}
