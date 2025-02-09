import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Classe utilitaire pour la gestion des logs.
 */
public class Logger {

    private static PrintWriter writer;

    /**
     * Initialise le logger.
     *
     * @throws IOException si une erreur d'entrée/sortie se produit.
     */
    public static void init() throws IOException {
        // Assurez-vous que ce chemin de fichier est valide pour votre système
        writer = new PrintWriter(new FileWriter("log.txt", true)); // Ouvrir en mode append
    }

    /**
     * Logue un message.
     *
     * @param message Le message à loguer.
     */
    public static void log(String message) {
        if (writer != null) {
            writer.println(message);
            writer.flush(); // S'assurer que le message est écrit immédiatement
        }
    }

    /**
     * Ferme le logger.
     */
    public static void close() {
        if (writer != null) {
            writer.close();
        }
    }
}