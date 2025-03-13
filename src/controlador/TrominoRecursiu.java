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

    private volatile boolean aturar = false;
    private final Practica2 principal;
    private final Model model;

    public TrominoRecursiu(Practica2 p) {
        principal = p;
        model = p.getModel();
    }

    @Override
    public void run() {
        aturar = false;

        long startTime = System.currentTimeMillis();

        tileRec(model.getBoardSize(), 0, 0, model.getInitialEmptySquareX(), model.getInitialEmptySquareY());

        long elapsedTime = System.currentTimeMillis() - startTime;
        model.setTrominoConstant(elapsedTime);

        model.setLastExecutionTime(elapsedTime / 1000.0);
        principal.notificar(Notificacio.FINALITZA);
    }

    private void tileRec(int size, int topX, int topY, int holeX, int holeY) {
        if (aturar) {
            return;
        }

        if (size == 2) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (model.isBoardSpaceEmpty(topX + i, topY + j)) {
                        model.setTrominoToBoard(topX + i, topY + j);
                    }
                }
            }
            model.increaseCurrentTromino();
            actualitzaVista();
            sleep();
            return;
        }

        int centerX = topX + size / 2 - 1;
        int centerY = topY + size / 2 - 1;

        boolean inUpperLeft = holeX < topX + size / 2 && holeY < topY + size / 2;
        boolean inUpperRight = holeX < topX + size / 2 && holeY >= topY + size / 2;
        boolean inBottomLeft = holeX >= topX + size / 2 && holeY < topY + size / 2;
        boolean inBottomRight = holeX >= topX + size / 2 && holeY >= topY + size / 2;

        if (!inUpperLeft) {
            model.setTrominoToBoard(centerX, centerY);
        }
        if (!inUpperRight) {
            model.setTrominoToBoard(centerX, centerY + 1);
        }
        if (!inBottomLeft) {
            model.setTrominoToBoard(centerX + 1, centerY);
        }
        if (!inBottomRight) {
            model.setTrominoToBoard(centerX + 1, centerY + 1);
        }

        model.increaseCurrentTromino();
        actualitzaVista();
        sleep();

        tileRec(size / 2, topX, topY, inUpperLeft ? holeX : centerX, inUpperLeft ? holeY : centerY);
        tileRec(size / 2, topX, topY + size / 2, inUpperRight ? holeX : centerX, inUpperRight ? holeY : centerY + 1);
        tileRec(size / 2, topX + size / 2, topY, inBottomLeft ? holeX : centerX + 1, inBottomLeft ? holeY : centerY);
        tileRec(size / 2, topX + size / 2, topY + size / 2, inBottomRight ? holeX : centerX + 1, inBottomRight ? holeY : centerY + 1);
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
