public class MultidokuBacktracking extends Backtracking {
    private final int[][] blocs; // Tableau des blocs personnalisés

    public MultidokuBacktracking(int[][] grilleInitiale, int[][] blocs) {
        super(grilleInitiale); // Appel du constructeur de la classe parente
        this.blocs = blocs; // Initialisation des blocs personnalisés
    }

    /**
     * Vérifie si une valeur peut être placée à une position donnée en tenant compte des blocs personnalisés
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
     * Vérifie si la grille initiale est valide en tenant compte des blocs personnalisés
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
     * Retourne la grille résolue
     */
    @Override
    public int[][] getGrilleResolue() {
        return grille;
    }
}