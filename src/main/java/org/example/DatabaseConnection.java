package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Database Properties
    private static final String HOST = "localhost";
    private static final String PORT = "5432";
    private static final String DATABASE = "your_database";
    private static final String URL = String.format("jdbc:postgresql://%s:%s/%s", HOST, PORT, DATABASE);
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";

    // Get database connection
    public static Connection getConnection() throws SQLException {
//        System.out.println(Thread.currentThread().getName() + " DB Connection Successful");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
