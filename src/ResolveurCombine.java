public class ResolveurCombine {
    private final int taille;
    private final int tailleBloc;
    private int[][] grille;
    private final int[][] grilleOriginale;
    private final Deduction deduction;
    private final Backtracking backtracking;

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
        
        // Initialisation des deux résolveurs
        this.deduction = new Deduction(grilleInitiale);
        this.backtracking = new Backtracking(grilleInitiale);
    }

    public boolean resoudreSudoku() {
        System.out.println("\nTentative de résolution par déduction...");
        
        // Première étape : essayer la déduction
        if (deduction.resoudreSudoku()) {
            System.out.println("Grille résolue par déduction !");
            this.grille = deduction.getGrilleResolue();
            return true;
        }

        System.out.println("La déduction seule n'a pas suffi, passage au backtracking...");
        
        // Si la déduction n'a pas suffi, utiliser le backtracking
        // On utilise la grille partiellement résolue par déduction
        int[][] grilleIntermediaire = deduction.getGrilleResolue();
        Backtracking backtrackingFinal = new Backtracking(grilleIntermediaire);
        
        if (backtrackingFinal.resoudreSudoku()) {
            System.out.println("Grille résolue par la combinaison déduction + backtracking !");
            this.grille = backtrackingFinal.getGrilleResolue();
            return true;
        }

        System.out.println("La grille n'a pas pu être résolue !");
        return false;
    }

    public void afficherSolution() {
        System.out.println("\nSolution finale :");
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

    public int[][] getGrilleResolue() {
        return grille;
    }
}