package model;

import controlador.Controlador;
import java.util.ArrayList;
import java.util.concurrent.*;
import model.Model;
import controlador.Notificacio;
import controlador.Notificar;

/**
 * Classe encarregada de resoldre el problema del Tromino de manera recursiva.
 * Implementa un procés en un fil independent i utilitza el patró
 * d'esdeveniments per comunicar-se amb la resta del programa.
 *
 * @author tonitorres
 */
public class TrominoRecursiu extends Thread implements Notificar {

    private boolean aturat; // Indica si l'execució ha de ser aturada
    private final Controlador controlador;
    private final Model model;
    private static ExecutorService executor;

    /**
     * Constructor que inicialitza la instància del controlador.
     *
     * @param c Instància de la classe principal Practica2
     */
    public TrominoRecursiu(Controlador c) {
        this.controlador = c;
        this.model = c.getModel();
    }

    /**
     * Mètode principal d'execució del fil. Crida el procés de resolució del
     * problema del Tromino i mesura el temps d'execució.
     */
    @Override
    public void run() {
        aturat = false;
        executor = Executors.newFixedThreadPool(4);

        // Captura el temps d'inici en nanosegons per a més precisió
        long iniciTemps = System.nanoTime();

        // Inicia la resolució del problema
        resoldreTrominoMultithreading(model.getMidaTauler(), 0, 0, model.getForatX(), model.getForatY());
        executor.shutdown();

        // Calcula el temps total d'execució en segons
        long tempsTotal = System.nanoTime() - iniciTemps;
        model.calculaConstantTromino(tempsTotal);
        model.setTempsExecucio(tempsTotal / 1000000000.0);

        // Notifica a la resta del sistema que el càlcul ha finalitzat
        controlador.notificar(Notificacio.FINALITZA);
    }

