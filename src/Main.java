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
            "Valeurs possibles : %s<br>",
            taillesValides
        );
        
        String tailleStr = JOptionPane.showInputDialog(
            null,
            message,
            "Taille de la grille",
            JOptionPane.QUESTION_MESSAGE
        );
        
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
                        "La taille doit être un carré parfait parmi : %s\n" +
                        "Cela permet d'avoir des blocs carrés équilibrés.",
                        taillesValides
                    ),
                    "Taille invalide",
                    JOptionPane.ERROR_MESSAGE
                );
                main(args); // Relance la demande
                return;
            }
            
            // Initialise et affiche la grille
            SwingUtilities.invokeLater(() -> {
                Grille grille = new Grille(taille);
                grille.initialiserGrille();
            });
            
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
}