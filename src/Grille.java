
import java.awt.*;
import javax.swing.*;

// Classe principale pour la grille
public class Grille {

    private int[][] grille;
    private int taille;

    /**
     *
     * @param taille Custom value of grille
     */
    public Grille(int taille) {
        this.taille = taille;
        this.grille = new int[taille][taille];
    }

    /**
     *
     */
    public void afficherGrilleGraphique() {
        JFrame frame = new JFrame("Grille");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);

        JPanel panel = new JPanel(new GridLayout(taille, taille));

        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(new Font("Arial", Font.BOLD, 20));
                cell.setText(grille[i][j] == 0 ? "" : String.valueOf(grille[i][j]));
                cell.setEditable(false);
                panel.add(cell);
            }
        }

        frame.add(panel);
        frame.setVisible(true);
    }

    /**
     *
     */
    public void initialiserGrille() {
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                grille[i][j] = 0; // Initialise toutes les cases à 0
            }
        }
    }
}