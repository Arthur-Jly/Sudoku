import java.awt.event.*;
import java.util.Arrays;
import javax.swing.*;

public class Main {
    // Liste des tailles valides (carrés parfaits jusqu'à 49)
    private static final int[] TAILLES_VALIDES = {4, 9, 16, 25, 36, 49};

    public static void main(String[] args) {
        // Prépare le message avec les tailles valides
        String taillesValides = Arrays.toString(TAILLES_VALIDES)
                .replace("[", "")
                .replace("]", "");

        // Demande la taille avec un message explicatif
        String message = String.format(
                "<html>Entrez la taille de la grille<br>" +
                        "Valeurs possibles : %s<br>" +
                        "(carrés parfaits pour les blocs)</html>",
                taillesValides
        );

        String tailleStr = JOptionPane.showInputDialog(
                null,
                message,
                "Taille de la grille",
                JOptionPane.QUESTION_MESSAGE
        );

        // Si l'utilisateur annule, quitter le programme
        if (tailleStr == null) {
            System.exit(0);
        }

        try {
            int taille = Integer.parseInt(tailleStr);

            // Vérifie si la taille est un carré parfait valide
            if (!estTailleValide(taille)) {
                JOptionPane.showMessageDialog(
                        null,
                        String.format(
                                "La taille doit être un carré parfait parmi : %s\n",
                                taillesValides
                        ),
                        "Taille invalide",
                        JOptionPane.ERROR_MESSAGE
                );
                main(args); // Relance la demande
                return;
            }

            // Création de la grille
            Grille grille = new Grille(taille);

            // Afficher la grille et attendre la saisie de l'utilisateur
            grille.initialiserGrille();

            // Attendre que la grille soit validée
            while (!grille.estValidee()) {
                try {
                    Thread.sleep(100); // Attendre 100 ms
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Récupération de la grille saisie
            int[][] grilleValeurs = grille.getGrilleValeurs();

            // Création et exécution du solveur
            Backtracking solveur = new Backtracking(grilleValeurs);

            // Tentative de résolution
            if (!solveur.resoudreSudoku()) {
                System.out.println("\nLa grille initiale était :");
                afficherGrille(grilleValeurs, taille);
                System.out.println("\nLa grille n'est pas résolvable !");
            } else {
                System.out.println("\nLa grille résolue est :");
                afficherGrille(solveur.getGrilleResolue(), taille);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Veuillez entrer un nombre valide.",
                    "Erreur de saisie",
                    JOptionPane.ERROR_MESSAGE
            );
            main(args); // Relance la demande
        }
    }

    /**
     * Vérifie si la taille donnée est valide (présente dans TAILLES_VALIDES)
     */
    private static boolean estTailleValide(int taille) {
        return Arrays.stream(TAILLES_VALIDES)
                .anyMatch(t -> t == taille);
    }

    /**
     * Affiche une grille dans le terminal
     */
    private static void afficherGrille(int[][] grille, int taille) {
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