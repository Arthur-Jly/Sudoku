import java.util.Scanner;

/**
 * Classe représentant le mode texte pour le Multidoku.
 */
public class MultidokuModeTexte extends SudokuModeTexte {
    private int[][] blocs;
    private int taille;

    /**
     * Constructeur de la classe MultidokuModeTexte.
     *
     * @param grille La grille de Multidoku.
     */
    public MultidokuModeTexte(Grille grille) {
        super(grille);
        this.taille = grille.getGrilleValeurs().length;
        this.blocs = new int[taille][taille];
    }

    /**
     * Démarre le jeu en mode texte.
     */
    @Override
    public void demarrerJeu() {
        Scanner scanner = new Scanner(System.in);
        
        // Phase 1: Définition des blocs
        System.out.println("Phase 1: Définition des blocs");
        definirBlocs(scanner);
        
        // Phase 2: Remplissage de la grille
        System.out.println("\nPhase 2: Remplissage de la grille");
        
        int[][] grilleValeurs = grille.getGrilleValeurs();

        // Affichage de la grille initiale
        afficherGrille(grilleValeurs, taille);

        while (!grille.estValidee()) {
            boolean entreeValide = false;

            while (!entreeValide) {
                System.out.println("Entrez la ligne, la colonne (0 à " + (taille - 1) + ") et la valeur à insérer (1 à " + taille + "):");
                int ligne = scanner.nextInt();
                int colonne = scanner.nextInt();
                int valeur = scanner.nextInt();

                scanner.nextLine(); 

                if (ligne >= 0 && ligne < taille && colonne >= 0 && colonne < taille) {
                    if (valeur >= 1 && valeur <= taille) {
                        grille.validerEntree(ligne, colonne, valeur);
                        System.out.println("Grille après saisie :");
                        afficherGrille(grilleValeurs, taille);
                        entreeValide = true;
                    } else {
                        System.out.println("Valeur invalide. La valeur doit être comprise entre 1 et " + taille + ".");
                    }
                } else {
                    System.out.println("Coordonnées invalides. Veuillez entrer des valeurs dans la plage (0 à " + (taille - 1) + ").");
                }
            }

            System.out.println("Tapez 'valider' si vous avez terminé de saisir, sinon appuyez sur Enter pour continuer.");
            String reponse = scanner.nextLine().trim();
            if ("valider".equalsIgnoreCase(reponse)) {
                grille.validerGrille();
            }
        }

        // Phase 3: Résolution
        System.out.println("Choisissez une méthode de résolution :");
        System.out.println("1. Résolution par Backtracking");
        System.out.println("2. Résolution par Déduction");
        int choix = scanner.nextInt();
        scanner.nextLine();

        if (choix == 1) {
            MultidokuBacktracking solveur = new MultidokuBacktracking(grilleValeurs, blocs);  
            if (!solveur.resoudreSudoku()) {
                System.out.println("\nLa grille n'est pas résolvable !");
            } else {
                System.out.println("\nLa grille résolue est :");
                afficherGrille(solveur.getGrilleResolue(), taille);
            }
        } else if (choix == 2) {
            Deduction deduction = new Deduction(grilleValeurs);
            if (!deduction.resoudreSudoku()) {
                System.out.println("\nLa grille ne peut pas être résolue par déduction.");
            } else {
                System.out.println("\nLa grille résolue est :");
                afficherGrille(deduction.getGrilleResolue(), taille);
            }
        } else {
            System.out.println("Choix invalide. Veuillez entrer 1 ou 2.");
        }

        scanner.close();
    }

