package infrastructure.db;

import core.Movie;
import java.sql.*;
import java.util.List;

public class SQLiteMovieRepository implements MovieRepository {
    private static final String DB_URL = DatabaseConfig.getConnectionUrl();

    @Override
    public void saveMovies(List<Movie> movies) {
        String sql = """
INSERT OR IGNORE INTO movies (id, title, release_date, vote_average, vote_count, genre_ids
    ) VALUES (?, ?, ?, ?, ?, ?)""";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            for (Movie movie : movies) {
                pstmt.setInt(1, movie.getId());
                pstmt.setString(2, movie.getTitle());
                pstmt.setString(3, movie.getReleaseDate());
                pstmt.setDouble(4, movie.getVoteAverage());
                pstmt.setInt(5, movie.getVoteCount());
                pstmt.setString(6, movie.getGenreIds().toString());
                pstmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}