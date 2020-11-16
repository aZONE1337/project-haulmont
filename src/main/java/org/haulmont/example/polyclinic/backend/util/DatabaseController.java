package org.haulmont.example.polyclinic.backend.util;

import java.io.*;
import java.sql.*;

public class DatabaseController implements Closeable, AutoCloseable {
    private final Connection connection;
    private PreparedStatement ps;

    public DatabaseController() throws SQLException, ClassNotFoundException {
        connection = ConnectionSingleton.getConnection();
        ps = null;
    }

    public Connection getConnection() {
        return connection;
    }

    public PreparedStatement getPs() {
        return ps;
    }

    public boolean isConnectionActive() throws SQLException {
        return !connection.isClosed();
    }

    public boolean isPrepStatementActive() throws SQLException {
        if (ps == null)
            return false;

        return !ps.isClosed();
    }

    public boolean closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            ps = connection.prepareStatement("SHUTDOWN");
            ps.close();
            connection.close();

            return true;
        }

        return false;
    }

    public void initDatabase() throws SQLException, IOException {
        //init code here
    }

    public void doSQL(String sql) throws SQLException {
        //sql request logic here
    }

    @Override
    public void close() throws IOException {
        try {
            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
