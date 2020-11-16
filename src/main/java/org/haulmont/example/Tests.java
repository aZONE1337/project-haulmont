package org.haulmont.example;

import org.haulmont.example.polyclinic.backend.util.DatabaseController;

import java.io.IOException;
import java.sql.SQLException;

public class Tests {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        DatabaseController dbController = new DatabaseController();
        //Проблемы с user friendly на моментах неправильного ввода
        dbController.closeConnection();
    }
}
