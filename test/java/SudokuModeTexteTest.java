import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

class SudokuModeTexteTest {
    private SudokuModeTexte sudokuModeTexte;
    private Grille grille;

    @BeforeEach
    void setUp() {
        grille = new Grille(4); // Initialisation d'une grille 4x4 pour simplifier les tests
        sudokuModeTexte = new SudokuModeTexte(grille);
    }

    @Test
    void testSudokuModeTexteInitialization() {
        assertNotNull(sudokuModeTexte, "L'instance de SudokuModeTexte ne doit pas être null");
        assertEquals(4, grille.getTaille(), "La taille de la grille doit être 4");
    }

    @Test
    void testAfficherGrille() {
        int[][] valeurs = {
            {1, 0, 3, 4},
            {3, 4, 0, 2},
            {0, 1, 4, 3},
            {4, 3, 2, 1}
        };

        for (int i = 0; i < 4; i++) {
            System.arraycopy(valeurs[i], 0, grille.getGrilleValeurs()[i], 0, 4);
        }

        assertArrayEquals(valeurs, grille.getGrilleValeurs(), "La grille doit contenir les valeurs saisies");
    }

    @Test
    void testValiderEntree() {
        sudokuModeTexte.grille.validerEntree(0, 0, 2);
        assertEquals(2, sudokuModeTexte.grille.getGrilleValeurs()[0][0], "La valeur 2 doit être insérée en (0,0)");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            sudokuModeTexte.grille.validerEntree(0, 0, 5);
        });

        assertEquals("Valeur invalide.", exception.getMessage(), "Une exception doit être levée pour une valeur hors limites");
    }

    @Test
    void testLectureFichierGrilleValide() throws IOException {
        File testFile = new File("testGrille.txt");
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("1 2 3 4\n");
            writer.write("3 4 1 2\n");
            writer.write("2 1 4 3\n");
            writer.write("4 3 2 1\n");
        }

        // Simuler la lecture du fichier
        Scanner scanner = new Scanner(testFile);
        int[][] valeurs = new int[4][4];
        int ligne = 0;

        while (scanner.hasNextLine() && ligne < 4) {
            String[] valeursLigne = scanner.nextLine().trim().split(" ");
            for (int colonne = 0; colonne < 4; colonne++) {
                valeurs[ligne][colonne] = Integer.parseInt(valeursLigne[colonne]);
                grille.getGrilleValeurs()[ligne][colonne] = valeurs[ligne][colonne]; // Copier les valeurs dans la grille
            }
            ligne++;
        }

        scanner.close();
        testFile.delete(); 

        assertArrayEquals(valeurs, grille.getGrilleValeurs(), "La grille doit contenir les valeurs du fichier");
    }

    @Test
    void testLectureFichierGrilleInvalide() throws IOException {
        File testFile = new File("testGrilleInvalide.txt");
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("1 2 3\n"); // Ligne invalide (trop courte)
            writer.write("3 4 1 2\n");
            writer.write("2 1 4 3\n");
            writer.write("4 3 2 1\n");
        }

        Scanner scanner = new Scanner(testFile);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            int ligne = 0;
            while (scanner.hasNextLine() && ligne < 4) {
                String[] valeursLigne = scanner.nextLine().trim().split(" ");
                if (valeursLigne.length != 4) {
                    throw new IllegalArgumentException("La ligne " + (ligne + 1) + " n'a pas le bon nombre de valeurs");
                }
                ligne++;
            }
        });

        scanner.close();
        testFile.delete(); 

        assertTrue(exception.getMessage().contains("n'a pas le bon nombre de valeurs"), "L'erreur doit signaler une ligne invalide");
    }

    @Test
    void testValidationGrille() {
        assertFalse(grille.estValidee(), "La grille ne devrait pas être validée avant l'appel à `validerGrille`");
        grille.validerGrille();
        assertTrue(grille.estValidee(), "La grille devrait être validée après l'appel à `validerGrille`");
    }

    @Test
    void testResolutionBacktracking() {
        // Préparer une grille simple 4x4
        int[][] grilleInitiale = {
            {1, 0, 0, 4},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {4, 0, 0, 1}
        };

        // Copier les valeurs dans la grille
        for (int i = 0; i < 4; i++) {
            System.arraycopy(grilleInitiale[i], 0, grille.getGrilleValeurs()[i], 0, 4);
        }

        // Créer et lancer le solveur Backtracking
        Backtracking backtracking = new Backtracking(grille.getGrilleValeurs());
        boolean resolu = backtracking.resoudreSudoku();
        int[][] grilleResolue = backtracking.getGrilleResolue();

        // Vérifier que la grille a été résolue
        assertTrue(resolu, "La grille devrait être résoluble par backtracking");
        
        // Vérifier que les valeurs initiales sont préservées
        assertEquals(1, grilleResolue[0][0]);
        assertEquals(4, grilleResolue[0][3]);
        assertEquals(4, grilleResolue[3][0]);
        assertEquals(1, grilleResolue[3][3]);

        // Vérifier que toutes les cases sont remplies
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertTrue(grilleResolue[i][j] > 0 && grilleResolue[i][j] <= 4,
                    "Chaque case doit contenir une valeur entre 1 et 4");
            }
        }
    }

    @Test
    void testResolutionDeduction() {
        // Préparer une grille simple 4x4 avec suffisamment d'indices pour la déduction
        int[][] grilleInitiale = {
            {1, 2, 0, 4},
            {0, 0, 2, 0},
            {0, 1, 0, 0},
            {4, 0, 1, 2}
        };

        // Copier les valeurs dans la grille
        for (int i = 0; i < 4; i++) {
            System.arraycopy(grilleInitiale[i], 0, grille.getGrilleValeurs()[i], 0, 4);
        }

        // Créer et lancer le solveur par Déduction
        Deduction deduction = new Deduction(grille.getGrilleValeurs());
        boolean resolu = deduction.resoudreSudoku();
        int[][] grilleResolue = deduction.getGrilleResolue();

        // Vérifier que les valeurs initiales sont préservées
        assertEquals(1, grilleResolue[0][0]);
        assertEquals(2, grilleResolue[0][1]);
        assertEquals(4, grilleResolue[0][3]);
        assertEquals(4, grilleResolue[3][0]);
    }

    @Test
    void testResolutionCombinee() {
        // Préparer une grille simple 4x4
        int[][] grilleInitiale = {
            {1, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        };

        // Copier les valeurs dans la grille
        for (int i = 0; i < 4; i++) {
            System.arraycopy(grilleInitiale[i], 0, grille.getGrilleValeurs()[i], 0, 4);
        }

        // Créer et lancer le solveur Combiné
        ResolveurCombine resolveur = new ResolveurCombine(grille.getGrilleValeurs());
        boolean resolu = resolveur.resoudreSudoku();
        int[][] grilleResolue = resolveur.getGrilleResolue();

        // Vérifier que la grille a été résolue
        assertTrue(resolu, "La grille devrait être résoluble par la méthode combinée");
        
        // Vérifier que les valeurs initiales sont préservées
        assertEquals(1, grilleResolue[0][0]);
        assertEquals(3, grilleResolue[0][2]);
        assertEquals(2, grilleResolue[1][3]);
        assertEquals(1, grilleResolue[3][3]);

        // Vérifier que toutes les cases sont remplies
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertTrue(grilleResolue[i][j] > 0 && grilleResolue[i][j] <= 4,
                    "Chaque case doit contenir une valeur entre 1 et 4");
            }
        }
    }
}
