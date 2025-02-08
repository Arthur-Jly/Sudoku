import java.util.HashSet;
import java.util.Set;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Deduction {
    public final int taille;
    public final int tailleBloc;
    public int[][] grille;
    public final int[][] grilleOriginale;
    public Set<Integer>[][] possibilites;
    public PrintWriter logWriter;

    public Deduction(int[][] grilleInitiale) {
        this.taille = grilleInitiale.length;
        this.tailleBloc = (int) Math.sqrt(taille);
        this.grilleOriginale = new int[taille][taille];
        this.grille = new int[taille][taille];
        this.possibilites = new HashSet[taille][taille];
        try {
            Logger.init(); // Initialise le logger sans avoir à spécifier le fichier de log
        } catch (IOException e) {
            System.err.println("Erreur lors de l'initialisation du Logger : " + e.getMessage());
        }

        // Effacer le contenu du fichier avant de commencer à écrire
        try (PrintWriter writer = new PrintWriter(new FileWriter("deduction_log.txt"))) {
            writer.print("");
        } catch (IOException e) {
            System.err.println("Erreur lors de l'effacement du fichier de log : " + e.getMessage());
        }

        try {
            logWriter = new PrintWriter(new FileWriter("deduction_log.txt", true));
        } catch (IOException e) {
            System.err.println("Erreur lors de l'initialisation du logWriter : " + e.getMessage());
        }

        // Copie des grilles et initialisation des possibilités
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                this.grilleOriginale[i][j] = grilleInitiale[i][j];
                this.grille[i][j] = grilleInitiale[i][j];
                this.possibilites[i][j] = new HashSet<>();

                if (grille[i][j] == 0) {
                    for (int num = 1; num <= taille; num++) {
                        this.possibilites[i][j].add(num);
                    }
                }
            }
        }
        miseAJourInitiale();
    }

    // Méthode pour loguer des messages
    protected void log(String message) {
        Logger.log(message);
    }

    // Méthode pour loguer la grille actuelle
    private void logGrille() {
        if (logWriter != null) {
            logWriter.println("Grille actuelle :");
            for (int i = 0; i < taille; i++) {
                for (int j = 0; j < taille; j++) {
                    logWriter.print(grille[i][j] + " ");
                }
                logWriter.println();
            }
            logWriter.println();
            logWriter.flush();
        }
    }

    /**
     * Met à jour les possibilités initiales de la grille.
     */
    private void miseAJourInitiale() {
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                if (grille[i][j] != 0) {
                    miseAJourPossibilites(i, j, grille[i][j]);
                }
            }
        }
    }

    /**
     * Vérifie si une valeur peut être placée à une position donnée.
     */
    private boolean estValide(int ligne, int colonne, int valeur) {
        for (int j = 0; j < taille; j++) {
            if (grille[ligne][j] == valeur) return false;
        }
        for (int i = 0; i < taille; i++) {
            if (grille[i][colonne] == valeur) return false;
        }
        int debutBloc_i = (ligne / tailleBloc) * tailleBloc;
        int debutBloc_j = (colonne / tailleBloc) * tailleBloc;
        for (int i = debutBloc_i; i < debutBloc_i + tailleBloc; i++) {
            for (int j = debutBloc_j; j < debutBloc_j + tailleBloc; j++) {
                if (grille[i][j] == valeur) return false;
            }
        }
        return true;
    }

    /**
     * Résout le Sudoku en utilisant la déduction.
     */
    public boolean resoudreSudoku() {
        log("Début de la résolution par déduction...");
        if (resoudreParDeduction()) {
            afficherGrille();
            log("Grille résolue par déduction !");
            return true;
        } else {
            log("La méthode de déduction ne suffit pas.");
            return false;
        }
    }

    /**
     * Applique la méthode de déduction jusqu'à ce qu'aucun changement ne soit fait.
     */
    private boolean resoudreParDeduction() {
        boolean modification = true;

        while (modification) {
            modification = false;

            for (int i = 0; i < taille; i++) {
                for (int j = 0; j < taille; j++) {
                    if (grille[i][j] == 0) {
                        if (possibilites[i][j].size() == 1) {
                            int valeur = possibilites[i][j].iterator().next();
                            grille[i][j] = valeur;
                            miseAJourPossibilites(i, j, valeur);
                            modification = true;
                            log("Placement de la valeur " + valeur + " en position (" + i + ", " + j + ")");
                            logGrille();
                        } else {
                            Integer valeurUnique = trouverValeurUnique(i, j);
                            if (valeurUnique != null) {
                                grille[i][j] = valeurUnique;
                                miseAJourPossibilites(i, j, valeurUnique);
                                modification = true;
                                log("Placement de la valeur unique " + valeurUnique + " en position (" + i + ", " + j + ")");
                                logGrille();
                            }
                        }
                    }
                }
            }
        }

        return estComplet();
    }

    /**
     * Met à jour les possibilités après avoir placé une valeur.
     */
    private void miseAJourPossibilites(int ligne, int colonne, int valeur) {
        for (int j = 0; j < taille; j++) {
            possibilites[ligne][j].remove(valeur);
        }
        for (int i = 0; i < taille; i++) {
            possibilites[i][colonne].remove(valeur);
        }
        int debutBloc_i = (ligne / tailleBloc) * tailleBloc;
        int debutBloc_j = (colonne / tailleBloc) * tailleBloc;
        for (int i = debutBloc_i; i < debutBloc_i + tailleBloc; i++) {
            for (int j = debutBloc_j; j < debutBloc_j + tailleBloc; j++) {
                possibilites[i][j].remove(valeur);
            }
        }
    }

    /**
     * Trouve une valeur unique pour une case dans la ligne, colonne ou bloc.
     */
    private Integer trouverValeurUnique(int ligne, int colonne) {
        for (int valeur : possibilites[ligne][colonne]) {
            if (estValeurUnique(ligne, colonne, valeur)) {
                return valeur;
            }
        }
        return null;
    }

    private boolean estValeurUnique(int ligne, int colonne, int valeur) {
        for (int j = 0; j < taille; j++) {
            if (j != colonne && possibilites[ligne][j].contains(valeur)) return false;
        }
        for (int i = 0; i < taille; i++) {
            if (i != ligne && possibilites[i][colonne].contains(valeur)) return false;
        }
        int debutBloc_i = (ligne / tailleBloc) * tailleBloc;
        int debutBloc_j = (colonne / tailleBloc) * tailleBloc;
        for (int i = debutBloc_i; i < debutBloc_i + tailleBloc; i++) {
            for (int j = debutBloc_j; j < debutBloc_j + tailleBloc; j++) {
                if ((i != ligne || j != colonne) && possibilites[i][j].contains(valeur)) return false;
            }
        }
        return true;
    }

    /**
     * Vérifie si la grille est complète.
     */
    private boolean estComplet() {
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                if (grille[i][j] == 0) return false;
            }
        }
        return true;
    }

    /**
     * Affiche la grille actuelle.
     */
    private void afficherGrille() {
        for (int[] ligne : grille) {
            for (int val : ligne) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }

    public int[][] getGrilleResolue() {
        return grille;
    }

    public void closeLogger() {
        try {
            Logger.close();
            if (logWriter != null) {
                logWriter.close();
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la fermeture du logger: " + e.getMessage());
        }
    }
}
