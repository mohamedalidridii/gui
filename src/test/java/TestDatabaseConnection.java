import Util.DatabaseUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class TestDatabaseConnection {
    public static void main(String[] args) {
        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn != null) {
                System.out.println(" Connection to MySQL database was successful!");
            }
        } catch (SQLException e) {
            System.err.println("Failed to connect to MySQL database.");
            e.printStackTrace();
        }
    }
}
