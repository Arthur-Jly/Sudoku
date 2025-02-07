import java.util.HashSet;
import java.util.Set;

public class Deduction {
    public final int taille;
    public final int tailleBloc;
    public int[][] grille;
    public final int[][] grilleOriginale;
    public Set<Integer>[][] possibilites;

    public Deduction(int[][] grilleInitiale) {
        this.taille = grilleInitiale.length;
        this.tailleBloc = (int) Math.sqrt(taille);
        this.grilleOriginale = new int[taille][taille];
        this.grille = new int[taille][taille];
        this.possibilites = new HashSet[taille][taille];

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
    public boolean estValide(int ligne, int colonne, int valeur) {
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
        if (resoudreParDeduction()) {
            afficherGrille();
            return true;
        } else {
            System.out.println("La méthode de déduction ne suffit pas.");
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
                        } else {
                            Integer valeurUnique = trouverValeurUnique(i, j);
                            if (valeurUnique != null) {
                                grille[i][j] = valeurUnique;
                                miseAJourPossibilites(i, j, valeurUnique);
                                modification = true;
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
    public void miseAJourPossibilites(int ligne, int colonne, int valeur) {
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

    public boolean estValeurUnique(int ligne, int colonne, int valeur) {
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
}
