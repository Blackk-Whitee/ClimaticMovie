package infrastructure.db;
import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseConfig {
    private static final Dotenv dotenv = Dotenv.load();

    public static String getConnectionUrl() {
        return dotenv.get("DB_URL"); // Lee del archivo .env
    }
}