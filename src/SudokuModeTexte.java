import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SudokuModeTexte {
    public Grille grille;

    public SudokuModeTexte(Grille grille) {
        this.grille = grille;
    }

    public void demarrerJeu() {
        Scanner scanner = new Scanner(System.in);

        // Demander à l'utilisateur s'il souhaite charger la grille depuis un fichier
        System.out.println("Voulez-vous charger la grille depuis le fichier grilleFile.txt ? (oui/non)");
        String reponse = scanner.nextLine().trim();

        int[][] grilleValeurs;
        String methodeResolution = "";

        if (reponse.equalsIgnoreCase("oui")) {
            // Charger la grille depuis le fichier
            try {
                File fichier = new File("grilleFile.txt"); // Nom du fichier fixe
                Scanner fichierScanner = new Scanner(fichier);

                grilleValeurs = new int[9][9]; // Initialiser une nouvelle grille
                int ligne = 0;

                // Lire la grille
                while (fichierScanner.hasNextLine() && ligne < 9) {
                    String ligneFichier = fichierScanner.nextLine().trim();
                    if (ligneFichier.isEmpty() || ligneFichier.startsWith("#")) {
                        continue; // Ignorer les lignes vides ou les commentaires
                    }

                    String[] valeurs = ligneFichier.split(" ");
                    for (int colonne = 0; colonne < valeurs.length; colonne++) {
                        grilleValeurs[ligne][colonne] = Integer.parseInt(valeurs[colonne]);
                    }
                    ligne++;
                }

                // Lire la méthode de résolution
                while (fichierScanner.hasNextLine()) {
                    String ligneFichier = fichierScanner.nextLine().trim();
                    if (!ligneFichier.isEmpty() && !ligneFichier.startsWith("#")) {
                        methodeResolution = ligneFichier;
                        break;
                    }
                }

                fichierScanner.close();
            } catch (FileNotFoundException e) {
                System.out.println("Erreur : Fichier non trouvé. Utilisation de la grille par défaut.");
                grilleValeurs = grille.getGrilleValeurs();
            } catch (NumberFormatException e) {
                System.out.println("Erreur : Le fichier contient des valeurs invalides. Utilisation de la grille par défaut.");
                grilleValeurs = grille.getGrilleValeurs();
            }
        } else {
            // Utiliser la grille par défaut et permettre à l'utilisateur de la saisir
            grilleValeurs = grille.getGrilleValeurs();
            int taille = grilleValeurs.length;

            // Affichage de la grille initiale
            afficherGrille(grilleValeurs, taille);

            while (!grille.estValidee()) {
                boolean entreeValide = false;

                while (!entreeValide) {
                    System.out.println("Entrez la ligne, la colonne (0 à " + (taille - 1) + ") et la valeur à insérer (1 à " + taille + "):");

                    if (!scanner.hasNextInt()) {
                        System.out.println("Entrée invalide. Veuillez entrer des nombres.");
                        scanner.next(); // Consomme l'entrée incorrecte
                        continue;
                    }
                    int ligne = scanner.nextInt();

                    if (!scanner.hasNextInt()) {
                        System.out.println("Entrée invalide. Veuillez entrer des nombres.");
                        scanner.next();
                        continue;
                    }
                    int colonne = scanner.nextInt();

                    if (!scanner.hasNextInt()) {
                        System.out.println("Entrée invalide. Veuillez entrer des nombres.");
                        scanner.next();
                        continue;
                    }
                    int valeur = scanner.nextInt();

                    // Consommer le retour à la ligne restant dans le buffer
                    scanner.nextLine();

                    // Vérifier si la saisie est valide
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
                        System.out.println("Coordonnées invalides. Veuillez entrer des valeurs entre 0 et " + (taille - 1) + ".");
                    }
                }

                // Vérifier si l'utilisateur veut terminer
                System.out.println("Tapez 'valider' si vous avez terminé de saisir, sinon appuyez sur Enter pour continuer.");
                String reponseValidation = scanner.nextLine().trim();
                if ("valider".equalsIgnoreCase(reponseValidation)) {
                    grille.validerGrille();
                }
            }
        }

        int taille = grilleValeurs.length;

        // Si la méthode de résolution n'a pas été lue depuis le fichier, demander à l'utilisateur
        if (methodeResolution.isEmpty()) {
            int choix = -1;
            while (choix < 1 || choix > 3) {
                System.out.println("\nChoisissez une méthode de résolution :");
                System.out.println("1. Résolution par Backtracking");
                System.out.println("2. Résolution par Déduction");
                System.out.println("3. Résolution combinée (Déduction + Backtracking)");

                if (scanner.hasNextInt()) {
                    choix = scanner.nextInt();
                } else {
                    System.out.println("Entrée invalide. Veuillez entrer 1, 2 ou 3.");
                    scanner.next(); // Consomme l'entrée incorrecte
                }
            }

            scanner.nextLine(); // Consommer le retour à la ligne

            switch (choix) {
                case 1:
                    methodeResolution = "backtracking";
                    break;
                case 2:
                    methodeResolution = "deduction";
                    break;
                case 3:
                    methodeResolution = "combine";
                    break;
            }
        }

        // Résolution de la grille selon la méthode choisie
        boolean resolutionReussie = false;
        int[][] grilleResolue = null;

        if (methodeResolution.equalsIgnoreCase("backtracking")) {
            Backtracking solveur = new Backtracking(grilleValeurs);
            resolutionReussie = solveur.resoudreSudoku();
            grilleResolue = solveur.getGrilleResolue();
        } else if (methodeResolution.equalsIgnoreCase("deduction")) {
            Deduction deduction = new Deduction(grilleValeurs);
            resolutionReussie = deduction.resoudreSudoku();
            grilleResolue = deduction.getGrilleResolue();
        } else if (methodeResolution.equalsIgnoreCase("combine")) {
            ResolveurCombine resolveur = new ResolveurCombine(grilleValeurs);
            resolutionReussie = resolveur.resoudreSudoku();
            grilleResolue = resolveur.getGrilleResolue();
        }

        // Affichage du résultat
        if (!resolutionReussie) {
            System.out.println("\nLa grille n'est pas résolvable !");
        } else {
            System.out.println("\nLa grille résolue par la méthode " + methodeResolution + " est :");
            afficherGrille(grilleResolue, taille);
        }

        scanner.close();
    }

    private void afficherGrille(int[][] grille, int taille) {
        int tailleBloc = (int) Math.sqrt(taille);
        System.out.println("-".repeat(taille * 2 + tailleBloc));

        for (int i = 0; i < taille; i++) {
            if (i > 0 && i % tailleBloc == 0) {
                System.out.println("-".repeat(taille * 2 + tailleBloc));
            }

            for (int j = 0; j < taille; j++) {
                if (j > 0 && j % tailleBloc == 0) {
                    System.out.print("| ");
                }
                System.out.print((grille[i][j] == 0 ? "." : grille[i][j]) + " ");
            }
            System.out.println();
        }
        System.out.println("-".repeat(taille * 2 + tailleBloc));
    }
}