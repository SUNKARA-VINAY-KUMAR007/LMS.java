import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Demonstrates inserting data into a MySQL database using JDBC's
 * PreparedStatement, creating the table first if it does not exist.
 *
 * Prerequisite (run in MySQL once):
 *
 *   CREATE DATABASE IF NOT EXISTS mydatabase;
 */
public class InsertUsers {

    // Step 2: connection constants (change to match your MySQL setup)
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/mydatabase";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "your_password";

    public static void main(String[] args) {

        String createTableSql =
                "CREATE TABLE IF NOT EXISTS users ("
                + "id INT PRIMARY KEY AUTO_INCREMENT, "
                + "name VARCHAR(50) NOT NULL, "
                + "email VARCHAR(100) NOT NULL)";

        String insertSql = "INSERT INTO users (name, email) VALUES (?, ?)";

        // Step 3: connection managed by try-with-resources
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {

            // Step 4: create the table if it does not exist
            try (PreparedStatement createStmt = connection.prepareStatement(createTableSql)) {
                createStmt.executeUpdate();
            }

            // Steps 5-7: insert two records using the same parameterized query
            try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {

                // First record
                insertStmt.setString(1, "John Doe");
                insertStmt.setString(2, "john.doe@example.com");
                insertStmt.executeUpdate();

                // Second record (parameters are replaced and the statement is reused)
                insertStmt.setString(1, "Jane Doe");
                insertStmt.setString(2, "jane.doe@example.com");
                insertStmt.executeUpdate();
            }

            // Step 8: confirmation message
            System.out.println("Data inserted successfully.");

        } catch (SQLException e) {
            // Step 9: handle database errors
            // (SQLIntegrityConstraintViolationException, thrown on duplicate keys,
            //  is a subclass of SQLException and is caught here too)
            System.err.println("Database error: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState()
                    + ", Error Code: " + e.getErrorCode());
        }
    }
}