    public void resoldreTrominoMultithreading(int mida, int iniciX, int iniciY, int foratX, int foratY) {
        if (aturat) {
            return;
        }

        int numTromino = model.incrementaIOBtenirTrominoActual();

        // Cas base: si la mida és 2x2, emplenar directament
        if (mida == 2) {
            colocaTrominoBase(iniciX, iniciY, numTromino);
            return;
        }

        int centreX = iniciX + mida / 2 - 1;
        int centreY = iniciY + mida / 2 - 1;

        boolean foratEsqSup = foratX < centreX + 1 && foratY < centreY + 1;
        boolean foratDretSup = foratX < centreX + 1 && foratY >= centreY + 1;
        boolean foratEsqInf = foratX >= centreX + 1 && foratY < centreY + 1;
        boolean foratDretInf = foratX >= centreX + 1 && foratY >= centreY + 1;

        if (!foratEsqSup) {
            model.colocaTromino(centreX, centreY, numTromino);
        }
        if (!foratDretSup) {
            model.colocaTromino(centreX, centreY + 1, numTromino);
        }
        if (!foratEsqInf) {
            model.colocaTromino(centreX + 1, centreY, numTromino);
        }
        if (!foratDretInf) {
            model.colocaTromino(centreX + 1, centreY + 1, numTromino);
        }

        actualitzaVista();
        pausaExecucio();

        ExecutorService executor = Executors.newFixedThreadPool(4);

        try {
            ArrayList<Future<?>> futures = new ArrayList<>();
            futures.add(executor.submit(() -> resoldreTromino(mida / 2, iniciX, iniciY, foratEsqSup ? foratX : centreX, foratEsqSup ? foratY : centreY)));
            futures.add(executor.submit(() -> resoldreTromino(mida / 2, iniciX, iniciY + mida / 2, foratDretSup ? foratX : centreX, foratDretSup ? foratY : centreY + 1)));
            futures.add(executor.submit(() -> resoldreTromino(mida / 2, iniciX + mida / 2, iniciY, foratEsqInf ? foratX : centreX + 1, foratEsqInf ? foratY : centreY)));
            futures.add(executor.submit(() -> resoldreTromino(mida / 2, iniciX + mida / 2, iniciY + mida / 2, foratDretInf ? foratX : centreX + 1, foratDretInf ? foratY : centreY + 1)));

            for (Future<?> future : futures) {
                future.get(); // Wait for all threads to complete
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    /**
     * Mètode recursiu per col·locar els Trominos.
     *
     * @param mida Mida actual de la submatriu
     * @param iniciX Coordenada X superior esquerra de la submatriu
     * @param iniciY Coordenada Y superior esquerra de la submatriu
     * @param foratX Coordenada X del forat dins la submatriu
     * @param foratY Coordenada Y del forat dins la submatriu
     */
    private void resoldreTromino(int mida, int iniciX, int iniciY, int foratX, int foratY) {
        if (aturat) {
            return;
        }

        int numTromino = model.incrementaIOBtenirTrominoActual();

        // Cas base: si la mida és 2x2, emplenar directament
        if (mida == 2) {
            colocaTrominoBase(iniciX, iniciY, numTromino);
            return;
        }

        // Trobar el centre de la submatriu
        int centreX = iniciX + mida / 2 - 1;
        int centreY = iniciY + mida / 2 - 1;

        // Determinar en quin quadrant es troba el forat
        boolean foratEsqSup = foratX < centreX + 1 && foratY < centreY + 1;
        boolean foratDretSup = foratX < centreX + 1 && foratY >= centreY + 1;
        boolean foratEsqInf = foratX >= centreX + 1 && foratY < centreY + 1;
        boolean foratDretInf = foratX >= centreX + 1 && foratY >= centreY + 1;

        // Col·locar un tromino al centre cobrint els tres quadrants restants
        if (!foratEsqSup) {
            model.colocaTromino(centreX, centreY, numTromino);
        }
        if (!foratDretSup) {
            model.colocaTromino(centreX, centreY + 1, numTromino);
        }
        if (!foratEsqInf) {
            model.colocaTromino(centreX + 1, centreY, numTromino);
        }
        if (!foratDretInf) {
            model.colocaTromino(centreX + 1, centreY + 1, numTromino);
        }

        // Actualitzar la vista i fer una petita pausa per visualització
        actualitzaVista();
        pausaExecucio();

        // Aplicar la recursió a cada quadrant
        resoldreTromino(mida / 2, iniciX, iniciY, foratEsqSup ? foratX : centreX, foratEsqSup ? foratY : centreY);
        resoldreTromino(mida / 2, iniciX, iniciY + mida / 2, foratDretSup ? foratX : centreX, foratDretSup ? foratY : centreY + 1);
        resoldreTromino(mida / 2, iniciX + mida / 2, iniciY, foratEsqInf ? foratX : centreX + 1, foratEsqInf ? foratY : centreY);
        resoldreTromino(mida / 2, iniciX + mida / 2, iniciY + mida / 2, foratDretInf ? foratX : centreX + 1, foratDretInf ? foratY : centreY + 1);
    }

    /**
     * Notifica la vista perquè es refresqui el tauler.
     */
    private void actualitzaVista() {
        controlador.notificar(Notificacio.PINTAR);
    }

    /**
     * Fa una pausa curta per evitar que el procés sigui massa ràpid per
     * visualització.
     */
    private void pausaExecucio() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Atura l'execució de la resolució del Tromino.
     */
    public void atura() {
        aturat = true;
    }


    /**
     * Coloca un tromino al cas base 2x2.
     */
    private void colocaTrominoBase(int x, int y, int numTromino) {
        if (model.esCasellaBuida(x, y)) {
            model.colocaTromino(x, y, numTromino);
        }
        if (model.esCasellaBuida(x, y + 1)) {
            model.colocaTromino(x, y + 1, numTromino);
        }
        if (model.esCasellaBuida(x + 1, y)) {
            model.colocaTromino(x + 1, y, numTromino);
        }
        if (model.esCasellaBuida(x + 1, y + 1)) {
            model.colocaTromino(x + 1, y + 1, numTromino);
        }

        actualitzaVista();
        pausaExecucio();
    }

    /**
     * Implementació del patró d'esdeveniments: escolta notificacions externes.
     *
     * @param n Tipus de notificació rebuda
     */
    @Override
    public void notificar(Notificacio n) {
        if (n == Notificacio.ATURAR) {
            atura();
        }
    }
}
