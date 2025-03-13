package controlador;

import main.Practica2;
import model.Model;
import model.Notificacio;
import model.Notificar;

/**
 *
 * @author tonitorres
 */
public class TrominoRecursiu extends Thread implements Notificar {

    private boolean aturar = false;
    private final Practica2 principal;
    private final Model model;

    public TrominoRecursiu(Practica2 p) {
        principal = p;
        model = p.getModel();
    }

    @Override
    public void run() {
        aturar = false;

        long inici = System.currentTimeMillis();

        colocaTromino(model.tamanyTauler(), 0, 0, model.getPosicioForatX(), model.getPosicioForatY());

        long tempsTotal = System.currentTimeMillis() - inici;
        model.setConstantTromino(tempsTotal);

        model.setTempsExecucio(tempsTotal / 1000.0);
        principal.notificar(Notificacio.FINALITZA);
    }

    private void colocaTromino(int mida, int iniciX, int iniciY, int foratX, int foratY) {
        if (aturar) {
            return;
        }

        if (mida == 2) {
            for (int i = 0; i < mida; i++) {
                for (int j = 0; j < mida; j++) {
                    if (model.noTrominoColocat(iniciX + i, iniciY + j)) {
                        model.colocaTromino(iniciX + i, iniciY + j);
                    }
                }
            }
            model.incrementaTrominoActual();
            actualitzaVista();
            sleep();
            return;
        }

        int centreX = iniciX + mida / 2 - 1;
        int centreY = iniciY + mida / 2 - 1;

        boolean adaltEsquerra = foratX < iniciX + mida / 2 && foratY < iniciY + mida / 2;
        boolean adaltDreta = foratX < iniciX + mida / 2 && foratY >= iniciY + mida / 2;
        boolean abaixEsquerra = foratX >= iniciX + mida / 2 && foratY < iniciY + mida / 2;
        boolean abaixDreta = foratX >= iniciX + mida / 2 && foratY >= iniciY + mida / 2;

        if (!adaltEsquerra) {
            model.colocaTromino(centreX, centreY);
        }
        if (!adaltDreta) {
            model.colocaTromino(centreX, centreY + 1);
        }
        if (!abaixEsquerra) {
            model.colocaTromino(centreX + 1, centreY);
        }
        if (!abaixDreta) {
            model.colocaTromino(centreX + 1, centreY + 1);
        }

        model.incrementaTrominoActual();
        actualitzaVista();
        sleep();

        colocaTromino(mida / 2, iniciX, iniciY, adaltEsquerra ? foratX : centreX, adaltEsquerra ? foratY : centreY);
        colocaTromino(mida / 2, iniciX, iniciY + mida / 2, adaltDreta ? foratX : centreX, adaltDreta ? foratY : centreY + 1);
        colocaTromino(mida / 2, iniciX + mida / 2, iniciY, abaixEsquerra ? foratX : centreX + 1, abaixEsquerra ? foratY : centreY);
        colocaTromino(mida / 2, iniciX + mida / 2, iniciY + mida / 2, abaixDreta ? foratX : centreX + 1, abaixDreta ? foratY : centreY + 1);
    }

    public void atura() {
        aturar = true;
    }

    private void actualitzaVista() {
        principal.notificar(Notificacio.PINTAR);
    }

    private void sleep() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void notificar(Notificacio n) {
        switch (n) {
            case Notificacio.ATURAR -> {
                atura();
            }

        }
    }
}
