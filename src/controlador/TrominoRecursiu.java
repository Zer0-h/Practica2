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

    private boolean aturat = false;
    private final Practica2 principal;
    private final Model model;

    public TrominoRecursiu(Practica2 p) {
        this.principal = p;
        this.model = p.getModel();
    }

    @Override
    public void run() {
        aturat = false;

        long iniciTemps = System.currentTimeMillis();
        resoldreTromino(model.getMidaTauler(), 0, 0, model.getForatX(), model.getForatY());
        long tempsTotal = System.currentTimeMillis() - iniciTemps;

        model.calculaConstantTromino(tempsTotal);
        model.setTempsExecucio(tempsTotal / 1000.0);
        principal.notificar(Notificacio.FINALITZA);
    }

    private void resoldreTromino(int mida, int iniciX, int iniciY, int foratX, int foratY) {
        if (aturat) {
            return;
        }

        if (mida == 2) {
            emplenarTromino(iniciX, iniciY, mida);
            return;
        }

        int centreX = iniciX + mida / 2 - 1;
        int centreY = iniciY + mida / 2 - 1;

        boolean foratEsqSup = foratX < centreX + 1 && foratY < centreY + 1;
        boolean foratDretSup = foratX < centreX + 1 && foratY >= centreY + 1;
        boolean foratEsqInf = foratX >= centreX + 1 && foratY < centreY + 1;
        boolean foratDretInf = foratX >= centreX + 1 && foratY >= centreY + 1;

        if (!foratEsqSup) {
            model.colocaTromino(centreX, centreY);
        }
        if (!foratDretSup) {
            model.colocaTromino(centreX, centreY + 1);
        }
        if (!foratEsqInf) {
            model.colocaTromino(centreX + 1, centreY);
        }
        if (!foratDretInf) {
            model.colocaTromino(centreX + 1, centreY + 1);
        }

        model.incrementaTrominoActual();
        actualitzaVista();
        pausaExecucio();

        resoldreTromino(mida / 2, iniciX, iniciY, foratEsqSup ? foratX : centreX, foratEsqSup ? foratY : centreY);
        resoldreTromino(mida / 2, iniciX, iniciY + mida / 2, foratDretSup ? foratX : centreX, foratDretSup ? foratY : centreY + 1);
        resoldreTromino(mida / 2, iniciX + mida / 2, iniciY, foratEsqInf ? foratX : centreX + 1, foratEsqInf ? foratY : centreY);
        resoldreTromino(mida / 2, iniciX + mida / 2, iniciY + mida / 2, foratDretInf ? foratX : centreX + 1, foratDretInf ? foratY : centreY + 1);
    }

    private void emplenarTromino(int x, int y, int mida) {
        for (int i = 0; i < mida; i++) {
            for (int j = 0; j < mida; j++) {
                if (model.esCasellaBuida(x + i, y + j)) {
                    model.colocaTromino(x + i, y + j);
                }
            }
        }
        model.incrementaTrominoActual();
        actualitzaVista();
        pausaExecucio();
    }

    private void actualitzaVista() {
        principal.notificar(Notificacio.PINTAR);
    }

    private void pausaExecucio() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void atura() {
        aturat = true;
    }

    @Override
    public void notificar(Notificacio n) {
        if (n == Notificacio.ATURAR) {
            atura();
        }
    }
}
