import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Classe Grille pour représenter une grille Sudoku avec une interface graphique.
 * Permet de définir les valeurs initiales de la grille et de configurer les blocs.
 */
public class Grille extends JFrame {

    private int[][] grilleValeurs; // Grille contenant les valeurs initiales du Sudoku
    private int[][] grilleBlocs;   // Grille contenant les blocs définis par l'utilisateur
    private int taille;           // Taille de la grille 
    private JTextField[][] champsTexte; // Champs de texte pour la saisie des valeurs
    private boolean estPremierePhase = true; // Indique si l'utilisateur est dans la phase initiale

    /**
     * Constructeur pour initialiser la grille avec une taille donnée.
     *
     * @param taille Taille de la grille Sudoku.
     */
    public Grille(int taille) {
        this.taille = taille;
        this.grilleValeurs = new int[taille][taille];
        this.grilleBlocs = new int[taille][taille];
        this.champsTexte = new JTextField[taille][taille];
        configurerInterface(); // Configure l'interface graphique
    }

    /**
     * Configure l'interface graphique de la fenêtre.
     */
    private void configurerInterface() {
        setTitle("Grille Sudoku - Phase initiale");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panneauGrille = new JPanel(new GridLayout(taille, taille));

        // Créer les champs de texte pour la grille
        for (int ligne = 0; ligne < taille; ligne++) {
            for (int colonne = 0; colonne < taille; colonne++) {
                JTextField champ = new JTextField(2);
                champ.setHorizontalAlignment(JTextField.CENTER);
                champ.setFont(new Font("Arial", Font.BOLD, 20));
                champ.setBorder(BorderFactory.createLineBorder(Color.BLACK));

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
        JButton boutonValider = new JButton("Valider et passer à la phase suivante");
        JButton boutonAfficher = new JButton("Afficher les grilles");

        // Actions pour les boutons
        boutonValider.addActionListener(e -> passerPhaseSuivante());
        boutonAfficher.addActionListener(e -> afficherGrilles());

        panneauBoutons.add(boutonValider);
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
                if (estPremierePhase) {
                    if (valeur >= 1 && valeur <= taille) {
                        grilleValeurs[ligne][colonne] = valeur;
                    } else {
                        champ.setText("");
                        JOptionPane.showMessageDialog(this, 
                            "Veuillez entrer un nombre entre 1 et " + taille);
                    }
                } else {
                    if (valeur >= 1 && valeur <= taille) {
                        grilleBlocs[ligne][colonne] = valeur;
                    } else {
                        champ.setText("");
                        JOptionPane.showMessageDialog(this, 
                            "Veuillez entrer un nombre entre 1 et " + taille);
                    }
                }
            } catch (NumberFormatException ex) {
                champ.setText("");
                JOptionPane.showMessageDialog(this, "Veuillez entrer un nombre valide");
            }
        }
    }

    /**
     * Affiche les grilles (valeurs et blocs) dans une boîte de dialogue.
     */
    private void afficherGrilles() {
        StringBuilder grilleValeursStr = new StringBuilder("Grille initiale:\n");
        StringBuilder grilleBlocsStr = new StringBuilder("\nGrille des blocs:\n");

        for (int ligne = 0; ligne < taille; ligne++) {
            for (int colonne = 0; colonne < taille; colonne++) {
                grilleValeursStr.append(grilleValeurs[ligne][colonne] == 0 ? "." : grilleValeurs[ligne][colonne]).append(" ");
            }
            grilleValeursStr.append("\n");
        }

        for (int ligne = 0; ligne < taille; ligne++) {
            for (int colonne = 0; colonne < taille; colonne++) {
                grilleBlocsStr.append(grilleBlocs[ligne][colonne] == 0 ? "." : grilleBlocs[ligne][colonne]).append(" ");
            }
            grilleBlocsStr.append("\n");
        }

        JDialog dialogue = new JDialog(this, "Affichage des grilles", true);
        dialogue.setLayout(new BorderLayout());

        JTextArea zoneTexte = new JTextArea(grilleValeursStr.toString() + grilleBlocsStr.toString());
        zoneTexte.setEditable(false);
        zoneTexte.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane zoneDefilement = new JScrollPane(zoneTexte);
        zoneDefilement.setPreferredSize(new Dimension(300, 400));

        JButton boutonFermer = new JButton("Valider et terminer");
        boutonFermer.addActionListener(e -> {
            dialogue.dispose();
            if (!estPremierePhase) {
                dispose();
                System.exit(0);
            }
        });

        JPanel panneauBouton = new JPanel();
        panneauBouton.add(boutonFermer);

        dialogue.add(zoneDefilement, BorderLayout.CENTER);
        dialogue.add(panneauBouton, BorderLayout.SOUTH);

        dialogue.pack();
        dialogue.setLocationRelativeTo(this);
        dialogue.setVisible(true);
    }

    /**
     * Passe à la phase suivante pour définir les blocs de la grille.
     */
    private void passerPhaseSuivante() {
        if (estPremierePhase) {
            estPremierePhase = false;
            setTitle("Grille Sudoku - Définition des blocs");

            for (int ligne = 0; ligne < taille; ligne++) {
                for (int colonne = 0; colonne < taille; colonne++) {
                    champsTexte[ligne][colonne].setText("");
                }
            }

            JOptionPane.showMessageDialog(this,
                "Passez à la définition des blocs.\nUtilisez les nombres de 1 à " + taille);
        } else {
            afficherGrilles();
        }
    }

    /**
     * Initialise et rend la fenêtre visible.
     */
    public void initialiserGrille() {
        setVisible(true);
    }
}
