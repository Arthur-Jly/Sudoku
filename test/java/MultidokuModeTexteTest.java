import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class MultidokuModeTexteTest {
    private MultidokuModeTexte multidoku;
    private Grille grille;

    @BeforeEach
    void setUp() {
        grille = new Grille(4); 
        multidoku = new MultidokuModeTexte(grille);
    }

    @Test
    void testInitialisation() {
        assertNotNull(multidoku, "L'instance MultidokuModeTexte ne devrait pas être null");
        assertEquals(4, grille.getTaille(), "La taille de la grille devrait être 4");
        assertFalse(grille.estValidee(), "La grille ne devrait pas être validée au départ");
    }

    @Test
    void testRemplissageGrille() {
        int[][] valeurs = {
            {1, 2, 3, 4},
            {3, 4, 1, 2},
            {2, 1, 4, 3},
            {4, 3, 2, 1}
        };

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                grille.grilleValeurs[i][j] = valeurs[i][j]; 
            }
        }

        assertArrayEquals(valeurs, grille.getGrilleValeurs(), "La grille devrait contenir les valeurs saisies");
    }

    @Test
    void testValidationGrille() {
        assertFalse(grille.estValidee(), "La grille ne devrait pas être validée avant l'appel à `validerGrille`");
        grille.validerGrille();
        assertTrue(grille.estValidee(), "La grille devrait être validée après l'appel à `validerGrille`");
    }

    @Test
    void testValiderEntree() {
        grille.validerEntree(0, 0, 3);
        assertEquals(3, grille.getGrilleValeurs()[0][0], "La valeur 3 doit être insérée en (0,0)");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            grille.validerEntree(0, 0, 5); // 5 est invalide dans une grille 4x4
        });

        assertEquals("Valeur invalide.", exception.getMessage(), "Une exception doit être levée pour une valeur hors limites");
    }

    @Test
    void testResolutionMultidokuBacktracking() {
        // Définir les blocs (2x2 pour une grille 4x4)
        int[][] blocs = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i < 2 && j < 2) blocs[i][j] = 1;
                else if (i < 2 && j >= 2) blocs[i][j] = 2;
                else if (i >= 2 && j < 2) blocs[i][j] = 3;
                else blocs[i][j] = 4;
            }
        }

        // Préparer une grille simple 4x4
        int[][] grilleInitiale = {
            {1, 0, 0, 4},
            {0, 0, 1, 0},
            {0, 1, 0, 0},
            {4, 0, 0, 1}
        };

        // Copier les valeurs dans la grille
        for (int i = 0; i < 4; i++) {
            System.arraycopy(grilleInitiale[i], 0, grille.getGrilleValeurs()[i], 0, 4);
        }

        // Créer et lancer le solveur MultidokuBacktracking
        MultidokuBacktracking backtracking = new MultidokuBacktracking(grille.getGrilleValeurs(), blocs);
        boolean resolu = backtracking.resoudreSudoku();
        int[][] grilleResolue = backtracking.getGrilleResolue();

        // Vérifier que la grille a été résolue
        assertTrue(resolu, "La grille devrait être résoluble par MultidokuBacktracking");
        
        // Vérifier que les valeurs initiales sont préservées
        assertEquals(1, grilleResolue[0][0]);
        assertEquals(4, grilleResolue[0][3]);
        assertEquals(4, grilleResolue[3][0]);
        assertEquals(1, grilleResolue[3][3]);

        // Vérifier que toutes les cases sont remplies avec des valeurs valides
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertTrue(grilleResolue[i][j] > 0 && grilleResolue[i][j] <= 4,
                    "Chaque case doit contenir une valeur entre 1 et 4");
            }
        }

        // Vérifier que les contraintes des blocs sont respectées
        for (int blocId = 1; blocId <= 4; blocId++) {
            boolean[] valeursUtilisees = new boolean[5]; // Index 0 non utilisé
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (blocs[i][j] == blocId) {
                        assertFalse(valeursUtilisees[grilleResolue[i][j]], 
                            "La valeur " + grilleResolue[i][j] + " est déjà utilisée dans le bloc " + blocId);
                        valeursUtilisees[grilleResolue[i][j]] = true;
                    }
                }
            }
        }
    }

    @Test
    void testResolutionDeduction() {
        // Définir les blocs (2x2 pour une grille 4x4)
        int[][] blocs = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i < 2 && j < 2) blocs[i][j] = 1;
                else if (i < 2 && j >= 2) blocs[i][j] = 2;
                else if (i >= 2 && j < 2) blocs[i][j] = 3;
                else blocs[i][j] = 4;
            }
        }

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

        // Vérifier que les valeurs déduites sont valides (si la déduction a trouvé des solutions)
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (grilleResolue[i][j] != 0) {
                    assertTrue(grilleResolue[i][j] > 0 && grilleResolue[i][j] <= 4,
                        "Chaque valeur déduite doit être entre 1 et 4");
                }
            }
        }
    }
}
