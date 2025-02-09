/**
 * Classe représentant une grille de Sudoku.
 */
public class Grille {
    public int[][] grilleValeurs;
    public final int taille;
    public final int tailleBloc;
    public boolean validee = false;

    /**
     * Constructeur de la classe Grille.
     *
     * @param taille La taille de la grille (par exemple, 9 pour une grille 9x9).
     */
    public Grille(int taille) {
        this.taille = taille;
        this.tailleBloc = (int) Math.sqrt(taille);
        this.grilleValeurs = new int[taille][taille];
    }

    /**
     * Valide et insère une valeur dans la grille.
     *
     * @param ligne  La ligne de la case.
     * @param colonne La colonne de la case.
     * @param valeur La valeur à insérer.
     * @throws IllegalArgumentException si la valeur est invalide.
     */
    public void validerEntree(int ligne, int colonne, int valeur) {
        if (valeur >= 1 && valeur <= taille) {
            grilleValeurs[ligne][colonne] = valeur;
        } else {
            throw new IllegalArgumentException("Valeur invalide.");
        }
    }

    /**
     * Retourne les valeurs de la grille.
     *
     * @return Un tableau 2D représentant les valeurs de la grille.
     */
    public int[][] getGrilleValeurs() {
        return grilleValeurs;
    }

    /**
     * Vérifie si la grille a été validée.
     *
     * @return true si la grille est validée, false sinon.
     */
    public boolean estValidee() {
        return validee;
    }

    /**
     * Valide la grille.
     */
    public void validerGrille() {
        validee = true;
    }

    /**
     * Retourne la taille de la grille.
     *
     * @return La taille de la grille.
     */
    public int getTaille() {
        return taille;
    }
}