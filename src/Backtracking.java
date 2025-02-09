import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Backtracking {
    public final int taille;
    public final int tailleBloc;
    public int[][] grille;
    public final int[][] grilleOriginale;
    private PrintWriter logWriter;

    public Backtracking(int[][] grilleInitiale) {
        this.taille = grilleInitiale.length;
        this.tailleBloc = (int) Math.sqrt(taille);
        this.grilleOriginale = new int[taille][taille];
        this.grille = new int[taille][taille];
        try {
            Logger.init(); // Initialise le logger sans avoir à spécifier le fichier de log
        } catch (IOException e) {
            System.err.println("Erreur lors de l'initialisation du Logger : " + e.getMessage());
        }

        // Effacer le contenu du fichier avant de commencer à écrire
        try (PrintWriter writer = new PrintWriter(new FileWriter("backtracking_log.txt"))) {
            writer.print("");
        } catch (IOException e) {
            System.err.println("Erreur lors de l'effacement du fichier de log : " + e.getMessage());
        }

        try {
            logWriter = new PrintWriter(new FileWriter("backtracking_log.txt", true));
        } catch (IOException e) {
            System.err.println("Erreur lors de l'initialisation du logWriter : " + e.getMessage());
        }

        // Copie des grilles pour garder l'originale intacte
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                this.grilleOriginale[i][j] = grilleInitiale[i][j];
                this.grille[i][j] = grilleInitiale[i][j];
            }
        }
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

    // Exemple d'utilisation de log dans une méthode de Backtracking
    public void resolver() {
        log("Début de la résolution...");
        // Logique de résolution ici
        log("Fin de la résolution.");
    }

    // Fermer le logger à la fin
    public void closeLogger() {
        Logger.close();
        if (logWriter != null) {
            logWriter.close();
        }
    }

    /**
     * Vérifie si une valeur peut être placée à une position donnée
     */
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
     * Algorithme de backtracking
     */
    public  boolean resoudre() {
        int[] position = new int[2];

        if (!trouverCaseVide(position)) {
            return true; // La grille est complète
        }

        int ligne = position[0];
        int colonne = position[1];

        for (int num = 1; num <= taille; num++) {
            if (estValide(ligne, colonne, num)) {
                grille[ligne][colonne] = num;
                logGrille(); // Loguer la grille après chaque modification

                if (resoudre()) {
                    return true;
                }

                grille[ligne][colonne] = 0; // Backtrack
                logGrille(); // Loguer la grille après chaque backtrack
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
        System.out.println(multiplierCaractere('-', taille * 2 + tailleBloc));

        for (int i = 0; i < taille; i++) {
            if (i > 0 && i % tailleBloc == 0) {
                System.out.println(multiplierCaractere('-', taille * 2 + tailleBloc));
            }

            for (int j = 0; j < taille; j++) {
                if (j > 0 && j % tailleBloc == 0) {
                    System.out.print("| ");
                }
                System.out.print(grille[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println(multiplierCaractere('-', taille * 2 + tailleBloc));
    }

    /**
     * Retourne la grille résolue
     */
    public int[][] getGrilleResolue() {
        return grille;
    }

    private String multiplierCaractere(char c, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
}
