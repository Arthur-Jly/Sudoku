package Class;
import Class.Deduction;

import Class.Backtracking;

/**
 * Classe implémentant une combinaison de déduction et de backtracking pour résoudre les grilles de Sudoku.
 */
public class ResolveurCombine {
    private final int taille;
    private final int tailleBloc;
    private int[][] grille;
    private final int[][] grilleOriginale;
    private final Deduction deduction;
    private final Backtracking backtracking;

    /**
     * Constructeur de la classe ResolveurCombine.
     *
     * @param grilleInitiale La grille initiale à résoudre.
     */
    public ResolveurCombine(int[][] grilleInitiale) {
        this.taille = grilleInitiale.length;
        this.tailleBloc = (int) Math.sqrt(taille);
        this.grilleOriginale = new int[taille][taille];
        this.grille = new int[taille][taille];
        
        // Copie de la grille initiale
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                this.grilleOriginale[i][j] = grilleInitiale[i][j];
                this.grille[i][j] = grilleInitiale[i][j];
            }
        }
        
        // Initialisation des résolveurs
        this.deduction = new Deduction(grilleInitiale);
        this.backtracking = new Backtracking(grilleInitiale);
    }

    /**
     * Résout la grille de Sudoku en utilisant une combinaison de déduction et de backtracking.
     *
     * @return true si la grille est résolue, false sinon.
     */
    public boolean resoudreSudoku() {
        System.out.println("\nTentative de résolution par déduction...");

        // Étape 1 : Essayer la déduction
        if (deduction.resoudreSudoku()) {
            System.out.println("Grille résolue par déduction !");
            this.grille = deduction.getGrilleResolue();
            return true;
        }

        System.out.println("Déduction insuffisante, passage au backtracking...");

        // Étape 2 : Si la déduction échoue, utiliser le backtracking
        int[][] grilleIntermediaire = deduction.getGrilleResolue();
        Backtracking backtrackingFinal = new Backtracking(grilleIntermediaire);
        
        if (backtrackingFinal.resoudreSudoku()) {
            System.out.println("Grille résolue par la combinaison Déduction + Backtracking !");
            this.grille = backtrackingFinal.getGrilleResolue();
            return true;
        }

        System.out.println("La grille n'a pas pu être résolue.");
        return false;
    }

    /**
     * Retourne la grille résolue.
     *
     * @return La grille résolue.
     */
    public int[][] getGrilleResolue() {
        return grille;
    }
}