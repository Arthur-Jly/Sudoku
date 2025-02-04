import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SudokuModeGraphique extends JFrame {
    public Grille grille;
    public JTextField[][] champsTexte;
    public final int taille;
    public final int tailleBloc;

    public SudokuModeGraphique(Grille grille) {
        this.grille = grille;
        this.taille = grille.getGrilleValeurs().length;
        this.tailleBloc = (int) Math.sqrt(taille);
        this.champsTexte = new JTextField[taille][taille];
        configurerInterface();
    }

    private void configurerInterface() {
        setTitle("Grille Sudoku");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panneauPrincipal = new JPanel(new BorderLayout(10, 10));
        panneauPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel pour la grille avec GridLayout
        JPanel panneauGrille = new JPanel(new GridLayout(taille, taille, 1, 1));
        panneauGrille.setBackground(Color.WHITE);
        panneauGrille.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        // Liste des couleurs à utiliser pour les blocs
        Color[] couleursBlocs = {
            new Color(173, 216, 230), // Bleu clair
            new Color(255, 228, 225), // Rose clair
            new Color(255, 255, 224), // Jaune pâle
            new Color(144, 238, 144), // Vert pâle
            new Color(255, 182, 193), // Rose pâle
            new Color(240, 248, 255), // Bleu pâle
            new Color(255, 250, 205), // Jaune très pâle
            new Color(221, 160, 221)  // Lavande clair
        };

        for (int ligne = 0; ligne < taille; ligne++) {
            for (int colonne = 0; colonne < taille; colonne++) {
                JTextField champ = new JTextField(2);
                champ.setHorizontalAlignment(JTextField.CENTER);
                champ.setFont(new Font("Arial", Font.BOLD, 20));

                // Calculer l'index du bloc (en fonction de la ligne et de la colonne)
                int blocLigne = ligne / tailleBloc;
                int blocColonne = colonne / tailleBloc;
                int indexBloc = blocLigne * tailleBloc + blocColonne;

                champ.setBackground(couleursBlocs[indexBloc % couleursBlocs.length]);

                // Ajouter des bordures pour les blocs
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

        // Panneau des boutons, avec deux boutons pour la résolution
        JPanel panneauBoutons = new JPanel();
        JButton boutonBacktracking = new JButton("Résolution par Backtracking");
        JButton boutonDeduction = new JButton("Résolution par Déduction");

        boutonBacktracking.addActionListener(e -> {
            // Récupérer les valeurs de l'interface graphique et les mettre dans la grille
            for (int ligne = 0; ligne < taille; ligne++) {
                for (int colonne = 0; colonne < taille; colonne++) {
                    String texte = champsTexte[ligne][colonne].getText().trim();
                    if (!texte.isEmpty()) {
                        try {
                            int valeur = Integer.parseInt(texte);
                            grille.validerEntree(ligne, colonne, valeur); // Mettre à jour la grille avec la valeur de l'utilisateur
                        } catch (NumberFormatException ex) {
                            // Si l'utilisateur entre une valeur invalide, ignorer ou afficher un message d'erreur
                            champsTexte[ligne][colonne].setText("");
                        }
                    }
                }
            }

            // Résolution par Backtracking
            Backtracking solveur = new Backtracking(grille.getGrilleValeurs());
            if (solveur.resoudreSudoku()) {
                afficherGrilleGraphique(solveur.getGrilleResolue());
            } else {
                JOptionPane.showMessageDialog(this, "La grille n'est pas résolvable !");
            }
        });

        boutonDeduction.addActionListener(e -> {
            // Récupérer les valeurs de l'interface graphique et les mettre dans la grille
            for (int ligne = 0; ligne < taille; ligne++) {
                for (int colonne = 0; colonne < taille; colonne++) {
                    String texte = champsTexte[ligne][colonne].getText().trim();
                    if (!texte.isEmpty()) {
                        try {
                            int valeur = Integer.parseInt(texte);
                            grille.validerEntree(ligne, colonne, valeur); // Mettre à jour la grille avec la valeur de l'utilisateur
                        } catch (NumberFormatException ex) {
                            // Si l'utilisateur entre une valeur invalide, ignorer ou afficher un message d'erreur
                            champsTexte[ligne][colonne].setText("");
                        }
                    }
                }
            }

            // Résolution par Déduction
            Deduction deduction = new Deduction(grille.getGrilleValeurs());
            if (deduction.resoudreSudoku()) {
                afficherGrilleGraphique(deduction.getGrilleResolue());
            } else {
                JOptionPane.showMessageDialog(this, "La grille ne peut pas être résolue par déduction.");
            }
        });

        panneauBoutons.add(boutonBacktracking);
        panneauBoutons.add(boutonDeduction);

        // Ajouter les composants à la fenêtre principale
        panneauPrincipal.add(panneauGrille, BorderLayout.CENTER);
        panneauPrincipal.add(panneauBoutons, BorderLayout.SOUTH);
        add(panneauPrincipal);

        // Agrandir la taille de la fenêtre
        setSize(800, 800);
        setLocationRelativeTo(null);
    }

    public void validerEntree(JTextField champ, int ligne, int colonne) {
        String entree = champ.getText().trim();
        if (!entree.isEmpty()) {
            try {
                int valeur = Integer.parseInt(entree);
                grille.validerEntree(ligne, colonne, valeur);
                champ.setForeground(Color.BLUE);
            } catch (NumberFormatException ex) {
                champ.setText("");
                JOptionPane.showMessageDialog(this, "Veuillez entrer un nombre valide.");
            }
        } else {
            grille.validerEntree(ligne, colonne, 0);
        }
    }

    public void afficherGrilleGraphique(int[][] grilleResolue) {
        // Fenêtre pour afficher la grille résolue
        JFrame fenetreSolution = new JFrame("Grille Sudoku Résolue");
        fenetreSolution.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panneauGrille = new JPanel(new GridLayout(taille, taille));

        Color[] couleursBlocs = {
            new Color(173, 216, 230),
            new Color(255, 228, 225),
            new Color(255, 255, 224),
            new Color(144, 238, 144),
            new Color(255, 182, 193),
            new Color(240, 248, 255),
            new Color(255, 250, 205),
            new Color(221, 160, 221)
        };

        for (int ligne = 0; ligne < taille; ligne++) {
            for (int colonne = 0; colonne < taille; colonne++) {
                JTextField champ = new JTextField(String.valueOf(grilleResolue[ligne][colonne]));
                champ.setHorizontalAlignment(JTextField.CENTER);
                champ.setFont(new Font("Arial", Font.BOLD, 20));
                champ.setEditable(false);

                int blocLigne = ligne / tailleBloc;
                int blocColonne = colonne / tailleBloc;
                int indexBloc = blocLigne * tailleBloc + blocColonne;

                champ.setBackground(couleursBlocs[indexBloc % couleursBlocs.length]);

                panneauGrille.add(champ);
            }
        }

        fenetreSolution.add(panneauGrille);
        fenetreSolution.setSize(800, 800);
        fenetreSolution.setLocationRelativeTo(null);
        fenetreSolution.setVisible(true);
    }

    public void initialiserGrille() {
        setVisible(true);
    }
}
