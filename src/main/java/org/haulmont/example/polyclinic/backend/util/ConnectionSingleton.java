package org.haulmont.example.polyclinic.backend.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ConnectionSingleton {
    private static Connection instance;

    public static synchronized Connection getConnection() throws ClassNotFoundException, SQLException {
        if (instance == null) {
            ResourceBundle resource = ResourceBundle.getBundle("database");
            String driver = resource.getString("db.driver");
            String url = resource.getString("db.url");
            String username = resource.getString("db.username");
            String password = resource.getString("db.password");

            Class.forName(driver);
            instance = DriverManager.getConnection(url, username, password);
        }
        return instance;
    }
}