    /**
     * Définit les blocs de la grille.
     *
     * @param scanner Le scanner pour lire les entrées de l'utilisateur.
     */
    private void definirBlocs(Scanner scanner) {
        System.out.println("Pour chaque position, entrez le numéro du bloc (1-" + taille + ")");
        
        boolean grilleValide = false;
        
        while (!grilleValide) {
            // Réinitialiser les blocs à chaque tentative
            for (int i = 0; i < taille; i++) {
                for (int j = 0; j < taille; j++) {
                    blocs[i][j] = 0;
                }
            }
            
            // Demander à l'utilisateur de remplir les blocs
            for (int ligne = 0; ligne < taille; ligne++) {
                for (int colonne = 0; colonne < taille; colonne++) {
                    boolean entreeValide = false;
                    
                    while (!entreeValide) {
                        afficherGrilleBlocs();
                        System.out.printf("Position [%d,%d] - Entrez le numéro du bloc: ", ligne, colonne);
                        try {
                            int bloc = scanner.nextInt();
                            scanner.nextLine(); 
                            
                            if (bloc >= 1 && bloc <= taille) {
                                blocs[ligne][colonne] = bloc;
                                entreeValide = true;
                            } else {
                                System.out.println("Erreur: Le numéro doit être entre 1 et " + taille);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Erreur: Veuillez entrer un nombre valide");
                            scanner.nextLine(); 
                        }
                    }
                }
            }
            
            // Vérifier si les blocs sont valides
            if (verifierBlocsValides()) {
                grilleValide = true; 
            } else {
                System.out.println("Certains blocs sont invalides, veuillez réessayer.");
            }
        }
        
        System.out.println("\nDéfinition des blocs terminée!");
        afficherGrilleBlocs();
    }

    /**
     * Vérifie si les blocs définis sont valides.
     *
     * @return true si les blocs sont valides, false sinon.
     */
    private boolean verifierBlocsValides() {
        int[] compteBlocs = new int[taille + 1];
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                int bloc = blocs[i][j];
                if (bloc > 0 && bloc <= taille) {
                    compteBlocs[bloc]++;
                }
            }
        }

        // Vérifier que chaque bloc ne contient pas plus de cases que la taille
        for (int i = 1; i <= taille; i++) {
            if (compteBlocs[i] > taille) {
                System.out.println("Le bloc " + i + " contient trop de cases.");
                return false; 
            }
        }
        
        return true; 
    }

    /**
     * Affiche la grille des blocs.
     */
    private void afficherGrilleBlocs() {
        System.out.println("\nGrille des blocs:");
        int tailleBloc = (int) Math.sqrt(taille);
        String ligneSeparatrice = "-".repeat(taille * 4 + tailleBloc);
        System.out.println(ligneSeparatrice);

        for (int i = 0; i < taille; i++) {
            if (i > 0 && i % tailleBloc == 0) {
                System.out.println(ligneSeparatrice);
            }

            for (int j = 0; j < taille; j++) {
                if (j > 0 && j % tailleBloc == 0) {
                    System.out.print("| ");
                }
                String valeur = blocs[i][j] == 0 ? "." : String.valueOf(blocs[i][j]);
                System.out.printf("%-3s", valeur);
            }
            System.out.println();
        }
        System.out.println(ligneSeparatrice);
    }

    /**
     * Affiche la grille actuelle avec les blocs.
     *
     * @param grille La grille à afficher.
     * @param taille La taille de la grille.
     */
    protected void afficherGrille(int[][] grille, int taille) {
        System.out.println("\nGrille actuelle (format: valeur(bloc)):");

        int tailleBloc = (int) Math.sqrt(taille);
        String ligneSeparatrice = "-".repeat(taille * 6 + tailleBloc);
        System.out.println(ligneSeparatrice);

        for (int i = 0; i < taille; i++) {
            if (i > 0 && i % tailleBloc == 0) {
                System.out.println(ligneSeparatrice);
            }

            for (int j = 0; j < taille; j++) {
                if (j > 0 && j % tailleBloc == 0) {
                    System.out.print("| ");
                }
                String valeur = grille[i][j] == 0 ? "." : String.valueOf(grille[i][j]);
                System.out.printf("%s(%d) ", valeur, blocs[i][j]);
            }
            System.out.println();
        }
        System.out.println(ligneSeparatrice);
    }
}