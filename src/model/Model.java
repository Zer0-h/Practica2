package model;

import java.awt.Color;

/**
 *
 * @author tonitorres
 */
public class Model {

    private Integer[] midesSeleccionables;
    private int[][] tauler;
    private double constantTromino;
    private double tempsExecucio;
    private int numTromino;
    private int foratX;
    private int foratY;
    private boolean enProces;

    public Model() {
        constantTromino = 1.0;
        midesSeleccionables = new Integer[]{4, 8, 16, 32, 64, 128};
        enProces = false;
    }

    public Integer[] getMidesSeleccionables() {
        return midesSeleccionables;
    }

    public int getPosicioForatX() {
        return foratX;
    }

    public int getPosicioForatY() {
        return foratY;
    }

    public void colocaForat(int x, int y) {
        foratX = x;
        foratY = y;

        tauler[this.foratX][this.foratY] = -1;
    }

    public void llevaForatAnterior() {
        tauler[foratX][foratY] = 0;
    }

    public int tamanyTauler() {
        return tauler.length;
    }

    public boolean seleccionatEspaiBuit() {
        return foratX != -1 && foratY != -1;
    }

    public void setTempsExecucio(double value) {
        tempsExecucio = value;
    }

    public double getTempsExecucio() {
        return tempsExecucio;
    }

    public void setConstantTromino(double elapsedTime) {
        constantTromino = (elapsedTime * 1.0) / Math.pow(4, Math.log(tauler.length) / Math.log(2));
    }

    public double estimaTempsExecucio() {
        long estimatedCalls = (long) Math.pow(4, Math.log(tauler.length) / Math.log(2));
        return (constantTromino * estimatedCalls) / 1000.0;
    }

    public void construirTauler(int size) {
        this.tauler = new int[size][size];
        this.numTromino = 1;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tauler[i][j] = 0;
            }
        }

        foratX = -1;
        foratY = -1;
    }

    public void setEnProces(boolean value) {
        enProces = value;
    }

    public boolean getEnProces() {
        return enProces;
    }

    public boolean noTrominoColocat(int x, int y) {
        return tauler[x][y] == 0;
    }

    public boolean esEspaiAmbForat(int x, int y) {
        return tauler[x][y] == -1;
    }

    public boolean esEspaiAmbTromino(int x, int y) {
        return tauler[x][y] > 0;
    }

    public boolean esVoraSuperiorTromino(int x, int y) {
        return x == 0 || tauler[x - 1][y] != tauler[x][y];
    }

    public boolean esVoraInferiorTromino(int x, int y) {
        return x == tauler.length - 1 || tauler[x + 1][y] != tauler[x][y];
    }

    public boolean esVoraEsquerraTromino(int x, int y) {
        return y == 0 || tauler[x][y - 1] != tauler[x][y];
    }

    public boolean esVoraDretaTromino(int x, int y) {
        return y == tauler[x].length - 1 || tauler[x][y + 1] != tauler[x][y];
    }

    public void colocaTromino(int x, int y) {
        tauler[x][y] = numTromino;
    }

    public void incrementaTrominoActual() {
        numTromino++;
    }

    public Color getColorPerTromino(int x, int y) {
        int trominoNumber = tauler[x][y];

        if (trominoNumber <= 0) {
            return Color.WHITE;
        }
        return new Color[]{
            Color.RED, Color.BLUE, Color.MAGENTA, Color.YELLOW, Color.GREEN,
            Color.ORANGE, Color.CYAN, Color.PINK
        }[(trominoNumber - 1) % 8];
    }
}
