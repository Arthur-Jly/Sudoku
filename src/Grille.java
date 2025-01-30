import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Grille extends JFrame {

    private int[][] grilleValeurs; // Grille contenant les valeurs initiales du Sudoku
    private int taille;           // Taille de la grille 
    private JTextField[][] champsTexte; // Champs de texte pour la saisie des valeurs
    private boolean estMultidoku; // Indique si l'utilisateur a choisi Multidoku

    /**
     * Constructeur pour initialiser la grille avec une taille donnée.
     *
     * @param taille Taille de la grille Sudoku.
     * @param estMultidoku Indique si l'utilisateur a choisi Multidoku.
     */
    public Grille(int taille, boolean estMultidoku) {
        this.taille = taille;
        this.estMultidoku = estMultidoku;
        this.grilleValeurs = new int[taille][taille];
        this.champsTexte = new JTextField[taille][taille];
        configurerInterface(); // Configure l'interface graphique
    }

    /**
     * Configure l'interface graphique de la fenêtre.
     */
    private void configurerInterface() {
        setTitle("Grille Sudoku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panneauGrille = new JPanel(new GridLayout(taille, taille));

        // Calculer la taille des blocs
        int tailleBloc = (int) Math.sqrt(taille);

        // Créer les champs de texte pour la grille
        for (int ligne = 0; ligne < taille; ligne++) {
            for (int colonne = 0; colonne < taille; colonne++) {
                JTextField champ = new JTextField(2);
                champ.setHorizontalAlignment(JTextField.CENTER);
                champ.setFont(new Font("Arial", Font.BOLD, 20));
                champ.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                // Définir la couleur de fond en fonction du bloc
                int blocLigne = ligne / tailleBloc;
                int blocColonne = colonne / tailleBloc;
                if ((blocLigne + blocColonne) % 2 == 0) {
                    champ.setBackground(new Color(240, 240, 240)); // Couleur claire
                } else {
                    champ.setBackground(new Color(200, 200, 200)); // Couleur foncée
                }

                final int l = ligne;
                final int c = colonne;

                // Ajouter un écouteur pour valider l'entrée utilisateur
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
        JButton boutonAfficher = new JButton("Afficher la grille");

        // Action pour le bouton Afficher
        boutonAfficher.addActionListener(e -> afficherGrille());

        panneauBoutons.add(boutonAfficher);

        setLayout(new BorderLayout());
        add(panneauGrille, BorderLayout.CENTER);
        add(panneauBoutons, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Valide l'entrée utilisateur dans un champ de texte.
     *
     * @param champ  Le champ de texte contenant l'entrée utilisateur.
     * @param ligne  La ligne correspondante dans la grille.
     * @param colonne La colonne correspondante dans la grille.
     */
    private void validerEntree(JTextField champ, int ligne, int colonne) {
        String entree = champ.getText().trim();
        if (!entree.isEmpty()) {
            try {
                int valeur = Integer.parseInt(entree);
                if (valeur >= 1 && valeur <= taille) {
                    grilleValeurs[ligne][colonne] = valeur;
                } else {
                    champ.setText("");
                    JOptionPane.showMessageDialog(this, 
                        "Veuillez entrer un nombre entre 1 et " + taille);
                }
            } catch (NumberFormatException ex) {
                champ.setText("");
                JOptionPane.showMessageDialog(this, "Veuillez entrer un nombre valide");
            }
        }
    }

    /**
     * Affiche la grille dans une boîte de dialogue.
     */
    private void afficherGrille() {
        StringBuilder grilleStr = new StringBuilder("Grille Sudoku:\n");

        for (int ligne = 0; ligne < taille; ligne++) {
            for (int colonne = 0; colonne < taille; colonne++) {
                grilleStr.append(grilleValeurs[ligne][colonne] == 0 ? "." : grilleValeurs[ligne][colonne]).append(" ");
            }
            grilleStr.append("\n");
        }

        JDialog dialogue = new JDialog(this, "Affichage de la grille", true);
        dialogue.setLayout(new BorderLayout());

        JTextArea zoneTexte = new JTextArea(grilleStr.toString());
        zoneTexte.setEditable(false);
        zoneTexte.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane zoneDefilement = new JScrollPane(zoneTexte);
        zoneDefilement.setPreferredSize(new Dimension(300, 400));

        JButton boutonFermer = new JButton("Fermer");
        boutonFermer.addActionListener(e -> dialogue.dispose());

        JPanel panneauBouton = new JPanel();
        panneauBouton.add(boutonFermer);

        dialogue.add(zoneDefilement, BorderLayout.CENTER);
        dialogue.add(panneauBouton, BorderLayout.SOUTH);

        dialogue.pack();
        dialogue.setLocationRelativeTo(this);
        dialogue.setVisible(true);
    }

    /**
     * Initialise et rend la fenêtre visible.
     */
    public void initialiserGrille() {
        setVisible(true);
    }

    /**
     * Méthode principale pour démarrer l'application.
     */
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
            "Entrez la taille de la grille (doit être un carré parfait, ex : 4, 9, 16, etc.)", 
            "Taille de la grille", 
            JOptionPane.QUESTION_MESSAGE);

        try {
            int taille = Integer.parseInt(tailleStr);

            // Vérifier que la taille est un carré parfait
            int sqrt = (int) Math.sqrt(taille);
            if (sqrt * sqrt != taille) {
                throw new NumberFormatException(); // Rejette les tailles non valides
            }

            // Initialiser et afficher la grille
            Grille grille = new Grille(taille, estMultidoku);
            grille.initialiserGrille();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, 
                "Veuillez entrer un carré parfait valide (ex : 4, 9, 16, etc.).", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}