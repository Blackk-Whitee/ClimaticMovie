package org.example.infrastructure.database;

import org.example.domain.models.Weather;
import java.sql.*;

public class SQLiteEventStorer {
    private final Connection connection;

    public SQLiteEventStorer(String dbUrl) throws SQLException {
        this.connection = DriverManager.getConnection(dbUrl);
        createTable();
    }

    private void createTable() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS weather_data (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                city TEXT NOT NULL,
                temperature REAL NOT NULL,
                humidity INTEGER NOT NULL,
                condition TEXT NOT NULL,
                wind_speed REAL NOT NULL,
                timestamp DATETIME NOT NULL
            )""";
        connection.createStatement().execute(sql);
    }

    public void saveWeather(Weather weather) throws SQLException {
        String sql = """
            INSERT INTO weather_data
            (city, temperature, humidity, condition, wind_speed, timestamp)
            VALUES (?, ?, ?, ?, ?, ?)""";

        PreparedStatement stmt = getPreparedStatement(weather, sql);

        stmt.executeUpdate();
    }

    private PreparedStatement getPreparedStatement(Weather weather, String sql) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, weather.getCity());
        stmt.setDouble(2, weather.getTemperature());
        stmt.setInt(3, weather.getHumidity());
        stmt.setString(4, weather.getCondition());
        stmt.setDouble(5, weather.getWindSpeed());
        stmt.setString(6, weather.getTimestamp().toString());
        return stmt;
    }
}