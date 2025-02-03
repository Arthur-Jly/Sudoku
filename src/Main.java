import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class Main {
    private static final int[] TAILLES_VALIDES = {4, 9, 16, 25, 36, 49};

    public static void main(String[] args) {
        String taillesValides = Arrays.toString(TAILLES_VALIDES).replace("[", "").replace("]", "");

        String message = "<html>Entrez la taille de la grille<br>" +
                "Valeurs possibles : " + taillesValides + "<br>" +
                "(carrés parfaits pour les blocs)</html>";

        String tailleStr = JOptionPane.showInputDialog(null, message, "Taille de la grille", JOptionPane.QUESTION_MESSAGE);

        if (tailleStr == null) {
            System.exit(0);
        }

        try {
            int taille = Integer.parseInt(tailleStr);

            if (!estTailleValide(taille)) {
                JOptionPane.showMessageDialog(null, "La taille doit être un carré parfait parmi : " + taillesValides, "Taille invalide", JOptionPane.ERROR_MESSAGE);
                main(args);
                return;
            }

            Grille grille = new Grille(taille);
            grille.initialiserGrille();

            while (!grille.estValidee()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            int[][] grilleValeurs = grille.getGrilleValeurs();
            afficherMenuResolution(grilleValeurs, taille);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Veuillez entrer un nombre valide.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
            main(args);
        }
    }

    private static boolean estTailleValide(int taille) {
        return Arrays.stream(TAILLES_VALIDES).anyMatch(t -> t == taille);
    }

    private static void afficherMenuResolution(int[][] grilleValeurs, int taille) {
        JFrame frame = new JFrame("Choisir la méthode de résolution");
        frame.setSize(300, 150);
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton btnBacktracking = new JButton("Backtracking");
        JButton btnDeduction = new JButton("Déduction");

        btnBacktracking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Fermer la fenêtre
                lancerBacktracking(grilleValeurs, taille);
            }
        });

        btnDeduction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                lancerDeduction(grilleValeurs, taille);
            }
        });

        frame.add(btnBacktracking);
        frame.add(btnDeduction);
        frame.setVisible(true);
    }

    private static void lancerBacktracking(int[][] grilleValeurs, int taille) {
        Backtracking solveur = new Backtracking(grilleValeurs);
        if (!solveur.resoudreSudoku()) {
            System.out.println("\nLa grille n'est pas résolvable !");
        } else {
            System.out.println("\nGrille résolue par Backtracking :");
            afficherGrille(solveur.getGrilleResolue(), taille);
        }
    }

    private static void lancerDeduction(int[][] grilleValeurs, int taille) {
        Deduction solveur = new Deduction(grilleValeurs);
        if (!solveur.resoudreSudoku()) {
            System.out.println("\nLa grille n'est pas résolvable !");
        } else {
            System.out.println("\nGrille résolue par Déduction :");
            afficherGrille(solveur.getGrilleResolue(), taille);
        }
    }

    private static void afficherGrille(int[][] grille, int taille) {
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
