import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {

    private static PrintWriter writer;

    // Méthode pour initialiser le logger
    public static void init() throws IOException {
        // Assurez-vous que ce chemin de fichier est valide pour votre système
        writer = new PrintWriter(new FileWriter("log.txt", true)); // Ouvrir en mode append
    }

    // Méthode pour loguer des messages
    public static void log(String message) {
        if (writer != null) {
            writer.println(message);
            writer.flush(); // S'assurer que le message est écrit immédiatement
        }
    }

    // Méthode pour fermer le logger
    public static void close() {
        if (writer != null) {
            writer.close();
        }
    }
}
