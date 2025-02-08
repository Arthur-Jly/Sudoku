import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MultidokuModeGraphique extends SudokuModeGraphique {
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 800;
    private static final int BUTTON_FONT_SIZE = 20;
    
    private JButton[][] boutonsBlocs;
    private int[][] blocs;
    private int blocActuel = 1;
    private JButton boutonChangerBloc;
    private JButton boutonValiderNombres;
    private JButton boutonValider;
    private boolean modeDefinitionBlocs = true;
    private JPanel panneauGrille;
    
    private Color[] couleursBlocs = {
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
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        configurerBlocs();
    }

    private int compterCasesBloc(int numBloc) {
        int count = 0;
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                if (blocs[i][j] == numBloc) {
                    count++;
                }
            }
        }
        return count;
    }

    private JButton creerBoutonAction(String texte, ActionListener action) {
        JButton bouton = new JButton(texte);
        bouton.addActionListener(action);
        return bouton;
    }

    private JButton creerBoutonBloc(int ligne, int colonne) {
        JButton boutonBloc = new JButton();
        boutonBloc.setBackground(Color.WHITE);
        boutonBloc.setOpaque(true);
        boutonBloc.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        boutonBloc.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (modeDefinitionBlocs) {
                    if (blocs[ligne][colonne] == 0) {
                        if (compterCasesBloc(blocActuel) >= taille) {
                            JOptionPane.showMessageDialog(null, 
                                "Le bloc " + blocActuel + " a déjà atteint le nombre maximum de cases autorisées (" + taille + ")");
                            return;
                        }
                        blocs[ligne][colonne] = blocActuel;
                        boutonBloc.setBackground(couleursBlocs[blocActuel - 1]);
                    } else {
                        blocs[ligne][colonne] = 0;
                        boutonBloc.setBackground(Color.WHITE);
                    }
                }
            }
        });
        return boutonBloc;
    }

    private JTextField creerChampTexte(int ligne, int colonne) {
        JTextField champ = new JTextField(2);
        champ.setHorizontalAlignment(JTextField.CENTER);
        champ.setFont(new Font("Arial", Font.BOLD, BUTTON_FONT_SIZE));
        champ.setVisible(false);
        champ.setEditable(false);
        champ.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validerEntree(champ, ligne, colonne);
            }
        });
        return champ;
    }

    private void configurerBlocs() {
        JPanel panneauPrincipal = new JPanel(new BorderLayout(10, 10));
        panneauPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Configuration de la grille
        panneauGrille = new JPanel(new GridLayout(taille, taille, 2, 2));
        panneauGrille.setBorder(BorderFactory.createTitledBorder("Définir les blocs"));

        // Création des boutons de la grille
        for (int ligne = 0; ligne < taille; ligne++) {
            for (int colonne = 0; colonne < taille; colonne++) {
                boutonsBlocs[ligne][colonne] = creerBoutonBloc(ligne, colonne);
                champsTexte[ligne][colonne] = creerChampTexte(ligne, colonne);
                panneauGrille.add(boutonsBlocs[ligne][colonne]);
            }
        }

        // Création des boutons de contrôle
        JPanel panneauBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        boutonChangerBloc = creerBoutonAction("Changer de bloc (Bloc actuel : " + blocActuel + ")", 
            e -> {
                if (modeDefinitionBlocs) {
                    blocActuel = blocActuel % taille + 1;
                    boutonChangerBloc.setText("Changer de bloc (Bloc actuel : " + blocActuel + ")");
                }
            });

        boutonValider = creerBoutonAction("Valider les blocs", 
            e -> {
                if (tousBlocksDefinis()) {
                    modeDefinitionBlocs = false;
                    JOptionPane.showMessageDialog(this, "Les blocs ont été validés. Vous pouvez maintenant saisir les valeurs.");
                    activerSaisieValeurs();
                } else {
                    JOptionPane.showMessageDialog(this, "Tous les blocs doivent être définis avant validation.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            });

        boutonValiderNombres = creerBoutonAction("Valider les nombres", 
            e -> {
                if (verifierMultidoku()) {
                    JOptionPane.showMessageDialog(this, "La grille est valide !");
                    dispose();
                }
            });
        boutonValiderNombres.setEnabled(false);

        // Bouton pour la résolution par Backtracking
        JButton boutonBacktracking = creerBoutonAction("Résolution par Backtracking", 
            e -> {
                MultidokuBacktracking solveur = new MultidokuBacktracking(getGrilleValeurs(), blocs);
                if (solveur.resoudreSudoku()) {
                    afficher_GrilleGraphique(solveur.getGrilleResolue(), blocs);
                } else {
                    JOptionPane.showMessageDialog(this, "La grille n'est pas résolvable !");
                }
            });

        // Bouton pour la résolution par Déduction
        JButton boutonDeduction = creerBoutonAction("Résolution par Déduction", 
            e -> {
                MultidokuDeduction solveur = new MultidokuDeduction(getGrilleValeurs(), blocs);
                if (solveur.resoudreSudoku()) {
                    afficher_GrilleGraphique(solveur.getGrilleResolue(), blocs);
                } else {
                    JOptionPane.showMessageDialog(this, "La grille ne peut pas être résolue par déduction.");
                }
            });

        // Ajout des boutons au panneau
        panneauBoutons.add(boutonChangerBloc);
        panneauBoutons.add(boutonValider);
        panneauBoutons.add(boutonValiderNombres);
        panneauBoutons.add(boutonBacktracking);
        panneauBoutons.add(boutonDeduction);

        // Configuration du panneau principal
        panneauPrincipal.add(panneauGrille, BorderLayout.CENTER);
        panneauPrincipal.add(panneauBoutons, BorderLayout.SOUTH);

        setContentPane(panneauPrincipal);
        setLocationRelativeTo(null);
    }

    private int[][] getGrilleValeurs() {
        int[][] grilleValeurs = new int[taille][taille];
        for (int ligne = 0; ligne < taille; ligne++) {
            for (int colonne = 0; colonne < taille; colonne++) {
                String texte = champsTexte[ligne][colonne].getText().trim();
                grilleValeurs[ligne][colonne] = texte.isEmpty() ? 0 : Integer.parseInt(texte);
            }
        }
        return grilleValeurs;
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