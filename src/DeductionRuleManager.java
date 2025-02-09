import java.util.HashSet;
import java.util.Set;

/**
 * Classe gérant les règles de déduction pour la résolution de Sudoku.
 */
public class DeductionRuleManager {
    private Set<DeductionRuleType> reglesActives;

    /**
     * Constructeur de la classe DeductionRuleManager.
     * Par défaut, active uniquement la règle SINGLE_CANDIDATE.
     */
    public DeductionRuleManager() {
        this.reglesActives = new HashSet<>();
        reglesActives.add(DeductionRuleType.SINGLE_CANDIDATE);
    }

    /**
     * Applique les règles de déduction actives sur la grille.
     *
     * @param grille        La grille de Sudoku.
     * @param possibilites  Les possibilités pour chaque case.
     * @return true si une modification a été effectuée, false sinon.
     */
    public boolean appliquerRegles(int[][] grille, Set<Integer>[][] possibilites) {
        boolean modification = false;

        for (DeductionRuleType regle : reglesActives) {
            if (regle == DeductionRuleType.SINGLE_CANDIDATE) {
                modification |= appliquerCandidatUnique(grille, possibilites);
            }
        }
        return modification;
    }

    /**
     * Applique la règle du candidat unique sur la grille.
     *
     * @param grille        La grille de Sudoku.
     * @param possibilites  Les possibilités pour chaque case.
     * @return true si une modification a été effectuée, false sinon.
     */
    private boolean appliquerCandidatUnique(int[][] grille, Set<Integer>[][] possibilites) {
        boolean modification = false;

        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille[i].length; j++) {
                if (grille[i][j] == 0 && possibilites[i][j].size() == 1) {
                    int valeur = possibilites[i][j].iterator().next();
                    grille[i][j] = valeur;
                    miseAJourPossibilites(grille, possibilites, i, j, valeur);
                    modification = true;
                }
            }
        }

        return modification;
    }

    /**
     * Met à jour les possibilités après avoir placé une valeur.
     *
     * @param grille        La grille de Sudoku.
     * @param possibilites  Les possibilités pour chaque case.
     * @param ligne         La ligne de la case.
     * @param colonne       La colonne de la case.
     * @param valeur        La valeur placée.
     */
    private void miseAJourPossibilites(int[][] grille, Set<Integer>[][] possibilites, int ligne, int colonne, int valeur) {
        int taille = grille.length;
        int tailleBloc = (int) Math.sqrt(taille);

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

    /**
     * Active une règle de déduction.
     *
     * @param regle La règle à activer.
     */
    public void activerRegle(DeductionRuleType regle) {
        reglesActives.add(regle);
    }

    /**
     * Désactive une règle de déduction.
     *
     * @param regle La règle à désactiver.
     */
    public void desactiverRegle(DeductionRuleType regle) {
        reglesActives.remove(regle);
    }

    /**
     * Retourne les règles de déduction actives.
     *
     * @return Un ensemble des règles de déduction actives.
     */
    public Set<DeductionRuleType> getReglesActives() {
        return reglesActives;
    }
}