import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MultidokuModeGraphique extends SudokuModeGraphique {
    public JButton[][] boutonsBlocs;
    public int[][] blocs;
    public int blocActuel = 1;
    public JButton boutonChangerBloc;
    public JButton boutonValiderNombres;
    public JButton boutonValider;
    public boolean modeDefinitionBlocs = true;
    public JPanel panneauGrille;
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

    public MultidokuModeGraphique(Grille grille) {
        super(grille);
        this.blocs = new int[taille][taille];
        this.boutonsBlocs = new JButton[taille][taille];
        configurerBlocs();
    }

    private void configurerBlocs() {
        panneauGrille = new JPanel(new GridLayout(taille, taille, 1, 1));
        panneauGrille.setBorder(BorderFactory.createTitledBorder("Définir les blocs"));
        for (int ligne = 0; ligne < taille; ligne++) {
            for (int colonne = 0; colonne < taille; colonne++) {
                JButton boutonBloc = new JButton();
                boutonBloc.setBackground(Color.WHITE);
                boutonBloc.setOpaque(true);
                boutonBloc.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                JTextField champ = new JTextField(2);
                champ.setHorizontalAlignment(JTextField.CENTER);
                champ.setFont(new Font("Arial", Font.BOLD, 20));
                champ.setVisible(false);
                champ.setEditable(false);

                final int l = ligne;
                final int c = colonne;
                
                boutonBloc.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (modeDefinitionBlocs) {
                            if (blocs[l][c] == 0) {
                                blocs[l][c] = blocActuel;
                                boutonBloc.setBackground(couleursBlocs[blocActuel - 1]);
                            } else {
                                blocs[l][c] = 0;
                                boutonBloc.setBackground(Color.WHITE);
                            }
                        }
                    }
                });

                champ.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusLost(FocusEvent e) {
                        validerEntree(champ, l, c);
                    }
                });

                boutonsBlocs[ligne][colonne] = boutonBloc;
                champsTexte[ligne][colonne] = champ;
                panneauGrille.add(boutonBloc);
            }
        }

        boutonChangerBloc = new JButton("Changer de bloc (Bloc actuel : " + blocActuel + ")");
        boutonChangerBloc.addActionListener(e -> {
            if (modeDefinitionBlocs) {
                blocActuel = blocActuel % taille + 1;
                boutonChangerBloc.setText("Changer de bloc (Bloc actuel : " + blocActuel + ")");
            }
        });

        boutonValider = new JButton("Valider les blocs");
        boutonValider.addActionListener(e -> {
            if (tousBlocksDefinis()) {
                modeDefinitionBlocs = false;
                JOptionPane.showMessageDialog(this, "Les blocs ont été validés. Vous pouvez maintenant saisir les valeurs.");
                activerSaisieValeurs();
            } else {
                JOptionPane.showMessageDialog(this, "Tous les blocs doivent être définis avant validation.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        boutonValiderNombres = new JButton("Valider les nombres");
        boutonValiderNombres.setEnabled(false);
        boutonValiderNombres.addActionListener(e -> {
            boolean valide = verifierMultidoku();
            if (valide) {
                JOptionPane.showMessageDialog(this, "La grille est valide !");
                dispose(); 
            }
        });

        // Ajout des boutons de résolution
        JButton boutonBacktracking = new JButton("Résolution par Backtracking");
        JButton boutonDeduction = new JButton("Résolution par Déduction");

        boutonBacktracking.addActionListener(e -> {
            int[][] grilleValeurs = new int[taille][taille];
            for (int ligne = 0; ligne < taille; ligne++) {
                for (int colonne = 0; colonne < taille; colonne++) {
                    String texte = champsTexte[ligne][colonne].getText().trim();
                    if (!texte.isEmpty()) {
                        grilleValeurs[ligne][colonne] = Integer.parseInt(texte);
                    } else {
                        grilleValeurs[ligne][colonne] = 0;
                    }
                }
            }

            Backtracking solveur = new MultidokuBacktracking(grilleValeurs, blocs);
            if (solveur.resoudreSudoku()) {
                afficher_GrilleGraphique(solveur.getGrilleResolue(), blocs);
            } else {
                JOptionPane.showMessageDialog(this, "La grille n'est pas résolvable !");
            }
        });

        boutonDeduction.addActionListener(e -> {
            int[][] grilleValeurs = new int[taille][taille];
            for (int ligne = 0; ligne < taille; ligne++) {
                for (int colonne = 0; colonne < taille; colonne++) {
                    String texte = champsTexte[ligne][colonne].getText().trim();
                    if (!texte.isEmpty()) {
                        grilleValeurs[ligne][colonne] = Integer.parseInt(texte);
                    } else {
                        grilleValeurs[ligne][colonne] = 0;
                    }
                }
            }

            Deduction deduction = new Deduction(grilleValeurs);
            if (deduction.resoudreSudoku()) {
                afficher_GrilleGraphique(deduction.getGrilleResolue(), blocs);
            } else {
                JOptionPane.showMessageDialog(this, "La grille ne peut pas être résolue par déduction.");
            }
        });

        JPanel panneauPrincipal = (JPanel) getContentPane().getComponent(0);
        panneauPrincipal.add(panneauGrille, BorderLayout.CENTER);

        JPanel panneauBoutons = new JPanel();
        panneauBoutons.add(boutonChangerBloc);
        panneauBoutons.add(boutonValider);
        panneauBoutons.add(boutonValiderNombres);
        panneauBoutons.add(boutonBacktracking);
        panneauBoutons.add(boutonDeduction);
        panneauPrincipal.add(panneauBoutons, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private boolean tousBlocksDefinis() {
        for (int[] ligne : blocs) {
            for (int bloc : ligne) {
                if (bloc == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean verifierMultidoku() {
        for (int i = 0; i < taille; i++) {
            if (!verifierLigne(i) || !verifierColonne(i)) {
                JOptionPane.showMessageDialog(this, "Erreur : Les nombres ne respectent pas les règles du Sudoku", "Erreur", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        for (int numBloc = 1; numBloc <= taille; numBloc++) {
            if (!verifierBloc(numBloc)) {
                JOptionPane.showMessageDialog(this, "Erreur : Les nombres ne respectent pas les règles dans le bloc " + numBloc, "Erreur", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }

    private boolean verifierLigne(int ligne) {
        boolean[] presents = new boolean[taille + 1];
        for (int col = 0; col < taille; col++) {
            String valeur = champsTexte[ligne][col].getText().trim();
            if (!valeur.isEmpty()) {
                int num = Integer.parseInt(valeur);
                if (num < 1 || num > taille || presents[num]) {
                    return false;
                }
                presents[num] = true;
            }
        }
        return true;
    }

    private boolean verifierColonne(int colonne) {
        boolean[] presents = new boolean[taille + 1];
        for (int lig = 0; lig < taille; lig++) {
            String valeur = champsTexte[lig][colonne].getText().trim();
            if (!valeur.isEmpty()) {
                int num = Integer.parseInt(valeur);
                if (num < 1 || num > taille || presents[num]) {
                    return false;
                }
                presents[num] = true;
            }
        }
        return true;
    }

    private boolean verifierBloc(int numBloc) {
        boolean[] presents = new boolean[taille + 1];
        for (int ligne = 0; ligne < taille; ligne++) {
            for (int colonne = 0; colonne < taille; colonne++) {
                if (blocs[ligne][colonne] == numBloc) {
                    String valeur = champsTexte[ligne][colonne].getText().trim();
                    if (!valeur.isEmpty()) {
                        int num = Integer.parseInt(valeur);
                        if (num < 1 || num > taille || presents[num]) {
                            return false;
                        }
                        presents[num] = true;
                    }
                }
            }
        }
        return true;
    }

    private void activerSaisieValeurs() {
        panneauGrille.removeAll();
        
        for (int ligne = 0; ligne < taille; ligne++) {
            for (int colonne = 0; colonne < taille; colonne++) {
                JTextField champ = champsTexte[ligne][colonne];
                champ.setVisible(true);
                champ.setEditable(true);
                champ.setBackground(boutonsBlocs[ligne][colonne].getBackground());
                panneauGrille.add(champ);
            }
        }

        boutonChangerBloc.setEnabled(false);
        boutonValider.setEnabled(false);
        boutonValiderNombres.setEnabled(true);
        
        panneauGrille.revalidate();
        panneauGrille.repaint();
    }

    @Override
    public void validerEntree(JTextField champ, int ligne, int colonne) {
        if (!modeDefinitionBlocs) {
            String entree = champ.getText().trim();
            if (!entree.isEmpty()) {
                try {
                    int valeur = Integer.parseInt(entree);
                    if (valeur >= 1 && valeur <= taille) {
                        grille.validerEntree(ligne, colonne, valeur);
                        champ.setForeground(Color.BLUE);
                    } else {
                        champ.setText("");
                        JOptionPane.showMessageDialog(this, "Veuillez entrer un nombre entre 1 et " + taille);
                    }
                } catch (NumberFormatException ex) {
                    champ.setText("");
                    JOptionPane.showMessageDialog(this, "Veuillez entrer un nombre valide.");
                }
            } else {
                grille.validerEntree(ligne, colonne, 0);
            }
        }
    }

    public void afficher_GrilleGraphique(int[][] grilleResolue, int[][] blocs) {
        // Fenêtre pour afficher la grille résolue
        JFrame fenetreSolution = new JFrame("Grille MultiDoku Résolue");
        fenetreSolution.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panneauGrille = new JPanel(new GridLayout(taille, taille));
        for (int ligne = 0; ligne < taille; ligne++) {
            for (int colonne = 0; colonne < taille; colonne++) {
                JTextField champ = new JTextField(String.valueOf(grilleResolue[ligne][colonne]));
                champ.setHorizontalAlignment(JTextField.CENTER);
                champ.setFont(new Font("Arial", Font.BOLD, 20));
                champ.setEditable(false);

                // Récupération de l'identifiant du bloc à partir du tableau des blocs
                int indexBloc = blocs[ligne][colonne];
                champ.setBackground(couleursBlocs[indexBloc % couleursBlocs.length]);

                panneauGrille.add(champ);
            }
        }

        fenetreSolution.add(panneauGrille);
        fenetreSolution.setSize(800, 800);
        fenetreSolution.setLocationRelativeTo(null);
        fenetreSolution.setVisible(true);
    }
}