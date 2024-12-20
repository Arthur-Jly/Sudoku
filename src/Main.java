import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Demande la taille de la grille à l'utilisateur
        String tailleStr = JOptionPane.showInputDialog(null, "Entrez la taille de la grille (ex : 4, 9, etc.)","Taille de la grille", JOptionPane.QUESTION_MESSAGE);

        try {
            int taille = Integer.parseInt(tailleStr);

            if (taille <= 0 || taille > 36) {
                throw new NumberFormatException(); // Rejette les tailles non valides
            }

            // Initialise et affiche la grille
            Grille grille = new Grille(taille);
            grille.initialiserGrille();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Veuillez entrer un nombre valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}