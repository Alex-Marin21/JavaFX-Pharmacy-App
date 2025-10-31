package com.example.farmaciebdproiect;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String connectionUrl =
            "jdbc:sqlserver://localhost\\SQLEXPRESS;" +
                    "databaseName=FarmacieDB;" +
                    "user=UtilizatorJava;" +
                    "password=Panda51;" +
                    "encrypt=true;" +
                    "trustServerCertificate=true;";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionUrl);
    }
}