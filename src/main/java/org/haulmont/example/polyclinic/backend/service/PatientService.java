package org.haulmont.example.polyclinic.backend.service;

import org.haulmont.example.polyclinic.backend.entity.Patient;
import org.haulmont.example.polyclinic.backend.util.ConnectionSingleton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatientService implements AbstractService<Patient, Long> {
    private static PatientService instance;
    private final Connection connection;
    private PreparedStatement ps;

    public PatientService() throws SQLException, ClassNotFoundException {
        connection = ConnectionSingleton.getConnection();
        ps = null;
    }

    public static synchronized PatientService getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null) {
            return new PatientService();
        }
        return instance;
    }

    @Override
    public Patient get(Long id) throws SQLException {
        Patient toReturn = new Patient();
        int rowsCount = 0;

        ps = connection.prepareStatement("SELECT * FROM patient WHERE id = ?");

        ps.setLong(1, id);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            toReturn = getPatient(rs);
            rowsCount++;
        }

        if (rowsCount > 1) {
            throw new RuntimeException("Returned more than 1 rows");
        }

        ps.clearParameters();

        return toReturn;
    }

    public Patient getByFullName(String fullname) throws SQLException {
        String[] separated = fullname.split(" ");   //middle name -> first name -> last name
        Patient toReturn = new Patient();
        int rowsCount = 0;

        ps = connection.prepareStatement("SELECT * FROM patient " +
                "WHERE middle_name = ? " +
                "AND first_name = ? " +
                "AND last_name = ?");

        ps.setString(1, separated[0]);
        ps.setString(2, separated[1]);
        ps.setString(3, separated[2]);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            toReturn = getPatient(rs);
            rowsCount++;
        }

        if (rowsCount > 1) {
            throw new RuntimeException("Returned more than 1 rows");
        }

        ps.clearParameters();

        return toReturn;
    }

    @Override
    public List<Patient> getAll() throws SQLException {
        List<Patient> patients = new ArrayList<>();

        ps = connection.prepareStatement("SELECT * FROM patient");

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            patients.add(getPatient(rs));
        }

        ps.clearParameters();

        return patients;
    }

    @Override
    public List<Patient> getAll(String midNameFilter) throws SQLException {
        List<Patient> patients = new ArrayList<>();

        ps = connection.prepareStatement("SELECT * FROM patient WHERE middle_name LIKE ?");

        ps.setString(1, "%" + midNameFilter + "%");

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            patients.add(getPatient(rs));
        }

        ps.clearParameters();

        return patients;
    }

    @Override
    public boolean update(Long id, Patient entity) throws SQLException {
        ps = connection.prepareStatement("UPDATE patient " +
                "SET first_name = ?, " +
                "middle_name = ?, " +
                "last_name = ?, " +
                "phone = ? " +
                "WHERE id = ?");

        ps.setString(1, entity.getFirstName());
        ps.setString(2, entity.getMiddleName());
        ps.setString(3, entity.getLastName());
        ps.setString(4, entity.getPhone());
        ps.setLong(5, id);

        if (ps.executeUpdate() != 1) {
            throw new RuntimeException("Changed more than 1 rows");
        }

        ps.clearParameters();

        return true;
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        ps = connection.prepareStatement("DELETE FROM patient WHERE id = ?");

        ps.setLong(1, id);

        if (ps.executeUpdate() != 1) {
            throw new RuntimeException("Deleted more than 1 rows");
        }

        ps.clearParameters();

        return true;
    }

    @Override
    public boolean hasRelatedReceipt(Long id) throws SQLException {
        ps = connection.prepareStatement("SELECT id FROM receipt " +
                "WHERE patient_id = ?");

        ps.setLong(1, id);

        ResultSet rs = ps.executeQuery();

        return rs.next();
    }

    @Override
    public boolean create(Patient entity) throws SQLException {
        ps = connection.prepareStatement("INSERT INTO patient(first_name, middle_name, last_name, phone) " +
                "VALUES (?, ?, ?, ?)");

        ps.setString(1, entity.getFirstName());
        ps.setString(2, entity.getMiddleName());
        ps.setString(3, entity.getLastName());
        ps.setString(4, entity.getPhone());

        if (ps.executeUpdate() != 1) {
            throw new RuntimeException("Inserted more than 1 rows");
        }

        ps.clearParameters();

        return true;
    }

    private Patient getPatient(ResultSet rs) throws SQLException {
        Patient toReturn = new Patient();

        toReturn.setId(rs.getLong(1));
        toReturn.setFirstName(rs.getString(2));
        toReturn.setMiddleName(rs.getString(3));
        toReturn.setLastName(rs.getString(4));
        toReturn.setPhone(rs.getString(5));

        return toReturn;
    }
}
