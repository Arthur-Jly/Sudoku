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

        System.out.println("Voulez-vous charger la grille depuis le fichier grilleFile.txt ? (oui/non)");
        String reponse = scanner.nextLine().trim();

        String methodeResolution = "";

        if (reponse.equalsIgnoreCase("oui")) {
            try {
                File fichier = new File("grilleFile.txt");
                Scanner fichierScanner = new Scanner(fichier);
                
                // Lire la première ligne non commentée pour déterminer la taille
                String premiereLigne = "";
                while (fichierScanner.hasNextLine()) {
                    String ligne = fichierScanner.nextLine().trim();
                    if (!ligne.isEmpty() && !ligne.startsWith("#")) {
                        premiereLigne = ligne;
                        break;
                    }
                }
                
                // Déterminer la taille en comptant les nombres sur la première ligne
                String[] valeursPremiereL = premiereLigne.split(" ");
                int tailleSudoku = valeursPremiereL.length;
                
                // Vérifier si la taille est valide (4, 9, 16, 25, 36)
                int[] taillesValides = {4, 9, 16, 25, 36};
                boolean tailleValide = false;
                for (int t : taillesValides) {
                    if (tailleSudoku == t) {
                        tailleValide = true;
                        break;
                    }
                }
                
                if (!tailleValide) {
                    throw new IllegalArgumentException("Taille de grille invalide. Les tailles autorisées sont : 4, 9, 16, 25, 36");
                }

                // Réinitialiser le scanner pour relire depuis le début
                fichierScanner = new Scanner(fichier);
                int ligne = 0;

                // Lire la grille
                while (fichierScanner.hasNextLine() && ligne < tailleSudoku) {
                    String ligneFichier = fichierScanner.nextLine().trim();
                    if (ligneFichier.isEmpty() || ligneFichier.startsWith("#")) {
                        continue;
                    }

                    String[] valeurs = ligneFichier.split(" ");
                    if (valeurs.length != tailleSudoku) {
                        throw new IllegalArgumentException("La ligne " + (ligne + 1) + " n'a pas le bon nombre de valeurs");
                    }

                    for (int colonne = 0; colonne < valeurs.length; colonne++) {
                        int valeur = Integer.parseInt(valeurs[colonne]);
                        if (valeur < 0 || valeur > tailleSudoku) {
                            throw new IllegalArgumentException("Valeur invalide : " + valeur + ". Les valeurs doivent être entre 0 et " + tailleSudoku);
                        }
                        grille.getGrilleValeurs()[ligne][colonne] = valeur;
                    }
                    ligne++;
                }

                if (ligne != tailleSudoku) {
                    throw new IllegalArgumentException("Nombre de lignes incorrect. Attendu : " + tailleSudoku + ", Trouvé : " + ligne);
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
                resoudreGrille(scanner, methodeResolution);  // Résoudre directement avec la méthode lue

            } catch (FileNotFoundException | IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage() + "\nPassage en mode saisie manuelle.");
                saisieManuelle(scanner);
            }
        } else {
            saisieManuelle(scanner);
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

    // Nouvelle méthode pour extraire la logique de saisie manuelle
    private void saisieManuelle(Scanner scanner) {
        int[][] grilleValeurs = grille.getGrilleValeurs();
        int taille = grilleValeurs.length;

        // Affichage de la grille initiale
        afficherGrille(grilleValeurs, taille);

        while (!grille.estValidee()) {
            boolean entreeValide = false;

            while (!entreeValide) {
                System.out.println("Entrez la ligne puis entrer, la colonne (0 à " + (taille - 1) + ") puis entrer,et la valeur à insérer (1 à " + taille + "):");

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

        // Ajout de la logique de résolution ici
        String methodeResolution = "";
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
                scanner.next();
            }
        }

        scanner.nextLine(); // Consommer le retour à la ligne

        switch (choix) {
            case 1: methodeResolution = "backtracking"; break;
            case 2: methodeResolution = "deduction"; break;
            case 3: methodeResolution = "combine"; break;
        }

        // Résolution de la grille
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
    }

    private void resoudreGrille(Scanner scanner, String methodeResolution) {
        int[][] grilleValeurs = grille.getGrilleValeurs();
        int taille = grilleValeurs.length;

        // Si pas de méthode spécifiée, demander à l'utilisateur
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
                    scanner.next();
                }
            }
            scanner.nextLine();

            switch (choix) {
                case 1: methodeResolution = "backtracking"; break;
                case 2: methodeResolution = "deduction"; break;
                case 3: methodeResolution = "combine"; break;
            }
        }

        // Code de résolution existant...
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

        if (!resolutionReussie) {
            System.out.println("\nLa grille n'est pas résolvable !");
        } else {
            System.out.println("\nLa grille résolue par la méthode " + methodeResolution + " est :");
            afficherGrille(grilleResolue, taille);
        }
    }
}