package Class;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Classe représentant le mode graphique pour le Sudoku.
 */
public class SudokuModeGraphique extends JFrame {
    public Grille grille;
    public JTextField[][] champsTexte;
    public final int taille;
    public final int tailleBloc;

    /**
     * Constructeur de la classe SudokuModeGraphique.
     *
     * @param grille La grille de Sudoku.
     */
    public SudokuModeGraphique(Grille grille) {
        this.grille = grille;
        this.taille = grille.getGrilleValeurs().length;
        this.tailleBloc = (int) Math.sqrt(taille);
        this.champsTexte = new JTextField[taille][taille];
        configurerInterface();
    }

    /**
     * Configure l'interface graphique.
     */
    public void configurerInterface() {
        setTitle("Grille Sudoku");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panneauPrincipal = new JPanel(new BorderLayout(10, 10));
        panneauPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panneauGrille = new JPanel(new GridLayout(taille, taille, 1, 1));
        panneauGrille.setBackground(Color.WHITE);
        panneauGrille.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        Color[] couleursBlocs = {
            new Color(173, 216, 230), new Color(255, 228, 225), new Color(255, 255, 224),
            new Color(144, 238, 144), new Color(255, 182, 193), new Color(240, 248, 255),
            new Color(255, 250, 205), new Color(221, 160, 221)
        };

        for (int ligne = 0; ligne < taille; ligne++) {
            for (int colonne = 0; colonne < taille; colonne++) {
                JTextField champ = new JTextField(2);
                champ.setHorizontalAlignment(JTextField.CENTER);
                champ.setFont(new Font("Arial", Font.BOLD, 20));

                int blocLigne = ligne / tailleBloc;
                int blocColonne = colonne / tailleBloc;
                int indexBloc = blocLigne * tailleBloc + blocColonne;
                champ.setBackground(couleursBlocs[indexBloc % couleursBlocs.length]);

                champ.setBorder(BorderFactory.createMatteBorder(1, 1, 
                    (ligne + 1) % tailleBloc == 0 && ligne < taille - 1 ? 2 : 1, 
                    (colonne + 1) % tailleBloc == 0 && colonne < taille - 1 ? 2 : 1, 
                    Color.BLACK));

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

        // Panel des boutons de résolution
        JPanel panneauBoutons = new JPanel();
        JButton boutonBacktracking = new JButton("Backtracking");
        JButton boutonDeduction = new JButton("Déduction");
        JButton boutonCombiné = new JButton("Résolution Combinée");

        boutonBacktracking.addActionListener(e -> lancerResolution(new Backtracking(grille.getGrilleValeurs())));
        boutonDeduction.addActionListener(e -> lancerResolution(new Deduction(grille.getGrilleValeurs())));
        boutonCombiné.addActionListener(e -> lancerResolutionCombine());

        panneauBoutons.add(boutonBacktracking);
        panneauBoutons.add(boutonDeduction);
        panneauBoutons.add(boutonCombiné);

        panneauPrincipal.add(panneauGrille, BorderLayout.CENTER);
        panneauPrincipal.add(panneauBoutons, BorderLayout.SOUTH);
        add(panneauPrincipal);

        setSize(800, 800);
        setLocationRelativeTo(null);
    }

    /**
     * Valide l'entrée de l'utilisateur dans un champ de texte.
     *
     * @param champ   Le champ de texte.
     * @param ligne   La ligne de la case.
     * @param colonne La colonne de la case.
     */
    public void validerEntree(JTextField champ, int ligne, int colonne) {
        String entree = champ.getText().trim();
    
        if (entree.isEmpty()) {
            return;
        }
    
        try {
            int valeur = Integer.parseInt(entree);
    
            // Vérifier que la valeur est dans l'intervalle valide
            if (valeur < 1 || valeur > taille) {
                throw new IllegalArgumentException("Valeur invalide. Elle doit être comprise entre 1 et " + taille);
            }
    
            grille.validerEntree(ligne, colonne, valeur);
            champ.setForeground(Color.BLUE);
        } catch (NumberFormatException e) {
            champ.setText(""); // Efface la valeur erronée
            JOptionPane.showMessageDialog(this, "Veuillez entrer un nombre entre 1 et " + taille, "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            champ.setText(""); // Efface la valeur erronée
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Lance la résolution de la grille en utilisant l'algorithme de backtracking.
     *
     * @param backtracking L'instance de l'algorithme de backtracking.
     */
    public void lancerResolution(Backtracking backtracking) {
        recupererValeursInterface();
        if (backtracking.resoudreSudoku()) {
            afficherGrilleGraphique(backtracking.getGrilleResolue());
        } else {
            JOptionPane.showMessageDialog(this, "La grille n'est pas résolvable !");
        }
    }

    /**
     * Lance la résolution de la grille en utilisant l'algorithme de déduction.
     *
     * @param deduction L'instance de l'algorithme de déduction.
     */
    public void lancerResolution(Deduction deduction) {
        recupererValeursInterface();
        if (deduction.resoudreSudoku()) {
            afficherGrilleGraphique(deduction.getGrilleResolue());
        } else {
            JOptionPane.showMessageDialog(this, "La grille ne peut pas être résolue par déduction.");
        }
    }

    /**
     * Lance la résolution de la grille en utilisant une combinaison de déduction et de backtracking.
     */
    public void lancerResolutionCombine() {
        recupererValeursInterface();
        ResolveurCombine solveur = new ResolveurCombine(grille.getGrilleValeurs());

        if (solveur.resoudreSudoku()) {
            afficherGrilleGraphique(solveur.getGrilleResolue());  // Affiche directement la grille
        } else {
            JOptionPane.showMessageDialog(this, "La grille n'a pas pu être résolue.");
        }
    }

    /**
     * Récupère les valeurs de la grille depuis l'interface utilisateur.
     */
    public void recupererValeursInterface() {
        for (int ligne = 0; ligne < taille; ligne++) {
            for (int colonne = 0; colonne < taille; colonne++) {
                String texte = champsTexte[ligne][colonne].getText().trim();
                if (!texte.isEmpty()) {
                    try {
                        int valeur = Integer.parseInt(texte);
                        grille.validerEntree(ligne, colonne, valeur);
                    } catch (NumberFormatException ex) {
                        champsTexte[ligne][colonne].setText("");
                    }
                }
            }
        }
    }

    /**
     * Affiche la grille résolue graphiquement.
     *
     * @param grilleResolue La grille résolue.
     */
    public void afficherGrilleGraphique(int[][] grilleResolue) {
        for (int ligne = 0; ligne < taille; ligne++) {
            for (int colonne = 0; colonne < taille; colonne++) {
                champsTexte[ligne][colonne].setText(String.valueOf(grilleResolue[ligne][colonne]));
                champsTexte[ligne][colonne].setForeground(Color.BLACK);
            }
        }
    }

    /**
     * Initialise et affiche la grille.
     */
    public void initialiserGrille() {
        setVisible(true);
    }
}