import org.junit.jupiter.api.Test;

import Class.Deduction;
import Class.Grille;
import Class.MultidokuBacktracking;
import Class.MultidokuModeGraphique;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import javax.swing.*;

class MultidokuModeGraphiqueTest {
    private MultidokuModeGraphique multidoku;
    private Grille grille;

    @BeforeEach
    void setUp() {
        grille = new Grille(4); // On utilise une grille 4x4 pour simplifier les tests
        multidoku = new MultidokuModeGraphique(grille); 
        multidoku.blocs = new int[4][4]; 

        // Vérification que tous les composants sont bien initialisés
        assertNotNull(multidoku, "multidoku n'a pas été initialisé !");
        assertNotNull(multidoku.boutonsBlocs, "boutonsBlocs n'a pas été initialisé !");
        assertNotNull(multidoku.blocs, "blocs n'a pas été initialisé !");
        assertNotNull(multidoku.champsTexte, "champsTexte n'a pas été initialisé !");
    }

    @Test
    void testInitialisation() {
        assertNotNull(multidoku.boutonsBlocs);
        assertNotNull(multidoku.blocs);
        assertEquals(4, multidoku.boutonsBlocs.length);
        assertEquals(4, multidoku.blocs.length);
        assertEquals(1, multidoku.blocActuel);
        assertTrue(multidoku.modeDefinitionBlocs);
    }

    @Test
    void testTousBlocksDefinis() {
        // Au départ, aucun bloc n'est défini
        assertFalse(multidoku.tousBlocksDefinis());

        // Définir tous les blocs à 1
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                multidoku.blocs[i][j] = 1;
            }
        }
        assertTrue(multidoku.tousBlocksDefinis());
    }

    @Test
    void testVerifierLigne() {
        // Test avec une ligne valide
        multidoku.champsTexte[0][0].setText("1");
        multidoku.champsTexte[0][1].setText("2");
        multidoku.champsTexte[0][2].setText("3");
        multidoku.champsTexte[0][3].setText("4");
        assertTrue(multidoku.verifierLigne(0));

        // Test avec une ligne invalide (nombre en double)
        multidoku.champsTexte[0][3].setText("1");
        assertFalse(multidoku.verifierLigne(0));
    }

    @Test
    void testVerifierColonne() {
        // Test avec une colonne valide
        multidoku.champsTexte[0][0].setText("1");
        multidoku.champsTexte[1][0].setText("2");
        multidoku.champsTexte[2][0].setText("3");
        multidoku.champsTexte[3][0].setText("4");
        assertTrue(multidoku.verifierColonne(0));

        // Test avec une colonne invalide (nombre en double)
        multidoku.champsTexte[3][0].setText("1");
        assertFalse(multidoku.verifierColonne(0));
    }

    @Test
    void testVerifierBloc() {
        // Définir un bloc
        multidoku.blocs[0][0] = 1;
        multidoku.blocs[0][1] = 1;
        multidoku.blocs[1][0] = 1;
        multidoku.blocs[1][1] = 1;

        // Test avec un bloc valide
        multidoku.champsTexte[0][0].setText("1");
        multidoku.champsTexte[0][1].setText("2");
        multidoku.champsTexte[1][0].setText("3");
        multidoku.champsTexte[1][1].setText("4");
        assertTrue(multidoku.verifierBloc(1));

        // Test avec un bloc invalide (nombre en double)
        multidoku.champsTexte[1][1].setText("1");
        assertFalse(multidoku.verifierBloc(1));
    }

    @Test
    void testValiderEntree() {
        multidoku.modeDefinitionBlocs = false;
        multidoku.blocs[0][0] = 1;  // Définir le bloc

        // Test avec une entrée valide
        JTextField champ = multidoku.champsTexte[0][0];
        champ.setText("3");
        multidoku.validerEntree(champ, 0, 0);
        assertEquals("3", champ.getText());

        // Test avec une entrée invalide (hors limites)
        champ.setText("5"); // Invalide pour une grille 4x4
        multidoku.validerEntree(champ, 0, 0);
        assertEquals("", champ.getText());
    }

    @Test
    void testLancerResolutionBacktracking() {
        // Définir les blocs d'abord (2x2 pour une grille 4x4)
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i < 2 && j < 2) multidoku.blocs[i][j] = 1;
                else if (i < 2 && j >= 2) multidoku.blocs[i][j] = 2;
                else if (i >= 2 && j < 2) multidoku.blocs[i][j] = 3;
                else multidoku.blocs[i][j] = 4;
            }
        }
        multidoku.modeDefinitionBlocs = false;

        // Préparer une grille simple
        multidoku.champsTexte[0][0].setText("1");
        multidoku.champsTexte[0][1].setText("2");
        multidoku.champsTexte[1][0].setText("3");

        multidoku.recupererValeursInterface();
        
        // Lancer la résolution avec MultidokuBacktracking
        MultidokuBacktracking backtracking = new MultidokuBacktracking(grille.getGrilleValeurs(), multidoku.blocs);
        multidoku.lancerResolution(backtracking);

        // Vérifier que les valeurs initiales sont préservées
        assertEquals("1", multidoku.champsTexte[0][0].getText());
        assertEquals("2", multidoku.champsTexte[0][1].getText());
        assertEquals("3", multidoku.champsTexte[1][0].getText());

        // Vérifier que d'autres cases ont été remplies
        assertFalse(multidoku.champsTexte[1][1].getText().isEmpty());
        assertFalse(multidoku.champsTexte[2][0].getText().isEmpty());
    }

    @Test
    void testLancerResolutionDeduction() {
        // Définir les blocs
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i < 2 && j < 2) multidoku.blocs[i][j] = 1;
                else if (i < 2 && j >= 2) multidoku.blocs[i][j] = 2;
                else if (i >= 2 && j < 2) multidoku.blocs[i][j] = 3;
                else multidoku.blocs[i][j] = 4;
            }
        }
        multidoku.modeDefinitionBlocs = false;

        // Préparer une grille simple
        multidoku.champsTexte[0][0].setText("1");
        multidoku.champsTexte[0][1].setText("2");
        multidoku.champsTexte[1][0].setText("3");

        multidoku.recupererValeursInterface();
        
        // Lancer la résolution
        Deduction deduction = new Deduction(grille.getGrilleValeurs());
        multidoku.lancerResolution(deduction);

        // Vérifier que les valeurs initiales sont préservées
        assertEquals("1", multidoku.champsTexte[0][0].getText());
        assertEquals("2", multidoku.champsTexte[0][1].getText());
        assertEquals("3", multidoku.champsTexte[1][0].getText());
    }
}

