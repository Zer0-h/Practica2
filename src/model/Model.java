package model;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Classe Model que representa el tauler i la lògica del problema de Tromino.
 * S'encarrega de gestionar l'estat del tauler i proporcionar mètodes per
 * manipular-lo.
 *
 * @author tonitorres
 */
public class Model {

    // Mides disponibles per al tauler
    private final Integer[] midesSeleccionables;

    // Matriu que representa el tauler de joc
    private int[][] tauler;

    // Paràmetres relacionats amb el temps d'execució
    private double constantTromino;
    private double tempsExecucio;

    // Comptador de trominos col·locats
    private AtomicInteger numTromino;

    // Coordenades del forat inicial
    private int foratX;
    private int foratY;

    // Estat de l'execució
    private boolean enExecucio;
    private boolean resolt;

    private Color trominoColor;
    private final Map<Integer, Color> trominoColors;

    /**
     * Constructor de la classe Model. Inicialitza les mides del tauler
     * disponibles i estableix valors per defecte.
     */
    public Model() {
        constantTromino = 1.0;
        midesSeleccionables = new Integer[]{4, 8, 16, 32, 64, 128};
        enExecucio = false;
        resolt = false;
        trominoColor = Color.WHITE;
        trominoColors = new HashMap<>();
    }

    /**
     * =======================
     * GETTERS
     * =======================
     */
    public Integer[] getMidesSeleccionables() {
        return midesSeleccionables;
    }

    public int getForatX() {
        return foratX;
    }

    public int getForatY() {
        return foratY;
    }

    public int getMidaTauler() {
        return tauler.length;
    }

    public double getTempsExecucio() {
        return tempsExecucio;
    }

    public boolean getEnExecucio() {
        return enExecucio;
    }

    public boolean getResolt() {
        return resolt;
    }

    /**
     * =======================
     * SETTERS
     * =======================
     */
    public void setTempsExecucio(double value) {
        tempsExecucio = value;
    }

    public void setEnExecucio(boolean value) {
        enExecucio = value;
    }

    public void setResolt(boolean value) {
        resolt = value;
    }

    /**
     * Calcula la constant multiplicativa del procés del tromino per estimar el
     * temps d'execució basant-se en el temps transcorregut.
     *
     * @param elapsedTime Temps total d'execució en nanosegons
     */
    public void calculaConstantTromino(double elapsedTime) {
        constantTromino = (elapsedTime * 1.0) / Math.pow(tauler.length, 2);
    }

    /**
     * Estima el temps d'execució del procés total per a la mida actual del
     * tauler.
     *
     * @return Temps estimat en segons
     */
    public double estimaTempsExecucio() {
        return (constantTromino * Math.pow(tauler.length, 2)) / 1_000_000_000.0; // Convertim de nanosegons a segons
    }

    /**
     * ==============================
     * GESTIÓ DEL TAULER
     * ==============================
     */
    /**
     * Inicialitza el tauler amb la mida especificada i reinicia els valors
     * necessaris.
     *
     * @param size Mida del tauler
     */
    public void inicialitzaTauler(int size) {
        tauler = new int[size][size];
        numTromino = new AtomicInteger(1);
        resolt = false;

        // Inicialitza totes les cel·les a 0 (buides)
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tauler[i][j] = 0;
            }
        }

        // Inicialitza la posició del forat com no col·locat
        foratX = -1;
        foratY = -1;
    }

    /**
     * Assigna el forat inicial al tauler.
     *
     * @param x Coordenada X del forat
     * @param y Coordenada Y del forat
     */
    public void assignarForat(int x, int y) {
        foratX = x;
        foratY = y;
        tauler[foratX][foratY] = -1;
    }

    /**
     * Elimina el forat actual del tauler.
     */
    public void netejarForat() {
        tauler[foratX][foratY] = 0;
    }

    /**
     * Comprova si hi ha un forat seleccionat.
     *
     * @return Cert si hi ha un forat seleccionat, fals en cas contrari
     */
    public boolean hiHaForatSeleccionat() {
        return foratX != -1 && foratY != -1;
    }

    /*
     * ==============================
     * GESTIÓ DELS TROMINOS
     * ==============================
     */
    /**
     * Comprova si una casella està buida.
     *
     * @param x Coordenada X
     * @param y Coordenada Y
     *
     * @return Cert si la casella està buida, fals en cas contrari
     */
    public boolean esCasellaBuida(int x, int y) {
        return tauler[x][y] == 0;
    }

    /**
     * Comprova si una casella conté un forat.
     *
     * @param x Coordenada X
     * @param y Coordenada Y
     *
     * @return Cert si la casella conté un forat, fals en cas contrari
     */
    public boolean esCasellaForat(int x, int y) {
        return tauler[x][y] == -1;
    }

    /**
     * Comprova si una casella conté un tromino.
     *
     * @param x Coordenada X
     * @param y Coordenada Y
     *
     * @return Cert si la casella conté un tromino, fals en cas contrari
     */
    public boolean esCasellaTromino(int x, int y) {
        return tauler[x][y] > 0;
    }

    /**
     * Col·loca un tromino en una casella del tauler.
     *
     * @param x Coordenada X
     * @param y Coordenada Y
     * @param trominoNumber Numero del tromino
     */
    public synchronized void colocaTromino(int x, int y, int trominoNumber) {
        if (tauler[x][y] == 0) {
            tauler[x][y] = trominoNumber;

            trominoColors.put(trominoNumber, trominoColor);
        }
    }

    /**
     * Incrementa el número de trominos col·locats.
     */
    public synchronized int incrementaIOBtenirTrominoActual() {
        return numTromino.incrementAndGet();
    }

    /**
     * ==============================
     * DETECCIÓ DE BORDES
     * ==============================
     */
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

    public int[][] test() {
        return tauler;
    }

    public void setTrominoColor(Color color) {
        trominoColor = color;
    }

    public Color getColorPerTromino(int x, int y) {
        int trominoNum = tauler[x][y];
        return trominoColors.getOrDefault(trominoNum, Color.WHITE);
    }
}
