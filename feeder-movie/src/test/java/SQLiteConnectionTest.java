import infrastructure.db.SQLiteConnection;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class SQLiteConnectionTest {

    // Verifica que la base de datos se inicialice correctamente con las tablas requeridas
    @Test
    void initializeDatabase_shouldCreateTables() throws Exception {
        // Ejecutar
        SQLiteConnection.initializeDatabase();

        // Verificar
        try (Connection conn = SQLiteConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table'")) {

            int tableCount = 0;
            while (rs.next()) {
                String tableName = rs.getString("name");
                assertTrue(tableName.equals("genres") || tableName.equals("movies"));
                tableCount++;
            }
            assertEquals(2, tableCount); // Deben existir 2 tablas
        }
    }

    // Confirma que se puede establecer una conexión válida a la base de datos
    @Test
    void getConnection_shouldReturnValidConnection() throws Exception {
        Connection conn = SQLiteConnection.getConnection();
        assertFalse(conn.isClosed());
        conn.close();
    }
}