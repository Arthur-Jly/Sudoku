import java.util.HashSet;
import java.util.Set;

public class DeductionRuleManager {
    private Set<DeductionRuleType> reglesActives;

    public DeductionRuleManager() {
        this.reglesActives = new HashSet<>();
        // Par défaut, on active les règles de base
        reglesActives.add(DeductionRuleType.SINGLE_CANDIDATE);
        reglesActives.add(DeductionRuleType.UNIQUE_VALUE);
    }

    public boolean appliquerRegles(int[][] grille, Set<Integer>[][] possibilites) {
        boolean modification = false;

        for (DeductionRuleType regle : reglesActives) {
            switch (regle) {
                case SINGLE_CANDIDATE:
                    modification |= appliquerCandidatUnique(grille, possibilites);
                    break;
                case UNIQUE_VALUE:
                    modification |= appliquerValeurUnique(grille, possibilites);
                    break;
                case HIDDEN_SINGLE:
                    modification |= appliquerSingleCache(grille, possibilites);
                    break;
                case NAKED_PAIRS:
                    modification |= appliquerPairesNues(grille, possibilites);
                    break;
                case POINTING_PAIRS:
                    modification |= appliquerPairesPointantes(grille, possibilites);
                    break;
            }
        }
        return modification;
    }

    private boolean appliquerCandidatUnique(int[][] grille, Set<Integer>[][] possibilites) {
        // TODO: Implémenter la règle du candidat unique
        return false;
    }

    private boolean appliquerValeurUnique(int[][] grille, Set<Integer>[][] possibilites) {
        // TODO: Implémenter la règle de la valeur unique
        return false;
    }

    private boolean appliquerSingleCache(int[][] grille, Set<Integer>[][] possibilites) {
        // TODO: Implémenter la règle du single caché
        return false;
    }

    private boolean appliquerPairesNues(int[][] grille, Set<Integer>[][] possibilites) {
        // TODO: Implémenter la règle des paires nues
        return false;
    }

    private boolean appliquerPairesPointantes(int[][] grille, Set<Integer>[][] possibilites) {
        // TODO: Implémenter la règle des paires pointantes
        return false;
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