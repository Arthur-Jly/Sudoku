import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class SudokuGenerator extends SudokuModeGraphique {
    private static final Random random = new Random();

    public SudokuGenerator(Grille grille) {
        super(grille);
    }

    @Override
    public void configurerInterface() {
        setTitle("Saisir Grille Complète");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panneauPrincipal = new JPanel(new BorderLayout(10, 10));
        panneauPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panneauGrille = new JPanel(new GridLayout(taille, taille, 1, 1));
        panneauGrille.setBackground(Color.WHITE);
        panneauGrille.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

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

                int blocLigne = ligne / tailleBloc;
                int blocColonne = colonne / tailleBloc;
                int indexBloc = blocLigne * tailleBloc + blocColonne;

                champ.setBackground(couleursBlocs[indexBloc % couleursBlocs.length]);

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
        JButton boutonValiderGrille = new JButton("Valider Grille");

        final int[][] grilleComplete = new int[taille][taille];

        boutonValiderGrille.addActionListener(e -> {
            for (int ligne = 0; ligne < taille; ligne++) {
                for (int colonne = 0; colonne < taille; colonne++) {
                    String texte = champsTexte[ligne][colonne].getText().trim();
                    if (!texte.isEmpty()) {
                        try {
                            int valeur = Integer.parseInt(texte);
                            grilleComplete[ligne][colonne] = valeur;
                        } catch (NumberFormatException ex) {
                            champsTexte[ligne][colonne].setText("");
                        }
                    }
                }
            }
            if (estGrilleValide(grilleComplete)) {
                JOptionPane.showMessageDialog(this, "La grille est valide", "Succès", JOptionPane.INFORMATION_MESSAGE);
                lancerGeneration(grilleComplete);
            } else {
                JOptionPane.showMessageDialog(this, "La grille saisie n'est pas valide", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        panneauBoutons.add(boutonValiderGrille);

        panneauPrincipal.add(panneauGrille, BorderLayout.CENTER);
        panneauPrincipal.add(panneauBoutons, BorderLayout.SOUTH);
        add(panneauPrincipal);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void lancerGeneration(int[][] grilleComplete) {
        String[] niveaux = {"Facile", "Moyen", "Difficile"};
        String niveau = (String) JOptionPane.showInputDialog(this, "Choisissez un niveau de difficulté", "Niveau de difficulté",
                JOptionPane.QUESTION_MESSAGE, null, niveaux, niveaux[1]);

        if (niveau != null) {
            int[][] grilleIncomplete = genererGrilleIncomplete(grilleComplete, niveau);
            while (!estResolvable(grilleIncomplete)) {
                grilleIncomplete = genererGrilleIncomplete(grilleComplete, niveau);
            }
            afficherGrilleAvecBoutons(grilleIncomplete);
        }
    }

    private void afficherGrilleAvecBoutons(int[][] grille) {
        JFrame fenetreSolution = new JFrame("Grille Sudoku Incomplète");
        fenetreSolution.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panneauGrille = new JPanel(new GridLayout(taille, taille));

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
                JTextField champ = new JTextField(grille[ligne][colonne] == 0 ? "" : String.valueOf(grille[ligne][colonne]));
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

        JPanel panneauBoutons = new JPanel();
        JButton boutonBacktracking = new JButton("Backtracking");
        JButton boutonDeduction = new JButton("Déduction");

        boutonBacktracking.addActionListener(e -> {
            Backtracking backtracking = new Backtracking(grille);
            if (backtracking.resoudreSudoku()) {
                int[][] grilleResolue = backtracking.getGrilleResolue();
                afficherGrilleGraphique(grilleResolue);
            } else {
                JOptionPane.showMessageDialog(this, "La grille ne peut pas être résolue", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        boutonDeduction.addActionListener(e -> {
            Deduction deduction = new Deduction(grille);
            if (deduction.resoudreSudoku()) {
                int[][] grilleResolue = deduction.getGrilleResolue();
                afficherGrilleGraphique(grilleResolue);
            } else {
                JOptionPane.showMessageDialog(this, "La grille ne peut pas être résolue", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        panneauBoutons.add(boutonBacktracking);
        panneauBoutons.add(boutonDeduction);

        fenetreSolution.add(panneauGrille, BorderLayout.CENTER);
        fenetreSolution.add(panneauBoutons, BorderLayout.SOUTH);
        fenetreSolution.setSize(600, 600); // Définir la taille de la fenêtre
        fenetreSolution.setLocationRelativeTo(null);
        fenetreSolution.setVisible(true);
    }

    @Override
    public void afficherGrilleGraphique(int[][] grille) {
        JFrame fenetreSolution = new JFrame("Grille Résolue");
        fenetreSolution.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panneauGrille = new JPanel(new GridLayout(taille, taille));

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
                JTextField champ = new JTextField(String.valueOf(grille[ligne][colonne]));
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
        fenetreSolution.setSize(600, 600); // Définir la taille de la fenêtre
        fenetreSolution.setLocationRelativeTo(null);
        fenetreSolution.setVisible(true);
    }

    private boolean estGrilleValide(int[][] grille) {
        // Vérifier les lignes
        for (int i = 0; i < taille; i++) {
            boolean[] presence = new boolean[taille + 1];
            for (int j = 0; j < taille; j++) {
                int valeur = grille[i][j];
                if (valeur < 1 || valeur > taille || presence[valeur]) {
                    return false;
                }
                presence[valeur] = true;
            }
        }

        // Vérifier les colonnes
        for (int j = 0; j < taille; j++) {
            boolean[] presence = new boolean[taille + 1];
            for (int i = 0; i < taille; i++) {
                int valeur = grille[i][j];
                if (valeur < 1 || valeur > taille || presence[valeur]) {
                    return false;
                }
                presence[valeur] = true;
            }
        }

        // Vérifier les blocs
        for (int bloc = 0; bloc < taille; bloc++) {
            boolean[] presence = new boolean[taille + 1];
            int ligneDebut = (bloc / tailleBloc) * tailleBloc;
            int colonneDebut = (bloc % tailleBloc) * tailleBloc;

            for (int i = 0; i < tailleBloc; i++) {
                for (int j = 0; j < tailleBloc; j++) {
                    int valeur = grille[ligneDebut + i][colonneDebut + j];
                    if (valeur < 1 || valeur > taille || presence[valeur]) {
                        return false;
                    }
                    presence[valeur] = true;
                }
            }
        }

        return true;
    }

    private boolean estResolvable(int[][] grille) {
        int[][] copieGrille = copierGrille(grille);
        return resoudre(copieGrille, 0, 0);
    }

    private boolean resoudre(int[][] grille, int ligne, int colonne) {
        if (ligne == taille) {
            ligne = 0;
            colonne++;
            if (colonne == taille) {
                return true;
            }
        }

        if (grille[ligne][colonne] != 0) {
            return resoudre(grille, ligne + 1, colonne);
        }

        for (int num = 1; num <= taille; num++) {
            if (estValide(grille, ligne, colonne, num)) {
                grille[ligne][colonne] = num;
                if (resoudre(grille, ligne + 1, colonne)) {
                    return true;
                }
                grille[ligne][colonne] = 0;
            }
        }
        return false;
    }

    private boolean estValide(int[][] grille, int ligne, int colonne, int valeur) {
        // Vérifier la ligne
        for (int j = 0; j < taille; j++) {
            if (grille[ligne][j] == valeur) return false;
        }

        // Vérifier la colonne
        for (int i = 0; i < taille; i++) {
            if (grille[i][colonne] == valeur) return false;
        }

        // Vérifier le bloc
        int blocLigne = (ligne / tailleBloc) * tailleBloc;
        int blocColonne = (colonne / tailleBloc) * tailleBloc;
        for (int i = 0; i < tailleBloc; i++) {
            for (int j = 0; j < tailleBloc; j++) {
                if (grille[blocLigne + i][blocColonne + j] == valeur) return false;
            }
        }

        return true;
    }

    private int[][] genererGrilleIncomplete(int[][] grilleComplete, String niveau) {
        int[][] grille = copierGrille(grilleComplete);
        int nbCasesAEffacer = determinerNombreCasesAEffacer(niveau);

        // Liste des positions à effacer
        int[] positions = new int[taille * taille];
        for (int i = 0; i < positions.length; i++) {
            positions[i] = i;
        }

        // Mélanger les positions
        for (int i = positions.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = positions[i];
            positions[i] = positions[j];
            positions[j] = temp;
        }

        // Effacer les cases dans un ordre aléatoire
        for (int i = 0; i < nbCasesAEffacer; i++) {
            int pos = positions[i];
            int ligne = pos / taille;
            int colonne = pos % taille;
            grille[ligne][colonne] = 0;
        }

        return grille;
    }

    private int determinerNombreCasesAEffacer(String niveau) {
        int nombreTotal = taille * taille;
        return switch (niveau.toLowerCase()) {
            case "facile" -> random.nextInt((int) (nombreTotal * 0.4), (int) (nombreTotal * 0.5));
            case "moyen" -> random.nextInt((int) (nombreTotal * 0.5), (int) (nombreTotal * 0.6));
            case "difficile" -> random.nextInt((int) (nombreTotal * 0.6), (int) (nombreTotal * 0.7));
            default -> random.nextInt((int) (nombreTotal * 0.5), (int) (nombreTotal * 0.6));
        };
    }

    private int[][] copierGrille(int[][] grille) {
        int[][] copie = new int[taille][taille];
        for (int i = 0; i < taille; i++) {
            System.arraycopy(grille[i], 0, copie[i], 0, taille);
        }
        return copie;
    }
}