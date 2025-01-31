import java.awt.*;
import java.awt.event.*;
import static java.lang.Math.sqrt;
import javax.swing.*;

public class Grille extends JFrame {
    private int[][] grilleValeurs;
    private JTextField[][] champsTexte;
    private final int taille;
    private final int tailleBloc;
    private boolean validee = false;

    public Grille(int taille) {
        this.taille = taille;
        this.tailleBloc = (int) sqrt(taille);
        this.grilleValeurs = new int[taille][taille];
        this.champsTexte = new JTextField[taille][taille];
        configurerInterface();
    }

    private void configurerInterface() {
        setTitle("Grille Sudoku");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panneauPrincipal = new JPanel(new BorderLayout(10, 10));
        panneauPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panneauGrille = new JPanel(new GridLayout(taille, taille, 1, 1));
        panneauGrille.setBackground(Color.BLACK);
        panneauGrille.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        for (int ligne = 0; ligne < taille; ligne++) {
            for (int colonne = 0; colonne < taille; colonne++) {
                JTextField champ = new JTextField(2);
                champ.setHorizontalAlignment(JTextField.CENTER);
                champ.setFont(new Font("Arial", Font.BOLD, 20));

                boolean bordureDroite = (colonne + 1) % tailleBloc == 0 && colonne < taille - 1;
                boolean bordureBas = (ligne + 1) % tailleBloc == 0 && ligne < taille - 1;

                champ.setBorder(BorderFactory.createMatteBorder(1, 1, bordureBas ? 2 : 1, bordureDroite ? 2 : 1, Color.BLACK));

                final int l = ligne;
                final int c = colonne;
                champ.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusLost(FocusEvent e) {
                        validerEntree(champ, l, c);
                    }
                });

                champsTexte[ligne][colonne] = champ;
                panneauGrille.add(champ);
            }
        }

        JPanel panneauBoutons = new JPanel();
        JButton boutonValider = new JButton("Valider");
        boutonValider.addActionListener(e -> {
            validee = true;
            afficherDansTerminal();
            dispose(); // Ferme la fenêtre sans arrêter le programme
        });
        panneauBoutons.add(boutonValider);

        panneauPrincipal.add(panneauGrille, BorderLayout.CENTER);
        panneauPrincipal.add(panneauBoutons, BorderLayout.SOUTH);
        add(panneauPrincipal);

        pack();
        setLocationRelativeTo(null);
    }

    private void validerEntree(JTextField champ, int ligne, int colonne) {
        String entree = champ.getText().trim();
        if (!entree.isEmpty()) {
            try {
                int valeur = Integer.parseInt(entree);
                if (valeur >= 1 && valeur <= taille) {
                    grilleValeurs[ligne][colonne] = valeur;
                    champ.setForeground(Color.BLUE);
                } else {
                    champ.setText("");
                    JOptionPane.showMessageDialog(this, "Veuillez entrer un nombre entre 1 et " + taille);
                }
            } catch (NumberFormatException ex) {
                champ.setText("");
                JOptionPane.showMessageDialog(this, "Veuillez entrer un nombre valide");
            }
        } else {
            grilleValeurs[ligne][colonne] = 0;
        }
    }

    private void afficherDansTerminal() {
        System.out.println("\nGrille Sudoku saisie :");
        System.out.println("-".repeat(taille * 2 + tailleBloc));

        for (int ligne = 0; ligne < taille; ligne++) {
            if (ligne > 0 && ligne % tailleBloc == 0) {
                System.out.println("-".repeat(taille * 2 + tailleBloc));
            }

            for (int colonne = 0; colonne < taille; colonne++) {
                if (colonne > 0 && colonne % tailleBloc == 0) {
                    System.out.print("| ");
                }
                int valeur = grilleValeurs[ligne][colonne];
                System.out.print((valeur == 0 ? "." : valeur) + " ");
            }
            System.out.println();
        }
        System.out.println("-".repeat(taille * 2 + tailleBloc));
    }

    public void initialiserGrille() {
        setVisible(true);
    }

    public int[][] getGrilleValeurs() {
        return grilleValeurs;
    }

    public boolean estValidee() {
        return validee;
    }
}