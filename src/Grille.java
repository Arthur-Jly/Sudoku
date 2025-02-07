public class Grille {
    public int[][] grilleValeurs;
    public final int taille;
    public final int tailleBloc;
    public boolean validee = false;

    public Grille(int taille) {
        this.taille = taille;
        this.tailleBloc = (int) Math.sqrt(taille);
        this.grilleValeurs = new int[taille][taille];
    }

    public void validerEntree(int ligne, int colonne, int valeur) {
        if (valeur >= 1 && valeur <= taille) {
            grilleValeurs[ligne][colonne] = valeur;
        } else {
            throw new IllegalArgumentException("Valeur invalide.");
        }
    }

    public int[][] getGrilleValeurs() {
        return grilleValeurs;
    }

    public boolean estValidee() {
        return validee;
    }

    public void validerGrille() {
        validee = true;
    }

    public int getTaille() {
        return taille;
    }
}
