package infrastructure.db;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

class SQLiteConnectionTest {
    @Test
    void testGetConnection() throws SQLException {
        // Usamos base de datos en memoria para tests
        SQLiteConnection conn = new SQLiteConnection("jdbc:sqlite::memory:");

        // Probamos la inicialización
        assertDoesNotThrow(conn::initializeDatabase);

        // Probamos la conexión
        Connection dbConn = conn.getConnection();
        assertNotNull(dbConn);
        assertFalse(dbConn.isClosed());
        dbConn.close();
    }
}