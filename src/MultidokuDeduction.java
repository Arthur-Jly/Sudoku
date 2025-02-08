public class MultidokuDeduction extends Deduction {
    private final int[][] blocs;  // Stocke la configuration des blocs

    public MultidokuDeduction(int[][] grilleInitiale, int[][] blocs) {
        super(grilleInitiale);
        
        // Vérification des paramètres
        if (grilleInitiale == null || blocs == null) {
            throw new IllegalArgumentException("grilleInitiale et blocs ne doivent pas être null");
        }
        
        if (grilleInitiale.length != blocs.length || grilleInitiale[0].length != blocs[0].length) {
            throw new IllegalArgumentException("grilleInitiale et blocs doivent avoir la même taille");
        }
        
        // Initialisation de blocs
        this.blocs = new int[grilleInitiale.length][grilleInitiale.length];
        
        // Copie du tableau des blocs
        for (int i = 0; i < grilleInitiale.length; i++) {
            System.arraycopy(blocs[i], 0, this.blocs[i], 0, grilleInitiale.length);
        }
    }

    @Override
    public boolean estValide(int ligne, int colonne, int valeur) {
        // Vérifie d'abord les conditions du Sudoku standard
        if (!super.estValide(ligne, colonne, valeur)) {
            return false;
        }

        // Si la valeur est 0 (case vide), elle est valide
        if (valeur == 0) {
            return true;
        }

        // Vérifie en plus la contrainte des blocs
        int blocCourant = blocs[ligne][colonne];
        
        // Parcours toutes les cases du même bloc
        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille.length; j++) {
                // Si on est dans le même bloc et que ce n'est pas la case courante
                if (blocs[i][j] == blocCourant && (i != ligne || j != colonne)) {
                    // Si on trouve la même valeur dans le bloc, c'est invalide
                    if (grille[i][j] == valeur) {
                        return false;
                    }
                }
            }
        }
        
        return true;
    }

    @Override
    public void miseAJourPossibilites(int ligne, int colonne, int valeur) {
        // Vérification que blocs est initialisé
        if (blocs == null) {
            throw new IllegalStateException("blocs n'a pas été initialisé");
        }

        // Mise à jour standard des possibilités (ligne, colonne, carré)
        super.miseAJourPossibilites(ligne, colonne, valeur);
        
        // Mise à jour supplémentaire pour le bloc
        int blocCourant = blocs[ligne][colonne];
        
        // Retire la valeur des possibilités de toutes les cases du même bloc
        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille.length; j++) {
                if (blocs[i][j] == blocCourant) {
                    if (possibilites[i][j] != null) { // Vérification pour éviter NullPointerException
                        possibilites[i][j].remove(valeur);
                    }
                }
            }
        }
    }

    @Override
    public boolean estValeurUnique(int ligne, int colonne, int valeur) {
        // Vérifie d'abord avec la méthode standard
        if (super.estValeurUnique(ligne, colonne, valeur)) {
            return true;
        }
        
        // Vérifie si la valeur est unique dans le bloc
        int blocCourant = blocs[ligne][colonne];
        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille.length; j++) {
                if (blocs[i][j] == blocCourant && (i != ligne || j != colonne)) {
                    if (possibilites[i][j] != null && possibilites[i][j].contains(valeur)) {
                        return false;
                    }
                }
            }
        }
        
        return true;
    }

}