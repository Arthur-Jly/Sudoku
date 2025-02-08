import java.util.HashSet;
import java.util.Set;

public class DeductionRuleManager {
    private Set<DeductionRuleType> reglesActives;

    public DeductionRuleManager() {
        this.reglesActives = new HashSet<>();
        // Par défaut, on active uniquement la règle SINGLE_CANDIDATE
        reglesActives.add(DeductionRuleType.SINGLE_CANDIDATE);
    }

    public boolean appliquerRegles(int[][] grille, Set<Integer>[][] possibilites) {
        boolean modification = false;

        for (DeductionRuleType regle : reglesActives) {
            if (regle == DeductionRuleType.SINGLE_CANDIDATE) {
                modification |= appliquerCandidatUnique(grille, possibilites);
            }
        }
        return modification;
    }

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

    public void activerRegle(DeductionRuleType regle) {
        reglesActives.add(regle);
    }

    public void desactiverRegle(DeductionRuleType regle) {
        reglesActives.remove(regle);
    }

    public Set<DeductionRuleType> getReglesActives() {
        return reglesActives;
    }
}