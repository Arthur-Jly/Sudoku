import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant une grille de Multidoku, une variante du Sudoku.
 */
public class Multidoku extends Grille {
    private List<int[][]> blocs;

    /**
     * Constructeur de la classe Multidoku.
     *
     * @param taille La taille de la grille (par exemple, 9 pour une grille 9x9).
     */
    public Multidoku(int taille) {
        super(taille);
        this.blocs = new ArrayList<>();
    }

    /**
     * Ajoute un bloc à la liste des blocs.
     *
     * @param bloc Le bloc à ajouter.
     * @throws IllegalArgumentException si les dimensions du bloc ne correspondent pas à la taille de la grille.
     */
    public void ajouterBloc(int[][] bloc) {
        if (bloc.length != taille || bloc[0].length != taille) {
            throw new IllegalArgumentException("Les dimensions du bloc doivent correspondre à la taille de la grille.");
        }
        blocs.add(bloc);
    }

    /**
     * Retourne la liste des blocs.
     *
     * @return Une liste de blocs.
     */
    public List<int[][]> getBlocs() {
        return blocs;
    }

    /**
     * Valide la grille de Multidoku.
     * Ajoute une logique de validation spécifique pour Multidoku.
     */
    @Override
    public void validerGrille() {
        // Ici, vous pouvez ajouter une logique de validation spécifique pour Multidoku
        // Par exemple, vérifier que chaque bloc est valide selon les règles du Multidoku
        super.validerGrille();
    }
}