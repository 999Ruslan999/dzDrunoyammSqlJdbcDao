package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {
    private static final String URL_KEY = "jdbc:postgresql://localhost:5433/BrunnoyammSQL";
    private static final String USERNAME_KEY = "postgres";
    private static final String PASSWORD_KEY = "postgres";

    private ConnectionManager() {
    }

    public static Connection open() {
        try {
            return DriverManager.getConnection(PropertiesUtil.get(URL_KEY),
                    PropertiesUtil.get(USERNAME_KEY),
                    PropertiesUtil.get(PASSWORD_KEY));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}