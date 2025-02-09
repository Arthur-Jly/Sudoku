import org.junit.jupiter.api.*;

import Class.Backtracking;
import Class.Deduction;
import Class.Grille;
import Class.SudokuModeGraphique;

import static org.junit.jupiter.api.Assertions.*;
import javax.swing.*;
import java.awt.Color;

class SudokuModeGraphiqueTest {
    private SudokuModeGraphique sudoku;
    private Grille grille;
    private static final int TAILLE = 9;

    @BeforeEach
    void setUp() {
        grille = new Grille(TAILLE);
        sudoku = new SudokuModeGraphique(grille);
    }

    @Test
    void testInitialisation() {
        assertNotNull(sudoku);
        assertEquals(TAILLE, sudoku.taille);
        assertEquals(3, sudoku.tailleBloc); // sqrt(9)
        assertNotNull(sudoku.champsTexte);
        assertEquals(TAILLE, sudoku.champsTexte.length);
        assertEquals(TAILLE, sudoku.champsTexte[0].length);
    }

    @Test
    void testValiderEntreeValide() {
        JTextField champ = new JTextField();
        champ.setText("5");
        sudoku.validerEntree(champ, 0, 0);
        
        assertEquals("5", champ.getText());
        assertEquals(Color.BLUE, champ.getForeground());
        assertEquals(5, grille.getGrilleValeurs()[0][0]);
    }

    @Test
    void testValiderEntreeInvalideHorsLimites() {
        JTextField champ = new JTextField();
        champ.setText("10"); // Valeur invalide pour une grille 9x9
        
        sudoku.validerEntree(champ, 0, 0);
        assertEquals("", champ.getText());
        assertEquals(0, grille.getGrilleValeurs()[0][0]);
    }

    @Test
    void testValiderEntreeNonNumerique() {
        JTextField champ = new JTextField();
        champ.setText("abc");
        
        sudoku.validerEntree(champ, 0, 0);
        assertEquals("", champ.getText());
        assertEquals(0, grille.getGrilleValeurs()[0][0]);
    }

    @Test
    void testRecupererValeursInterface() {
        // Remplir quelques champs avec des valeurs valides
        sudoku.champsTexte[0][0].setText("1");
        sudoku.champsTexte[1][1].setText("2");
        sudoku.champsTexte[2][2].setText("3");

        sudoku.recupererValeursInterface();

        assertEquals(1, grille.getGrilleValeurs()[0][0]);
        assertEquals(2, grille.getGrilleValeurs()[1][1]);
        assertEquals(3, grille.getGrilleValeurs()[2][2]);
    }

    @Test
    void testAfficherGrilleGraphique() {
        int[][] grilleTest = new int[TAILLE][TAILLE];
        // Remplir la grille test avec des valeurs
        for (int i = 0; i < TAILLE; i++) {
            for (int j = 0; j < TAILLE; j++) {
                grilleTest[i][j] = (i + j) % TAILLE + 1;
            }
        }

        sudoku.afficherGrilleGraphique(grilleTest);

        // Vérifier que les valeurs sont correctement affichées
        for (int i = 0; i < TAILLE; i++) {
            for (int j = 0; j < TAILLE; j++) {
                assertEquals(String.valueOf(grilleTest[i][j]), 
                           sudoku.champsTexte[i][j].getText());
                assertEquals(Color.BLACK, 
                           sudoku.champsTexte[i][j].getForeground());
            }
        }
    }

    @Test
    void testLancerResolutionBacktracking() {
        // Préparer une grille simple
        sudoku.champsTexte[0][0].setText("5");
        sudoku.champsTexte[0][1].setText("3");
        sudoku.champsTexte[0][2].setText("4");

        sudoku.recupererValeursInterface();
        
        // Lancer la résolution
        Backtracking backtracking = new Backtracking(grille.getGrilleValeurs());
        sudoku.lancerResolution(backtracking);

        // Vérifier que les valeurs initiales sont préservées
        assertEquals("5", sudoku.champsTexte[0][0].getText());
        assertEquals("3", sudoku.champsTexte[0][1].getText());
        assertEquals("4", sudoku.champsTexte[0][2].getText());

        // Vérifier que d'autres cases ont été remplies
        assertFalse(sudoku.champsTexte[1][0].getText().isEmpty());
        assertFalse(sudoku.champsTexte[1][1].getText().isEmpty());
    }

    @Test
    void testLancerResolutionDeduction() {
        // Préparer une grille simple
        sudoku.champsTexte[0][0].setText("5");
        sudoku.champsTexte[0][1].setText("3");
        sudoku.champsTexte[0][2].setText("4");

        sudoku.lancerResolution(new Deduction(grille.getGrilleValeurs()));

        // Vérifier l'état après la tentative de résolution
        assertNotNull(sudoku.champsTexte[0][0].getText());
    }

    @AfterEach
    void tearDown() {
        sudoku.dispose();
    }
}