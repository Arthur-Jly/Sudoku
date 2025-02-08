import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SudokuGenerator extends SudokuModeGraphique {
    private static final int TAILLE = 9;
    private static final int TAILLE_BLOC = (int) Math.sqrt(TAILLE);
    private static final Random random = new Random();

    public SudokuGenerator(Grille grille) {
        super(grille);
        configurerInterface();
    }

    @Override
    public void configurerInterface() {
        try {
            int[][] grilleComplete = obtenirGrilleComplete();
            String[] niveaux = {"Facile", "Moyen", "Difficile"};
            String niveau = (String) JOptionPane.showInputDialog(this, "Choisissez un niveau de difficulté", "Niveau de difficulté", JOptionPane.QUESTION_MESSAGE, null, niveaux, niveaux[1]);
            if (niveau != null) {
                int[][] grilleIncomplète = genererGrilleIncomplète(grilleComplete, niveau);
                afficherGrilleGraphique(grilleIncomplète);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Une erreur est survenue : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public int[][] obtenirGrilleComplete() {
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
            synchronized (this) {
                this.notify();
            }
        });

        panneauBoutons.add(boutonValiderGrille);

        panneauPrincipal.add(panneauGrille, BorderLayout.CENTER);
        panneauPrincipal.add(panneauBoutons, BorderLayout.SOUTH);
        add(panneauPrincipal);

        setSize(800, 800);
        setLocationRelativeTo(null);
        setVisible(true);

        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        return grilleComplete;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Grille grille = new Grille(TAILLE);
            SudokuGenerator fenetre = new SudokuGenerator(grille);
            fenetre.initialiserGrille();
        });
    }

    public static int[][] genererGrilleComplete() {
        int[][] grille = new int[TAILLE][TAILLE];
        remplirGrille(grille);
        appliquerPermutations(grille);
        return grille;
    }

    private static boolean remplirGrille(int[][] grille) {
        for (int i = 0; i < TAILLE; i++) {
            for (int j = 0; j < TAILLE; j++) {
                if (grille[i][j] == 0) {
                    int[] valeurs = obtenirValeursAleatoires();
                    for (int valeur : valeurs) {
                        if (estValide(grille, i, j, valeur)) {
                            grille[i][j] = valeur;
                            if (remplirGrille(grille)) {
                                return true;
                            }
                            grille[i][j] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private static int[] obtenirValeursAleatoires() {
        int[] valeurs = new int[TAILLE];
        for (int i = 0; i < TAILLE; i++) {
            valeurs[i] = i + 1;
        }
        for (int i = TAILLE - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = valeurs[i];
            valeurs[i] = valeurs[index];
            valeurs[index] = temp;
        }
        return valeurs;
    }

    private static void appliquerPermutations(int[][] grille) {
        for (int i = 0; i < 10; i++) {
            permuterLignesBloc(grille, random.nextInt(TAILLE_BLOC), random.nextInt(TAILLE_BLOC), random.nextInt(TAILLE_BLOC));
            permuterColonnesBloc(grille, random.nextInt(TAILLE_BLOC), random.nextInt(TAILLE_BLOC), random.nextInt(TAILLE_BLOC));
            permuterSymboles(grille, random.nextInt(TAILLE) + 1, random.nextInt(TAILLE) + 1);
        }
    }

    public static int[][] genererGrilleIncomplète(int[][] grilleComplete, String niveau) {
        int[][] grille = copierGrille(grilleComplete);
        int nbCasesAEffacer = determinerNombreCasesAEffacer(niveau);
        while (nbCasesAEffacer > 0) {
            int ligne = random.nextInt(TAILLE);
            int colonne = random.nextInt(TAILLE);
            if (grille[ligne][colonne] != 0) {
                grille[ligne][colonne] = 0;
                nbCasesAEffacer--;
            }
        }
        return grille;
    }

    private static int determinerNombreCasesAEffacer(String niveau) {
        return switch (niveau.toLowerCase()) {
            case "facile" -> 30;
            case "moyen" -> 40;
            case "difficile" -> 50;
            default -> 40;
        };
    }

    private static boolean estValide(int[][] grille, int ligne, int colonne, int valeur) {
        for (int i = 0; i < TAILLE; i++) {
            if (grille[ligne][i] == valeur || grille[i][colonne] == valeur) return false;
        }
        int blocLigne = (ligne / TAILLE_BLOC) * TAILLE_BLOC;
        int blocColonne = (colonne / TAILLE_BLOC) * TAILLE_BLOC;
        for (int i = 0; i < TAILLE_BLOC; i++) {
            for (int j = 0; j < TAILLE_BLOC; j++) {
                if (grille[blocLigne + i][blocColonne + j] == valeur) return false;
            }
        }
        return true;
    }

    @Override
    public void afficherGrilleGraphique(int[][] grille) {
        for (int ligne = 0; ligne < TAILLE; ligne++) {
            for (int colonne = 0; colonne < TAILLE; colonne++) {
                if (grille[ligne][colonne] != 0) {
                    champsTexte[ligne][colonne].setText(String.valueOf(grille[ligne][colonne]));
                } else {
                    champsTexte[ligne][colonne].setText("");
                }
            }
        }
    }

    public static void permuterLignesBloc(int[][] grille, int blocLigne, int ligne1, int ligne2) {
        for (int j = 0; j < TAILLE; j++) {
            int temp = grille[blocLigne * TAILLE_BLOC + ligne1][j];
            grille[blocLigne * TAILLE_BLOC + ligne1][j] = grille[blocLigne * TAILLE_BLOC + ligne2][j];
            grille[blocLigne * TAILLE_BLOC + ligne2][j] = temp;
        }
    }

    public static void permuterColonnesBloc(int[][] grille, int blocColonne, int colonne1, int colonne2) {
        for (int i = 0; i < TAILLE; i++) {
            int temp = grille[i][blocColonne * TAILLE_BLOC + colonne1];
            grille[i][blocColonne * TAILLE_BLOC + colonne1] = grille[i][blocColonne * TAILLE_BLOC + colonne2];
            grille[i][blocColonne * TAILLE_BLOC + colonne2] = temp;
        }
    }

    public static void permuterSymboles(int[][] grille, int symbole1, int symbole2) {
        for (int i = 0; i < TAILLE; i++) {
            for (int j = 0; j < TAILLE; j++) {
                if (grille[i][j] == symbole1) grille[i][j] = symbole2;
                else if (grille[i][j] == symbole2) grille[i][j] = symbole1;
            }
        }
    }

    private static int[][] copierGrille(int[][] grille) {
        int[][] copie = new int[TAILLE][TAILLE];
        for (int i = 0; i < TAILLE; i++) {
            System.arraycopy(grille[i], 0, copie[i], 0, TAILLE);
        }
        return copie;
    }
}