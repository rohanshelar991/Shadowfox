package com.shadowfox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DATABASE_URL = "jdbc:sqlite:library.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            // db parameters
            // create a connection to the database
            conn = DriverManager.getConnection(DATABASE_URL);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void createNewDatabase() {
        try (Connection conn = connect()) {
            if (conn != null) {
                System.out.println("A new database has been created or connected at " + DATABASE_URL);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createTables() {
        // SQL statement for creating a new books table
        String sqlBooks = "CREATE TABLE IF NOT EXISTS books (\n" +
                          "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                          "    title TEXT NOT NULL,\n" +
                          "    author TEXT NOT NULL,\n" +
                          "    isbn TEXT UNIQUE NOT NULL,\n" +
                          "    genre TEXT,\n" +
                          "    available INTEGER DEFAULT 1\n" +
                          ");";

        // SQL statement for creating a new users table
        String sqlUsers = "CREATE TABLE IF NOT EXISTS users (\n" +
                          "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                          "    username TEXT UNIQUE NOT NULL,\n" +
                          "    password TEXT NOT NULL,\n" +
                          "    role TEXT DEFAULT 'user'\n" +
                          ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            // create new tables
            stmt.execute(sqlBooks);
            stmt.execute(sqlUsers);
            System.out.println("Tables 'books' and 'users' created successfully (if they didn't exist).");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    // Main method for testing database connection and table creation (can be removed later)
    public static void main(String[] args) {
        createNewDatabase();
        createTables();
    }
} 