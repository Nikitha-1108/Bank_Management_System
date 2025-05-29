package Bank_GUI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection getConnection() {
        Connection conn = null;
        
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish connection
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/unisoft",  // Database URL
                "root",                                // MySQL username
                "Nikitha@11"                           // MySQL password
            );
            
            System.out.println("Connection established successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL Error: Unable to establish a connection.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error: MySQL JDBC driver not found.");
        }

        return conn;
    }
}
