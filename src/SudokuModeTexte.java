import java.util.Scanner;

public class SudokuModeTexte {
    private Grille grille;

    public SudokuModeTexte(Grille grille) {
        this.grille = grille;
    }

    public void demarrerJeu() {
        Scanner scanner = new Scanner(System.in);
        int[][] grilleValeurs = grille.getGrilleValeurs();
        int taille = grilleValeurs.length;

        // Affichage de la grille initiale
        afficherGrille(grilleValeurs, taille);

        while (!grille.estValidee()) {
            boolean entreeValide = false;

            while (!entreeValide) {
                System.out.println("Entrez la ligne, la colonne (0 à " + (taille - 1) + ") et la valeur à insérer (1 à " + taille + "):");
                int ligne = scanner.nextInt();
                int colonne = scanner.nextInt();
                int valeur = scanner.nextInt();

                // Consommer le retour à la ligne restant dans le buffer
                scanner.nextLine();

                // Vérifier si la saisie est valide
                if (ligne >= 0 && ligne < taille && colonne >= 0 && colonne < taille) {
                    if (valeur >= 1 && valeur <= taille) {
                        grille.validerEntree(ligne, colonne, valeur);
                        System.out.println("Grille après saisie :");
                        afficherGrille(grilleValeurs, taille);
                        entreeValide = true; // Saisie correcte
                    } else {
                        System.out.println("Valeur invalide. La valeur doit être comprise entre 1 et " + taille + ".");
                    }
                } else {
                    System.out.println("Coordonnées invalides. Veuillez entrer des valeurs dans la plage (0 à " + (taille - 1) + ").");
                }
            }

            // Vérifier si l'utilisateur veut terminer
            System.out.println("Tapez 'valider' si vous avez terminé de saisir, sinon appuyez sur Enter pour continuer.");
            String reponse = scanner.nextLine().trim(); // Utilise nextLine pour lire la réponse complète
            if ("valider".equalsIgnoreCase(reponse)) {
                grille.validerGrille();
            }
        }

        // Choisir le mode de résolution
        System.out.println("Choisissez une méthode de résolution :");
        System.out.println("1. Résolution par Backtracking");
        System.out.println("2. Résolution par Déduction");
        int choix = scanner.nextInt();
        scanner.nextLine();  // Consommer le retour à la ligne restant dans le buffer

        // La grille est validée, on la résout
        if (choix == 1) {
            Backtracking solveur = new Backtracking(grilleValeurs);
            if (!solveur.resoudreSudoku()) {
                System.out.println("\nLa grille n'est pas résolvable !");
            } else {
                System.out.println("\nLa grille résolue est :");
                afficherGrille(solveur.getGrilleResolue(), taille);
            }
        } else if (choix == 2) {
            Deduction deduction = new Deduction(grilleValeurs);
            if (!deduction.resoudreSudoku()) {
                System.out.println("\nLa grille ne peut pas être résolue par déduction.");
            } else {
                System.out.println("\nLa grille résolue est :");
                afficherGrille(deduction.getGrilleResolue(), taille);
            }
        } else {
            System.out.println("Choix invalide. Veuillez entrer 1 ou 2.");
        }

        scanner.close();
    }

    private void afficherGrille(int[][] grille, int taille) {
        int tailleBloc = (int) Math.sqrt(taille);
        System.out.println("-".repeat(taille * 2 + tailleBloc));

        for (int i = 0; i < taille; i++) {
            if (i > 0 && i % tailleBloc == 0) {
                System.out.println("-".repeat(taille * 2 + tailleBloc));
            }

            for (int j = 0; j < taille; j++) {
                if (j > 0 && j % tailleBloc == 0) {
                    System.out.print("| ");
                }
                System.out.print((grille[i][j] == 0 ? "." : grille[i][j]) + " ");
            }
            System.out.println();
        }
        System.out.println("-".repeat(taille * 2 + tailleBloc));
    }
}
