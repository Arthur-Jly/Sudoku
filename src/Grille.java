import java.util.Scanner;

public class Grille {

    private int[][] grille;
    private int taille;

    /**
     * Constructeur de la classe Grille
     * @param taille Custom value of grille
     */
    public Grille(int taille) {
        this.taille = taille;
        this.grille = new int[taille][taille];
    }

    /**
     * Affiche la grille dans le terminal
     */
    public void afficherGrilleTerminal() {
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                System.out.print(grille[i][j] == 0 ? "." : grille[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    /**
     * Initialiser la grille avec les valeurs saisies par l'utilisateur
     */
    public void initialiserGrille() {
        Scanner scanner = new Scanner(System.in);
        boolean continuer = true;

        while (continuer) {
            System.out.print("Entrez l'abscisse (0 a " + (taille - 1) + ") : ");
            int abscisse = scanner.nextInt();

            System.out.print("Entrez l'ordonnee (0 a " + (taille - 1) + ") : ");
            int ordonnee = scanner.nextInt();

            if (abscisse < 0 || abscisse >= taille || ordonnee < 0 || ordonnee >= taille) {
                System.out.println("Coordonnees invalides. Essayez a nouveau.");
                continue;
            }

            System.out.print("Entrez la valeur (1 a 9) pour la case (" + abscisse + ", " + ordonnee + ") : ");
            int valeur = scanner.nextInt();

            if (valeur < 1 || valeur > 9) {
                System.out.println("Valeur invalide. La valeur doit etre entre 1 et 9.");
                continue;
            }

            // Met la valeur dans la grille
            grille[abscisse][ordonnee] = valeur;

            // Demande a l'utilisateur s'il veut continuer
            System.out.print("Voulez-vous continuer ? (oui/non) : ");
            String reponse = scanner.next();

            if (reponse.equalsIgnoreCase("non")) {
                continuer = false;
            }
        }

        // Afficher la grille apres les entrees
        afficherGrilleTerminal();
    }
}
