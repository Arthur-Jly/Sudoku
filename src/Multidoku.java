import java.util.ArrayList;
import java.util.List;

public class Multidoku extends Grille {
    private List<int[][]> blocs;

    public Multidoku(int taille) {
        super(taille);
        this.blocs = new ArrayList<>();
    }

    public void ajouterBloc(int[][] bloc) {
        if (bloc.length != taille || bloc[0].length != taille) {
            throw new IllegalArgumentException("Les dimensions du bloc doivent correspondre à la taille de la grille.");
        }
        blocs.add(bloc);
    }

    public List<int[][]> getBlocs() {
        return blocs;
    }

    @Override
    public void validerGrille() {
        // Ici, vous pouvez ajouter une logique de validation spécifique pour Multidoku
        // Par exemple, vérifier que chaque bloc est valide selon les règles du Multidoku
        super.validerGrille();
    }
}