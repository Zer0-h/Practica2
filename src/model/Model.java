package model;

import java.awt.Color;

/**
 *
 * @author tonitorres
 */
public class Model {

    private final Integer[] midesSeleccionables;
    private int[][] tauler;
    private double constantTromino;
    private double tempsExecucio;
    private int numTromino;
    private int foratX;
    private int foratY;
    private boolean enExecucio;

    public Model() {
        constantTromino = 1.0;
        midesSeleccionables = new Integer[]{4, 8, 16, 32, 64, 128};
        enExecucio = false;
    }

    public Integer[] getMidesSeleccionables() {
        return midesSeleccionables;
    }

    public int getForatX() {
        return foratX;
    }

    public int getForatY() {
        return foratY;
    }

    public void assignarForat(int x, int y) {
        foratX = x;
        foratY = y;

        tauler[this.foratX][this.foratY] = -1;
    }

    public void netejarForat() {
        tauler[foratX][foratY] = 0;
    }

    public int getMidaTauler() {
        return tauler.length;
    }

    public boolean hiHaForatSeleccionat() {
        return foratX != -1 && foratY != -1;
    }

    public void setTempsExecucio(double value) {
        tempsExecucio = value;
    }

    public double getTempsExecucio() {
        return tempsExecucio;
    }

    public void calculaConstantTromino(double elapsedTime) {
        constantTromino = (elapsedTime * 1.0) / Math.pow(4, Math.log(tauler.length) / Math.log(2));
    }

    public double estimaTempsExecucio() {
        long estimatedCalls = (long) Math.pow(4, Math.log(tauler.length) / Math.log(2));
        return (constantTromino * estimatedCalls) / 1000.0;
    }

    public void inicialitzaTauler(int size) {
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

    public void setEnExecucio(boolean value) {
        enExecucio = value;
    }

    public boolean getEnExecucio() {
        return enExecucio;
    }

    public boolean esCasellaBuida(int x, int y) {
        return tauler[x][y] == 0;
    }

    public boolean esCasellaForat(int x, int y) {
        return tauler[x][y] == -1;
    }

    public boolean esCasellaTromino(int x, int y) {
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
        int trominoNum = tauler[x][y];

        if (trominoNum <= 0) {
            return Color.WHITE;
        }
        return new Color[]{
            Color.RED, Color.BLUE, Color.MAGENTA, Color.YELLOW, Color.GREEN,
            Color.ORANGE, Color.CYAN, Color.PINK
        }[(trominoNum - 1) % 8];
    }
}
