import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Demander à l'utilisateur de choisir entre Sudoku et Multidoku
        String[] options = {"Sudoku", "Multidoku"};
        int choix = JOptionPane.showOptionDialog(null, 
            "Choisissez le type de grille :", 
            "Type de grille", 
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            options, 
            options[0]);

        boolean estMultidoku = (choix == 1); // true si Multidoku, false si Sudoku

        // Demander la taille de la grille
        String tailleStr = JOptionPane.showInputDialog(null, 
            "Entrez la taille de la grille (ex : 4, 9, etc.)", 
            "Taille de la grille", 
            JOptionPane.QUESTION_MESSAGE);

        try {
            int taille = Integer.parseInt(tailleStr);

            // Vérifier que la taille est valide
            if (taille <= 0 || taille > 36) {
                throw new NumberFormatException(); // Rejette les tailles non valides
            }

            // Initialiser et afficher la grille
            Grille grille = new Grille(taille, estMultidoku);
            grille.initialiserGrille();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, 
                "Veuillez entrer un nombre valide.", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}