public class Backtracking {
    private final int taille;
    private final int tailleBloc;
    private int[][] grille;
    private final int[][] grilleOriginale;

    public Backtracking(int[][] grilleInitiale) {
        this.taille = grilleInitiale.length;
        this.tailleBloc = (int) Math.sqrt(taille);
        this.grilleOriginale = new int[taille][taille];
        this.grille = new int[taille][taille];
        
        // Copie des grilles pour garder l'originale intacte
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                this.grilleOriginale[i][j] = grilleInitiale[i][j];
                this.grille[i][j] = grilleInitiale[i][j];
            }
        }
    }

    /**
     * Vérifie si une valeur peut être placée à une position donnée
     */
    private boolean estValide(int ligne, int colonne, int valeur) {
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

    // Vérification du bloc
    int debutBloc_i = (ligne / tailleBloc) * tailleBloc;
    int debutBloc_j = (colonne / tailleBloc) * tailleBloc;

    for (int i = debutBloc_i; i < debutBloc_i + tailleBloc; i++) {
        for (int j = debutBloc_j; j < debutBloc_j + tailleBloc; j++) {
            if (grille[i][j] == valeur) {
                return false;
            }
        }
    }

    return true;
}

    /**
     * Trouve la prochaine case vide
     */
    private boolean trouverCaseVide(int[] position) {
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                if (grille[i][j] == 0) {
                    position[0] = i;
                    position[1] = j;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Vérifie si la grille initiale est valide
     */
   private boolean verifierGrilleInitiale() {
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
     * Algorithme de backtracking
     */
    private boolean resoudre() {
        int[] position = new int[2];

        if (!trouverCaseVide(position)) {
            return true; // La grille est complète
        }

        int ligne = position[0];
        int colonne = position[1];

        for (int num = 1; num <= taille; num++) {
            if (estValide(ligne, colonne, num)) {
                grille[ligne][colonne] = num;

                if (resoudre()) {
                    return true;
                }

                grille[ligne][colonne] = 0; // Backtrack
            }
        }

        return false;
    }

    public boolean resoudreSudoku() {
    if (!verifierGrilleInitiale()) {
        System.out.println("La grille initiale n'est pas valide !");
        return false;
    }

    // Réinitialiser la grille de travail
    for (int i = 0; i < taille; i++) {
        for (int j = 0; j < taille; j++) {
            grille[i][j] = grilleOriginale[i][j];
        }
    }

    if (resoudre()) {
        afficherSolution();
        return true;
    } else {
        System.out.println("Aucune solution n'existe pour cette grille !");
        return false;
    }
}

    /**
     * Affiche la solution trouvée
     */
    private void afficherSolution() {
        System.out.println("\nSolution trouvée :");
        System.out.println("-".repeat(taille * 2 + tailleBloc));
        
        for (int i = 0; i < taille; i++) {
            if (i > 0 && i % tailleBloc == 0) {
                System.out.println("-".repeat(taille * 2 + tailleBloc));
            }
            
            for (int j = 0; j < taille; j++) {
                if (j > 0 && j % tailleBloc == 0) {
                    System.out.print("| ");
                }
                System.out.print(grille[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("-".repeat(taille * 2 + tailleBloc));
    }

    /**
     * Retourne la grille résolue
     */
    public int[][] getGrilleResolue() {
        return grille;
    }
}