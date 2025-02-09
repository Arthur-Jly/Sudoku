/**
 * Classe implémentant l'algorithme de backtracking pour résoudre les grilles de Multidoku.
 */
public class MultidokuBacktracking extends Backtracking {
    private final int[][] blocs; // Tableau des blocs personnalisés

    /**
     * Constructeur de la classe MultidokuBacktracking.
     *
     * @param grilleInitiale La grille initiale à résoudre.
     * @param blocs          Les blocs personnalisés.
     */
    public MultidokuBacktracking(int[][] grilleInitiale, int[][] blocs) {
        super(grilleInitiale); // Appel du constructeur de la classe parente
        this.blocs = blocs; // Initialisation des blocs personnalisés
    }

    /**
     * Vérifie si une valeur peut être placée à une position donnée en tenant compte des blocs personnalisés.
     *
     * @param ligne   La ligne de la case.
     * @param colonne La colonne de la case.
     * @param valeur  La valeur à vérifier.
     * @return true si la valeur peut être placée, false sinon.
     */
    @Override
    public boolean estValide(int ligne, int colonne, int valeur) {
        // Vérification de la ligne
        for (int j = 0; j < taille; j++) {
            if (grille[ligne][j] == valeur) {
                return false;
            }
        }

        // Vérification de la colonne
        for (int i = 0; i < taille; i++) {
            if (grille[i][colonne] == valeur) {
                return false;
            }
        }

        // Vérification du bloc personnalisé
        int blocActuel = blocs[ligne][colonne]; // Identifiant du bloc
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                if (blocs[i][j] == blocActuel && grille[i][j] == valeur) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Vérifie si la grille initiale est valide en tenant compte des blocs personnalisés.
     *
     * @return true si la grille initiale est valide, false sinon.
     */
    @Override
    public boolean verifierGrilleInitiale() {
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                int valeur = grilleOriginale[i][j];
                if (valeur != 0) {
                    // Temporairement mettre à 0 pour vérifier la validité
                    grille[i][j] = 0;
                    if (!estValide(i, j, valeur)) {
                        return false;
                    }
                    grille[i][j] = valeur;
                }
            }
        }
        return true;
    }

    /**
     * Retourne la grille résolue.
     *
     * @return La grille résolue.
     */
    @Override
    public int[][] getGrilleResolue() {
        return grille;
    }
}