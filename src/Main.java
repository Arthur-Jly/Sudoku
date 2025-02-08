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

            // Demander à l'utilisateur quel type de jeu il préfère
            String[] jeuxOptions = {"Sudoku", "Multidoku"};
            int choixJeu = JOptionPane.showOptionDialog(
                    null,
                    "Choisissez le type de jeu :",
                    "Type de jeu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    jeuxOptions,
                    jeuxOptions[0]
            );

            // Créer l'objet grille en fonction du choix de l'utilisateur
            Grille grille;
            if (choixJeu == 0) {
                grille = new Grille(taille); // Si l'utilisateur choisit Sudoku
            } else {
                grille = new Multidoku(taille); // Si l'utilisateur choisit Multidoku
            }

            // Demander à l'utilisateur quel mode il préfère
            String[] modesOptions = {"Graphique", "Texte"};
            int choixMode = JOptionPane.showOptionDialog(
                    null,
                    "Choisissez le mode d'affchage :",
                    "Mode de jeu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    modesOptions,
                    modesOptions[0]
            );

            // Mode graphique
            if (choixMode == 0) {
                if (choixJeu == 0) {
                    SudokuModeGraphique modeGraphique = new SudokuModeGraphique(grille);
                    modeGraphique.initialiserGrille();
                } else {
                    MultidokuModeGraphique modeGraphique = new MultidokuModeGraphique(grille);
                    modeGraphique.initialiserGrille();
                }
            }
            // Mode texte
            else if (choixMode == 1) {

                SudokuModeTexte modeTexte = new MultidokuModeTexte(grille);
                modeTexte.demarrerJeu();
              
                if (choixJeu == 0) {
                    SudokuModeTexte modeTexte = new SudokuModeTexte(grille);
                    modeTexte.demarrerJeu();
                } else {
                    MultidokuModeTexte modeTexte = new MultidokuModeTexte(grille);
                    modeTexte.demarrerJeu();
                }

            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Veuillez entrer un nombre valide.",
                    "Erreur de saisie",
                    JOptionPane.ERROR_MESSAGE
            );
            main(args); // Relance la demande
        } finally {
            // N'oubliez pas d'appeler la méthode closeLogger à la fin de votre programme pour fermer correctement le PrintWriter.
            Backtracking backtracking = new Backtracking(new int[0][0]); // Utilisez une grille vide pour accéder à closeLogger
            backtracking.closeLogger();

            // Fermer le logger de Deduction
            Deduction deduction = new Deduction(new int[0][0]); // Utilisez une grille vide pour accéder à closeLogger
            deduction.closeLogger();
        }
    }

    /**
     * Vérifie si la taille donnée est valide (présente dans TAILLES_VALIDES)
     */
    private static boolean estTailleValide(int taille) {
        return Arrays.stream(TAILLES_VALIDES)
                .anyMatch(t -> t == taille);
    }
}
