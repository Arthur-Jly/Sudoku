import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class Deduction {
    private final int taille;
    private final int tailleBloc;
    private int[][] grille;
    private final int[][] grilleOriginale;
    private Set<Integer>[][] possibilites;
    private DeductionRuleManager ruleManager;
    private PrintWriter logWriter;

    // Constructeur original (pour la compatibilité)
    public Deduction(int[][] grilleInitiale) {
        this(grilleInitiale, new DeductionRuleManager()); // Utilise les règles par défaut
    }

    // Nouveau constructeur avec règles personnalisées
    public Deduction(int[][] grilleInitiale, DeductionRuleManager ruleManager) {
        this.taille = grilleInitiale.length;
        this.tailleBloc = (int) Math.sqrt(taille);
        this.grilleOriginale = new int[taille][taille];
        this.grille = new int[taille][taille];
        this.possibilites = new HashSet[taille][taille];
        this.ruleManager = ruleManager;

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

    private void miseAJourInitiale() {
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                if (grille[i][j] != 0) {
                    miseAJourPossibilites(i, j, grille[i][j]);
                }
            }
        }
    }

    private void miseAJourPossibilites(int ligne, int colonne, int valeur) {
        // Mettre à jour la ligne
        for (int j = 0; j < taille; j++) {
            possibilites[ligne][j].remove(valeur);
        }

        // Mettre à jour la colonne
        for (int i = 0; i < taille; i++) {
            possibilites[i][colonne].remove(valeur);
        }

        // Mettre à jour le bloc
        int debutBloc_i = (ligne / tailleBloc) * tailleBloc;
        int debutBloc_j = (colonne / tailleBloc) * tailleBloc;
        for (int i = debutBloc_i; i < debutBloc_i + tailleBloc; i++) {
            for (int j = debutBloc_j; j < debutBloc_j + tailleBloc; j++) {
                possibilites[i][j].remove(valeur);
            }
        }
    }

    private boolean estValide(int ligne, int colonne, int valeur) {
        // Vérifier la ligne
        for (int j = 0; j < taille; j++) {
            if (grille[ligne][j] == valeur) return false;
        }

        // Vérifier la colonne
        for (int i = 0; i < taille; i++) {
            if (grille[i][colonne] == valeur) return false;
        }

        // Vérifier le bloc
        int debutBloc_i = (ligne / tailleBloc) * tailleBloc;
        int debutBloc_j = (colonne / tailleBloc) * tailleBloc;
        for (int i = debutBloc_i; i < debutBloc_i + tailleBloc; i++) {
            for (int j = debutBloc_j; j < debutBloc_j + tailleBloc; j++) {
                if (grille[i][j] == valeur) return false;
            }
        }

        return true;
    }

    public boolean resoudreSudoku() {
        if (resoudreParDeduction()) {
            return true;
        } else {
            System.out.println("La méthode de déduction ne suffit pas.");
            return false;
        }
    }

    private boolean resoudreParDeduction() {
        boolean modification = true;
        boolean resolved = false;

        while (modification && !resolved) {
            modification = false;

            // Applique toutes les règles actives
            modification = ruleManager.appliquerRegles(grille, possibilites);

            // Si une modification a été faite, met à jour les possibilités
            if (modification) {
                miseAJourPossibilites();
                logGrille(); // Loguer la grille après chaque modification
            }

            resolved = estComplet();
        }

        return resolved;
    }

    private void miseAJourPossibilites() {
        // Réinitialiser toutes les possibilités
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                if (grille[i][j] == 0) {
                    possibilites[i][j].clear();
                    for (int num = 1; num <= taille; num++) {
                        if (estValide(i, j, num)) {
                            possibilites[i][j].add(num);
                        }
                    }
                } else {
                    possibilites[i][j].clear();
                }
            }
        }
    }

    private boolean estComplet() {
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                if (grille[i][j] == 0) return false;
            }
        }
        return true;
    }

    public int[][] getGrilleResolue() {
        return grille;
    }

    public void afficherGrille() {
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                System.out.print(grille[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Fermer le logWriter à la fin
    public void closeLogger() {
        if (logWriter != null) {
            logWriter.close();
        }
    }
}