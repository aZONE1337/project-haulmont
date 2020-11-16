package org.haulmont.example.polyclinic.backend.service;

import org.haulmont.example.polyclinic.backend.entity.Doctor;
import org.haulmont.example.polyclinic.backend.util.ConnectionSingleton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DoctorService implements AbstractService<Doctor, Long> {
    private static DoctorService instance;
    private final Connection connection;
    private PreparedStatement ps;

    public DoctorService() throws SQLException, ClassNotFoundException {
        connection = ConnectionSingleton.getConnection();
        ps = null;
    }

    public static synchronized DoctorService getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null) {
            return new DoctorService();
        }
        return instance;
    }

    @Override
    public Doctor get(Long id) throws SQLException {
        Doctor toReturn = new Doctor();

        int rowsCount = 0;

        ps = connection.prepareStatement("SELECT * FROM doctor WHERE id = ?");

        ps.setLong(1, id);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            toReturn = getDoctor(rs);
            rowsCount++;
        }

        if (rowsCount > 1) {
            throw new RuntimeException("Returned more than 1 rows");
        }

        ps.clearParameters();

        return toReturn;
    }

    public Doctor getByFullname(String fullname) throws SQLException {
        String[] separated = fullname.split(" ");
        Doctor toReturn = new Doctor();
        int rowsCount = 0;

        ps = connection.prepareStatement("SELECT * FROM doctor " +
                "WHERE middle_name = ? " +
                "AND first_name = ? " +
                "AND last_name = ?");

        ps.setString(1, separated[0]);
        ps.setString(2, separated[1]);
        ps.setString(3, separated[2]);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            toReturn = getDoctor(rs);
            rowsCount++;
        }

        if (rowsCount > 1) {
            throw new RuntimeException("Returned more than 1 rows");
        }

        return toReturn;
    }

    @Override
    public List<Doctor> getAll() throws SQLException {
        List<Doctor> doctors = new ArrayList<>();

        ps = connection.prepareStatement("SELECT * FROM doctor");

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            doctors.add(getDoctor(rs));
        }

        ps.clearParameters();

        return doctors;
    }

    @Override
    public List<Doctor> getAll(String midNameFilter) throws SQLException {
        List<Doctor> doctors = new ArrayList<>();

        ps = connection.prepareStatement("SELECT * FROM doctor WHERE middle_name LIKE ?");

        ps.setString(1, "%" + midNameFilter + "%");

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            doctors.add(getDoctor(rs));
        }

        ps.clearParameters();

        return doctors;
    }

    @Override
    public boolean update(Long id, Doctor entity) throws SQLException {
        ps = connection.prepareStatement("UPDATE doctor " +
                "SET first_name = ?, " +
                "middle_name = ?, " +
                "last_name = ?, " +
                "specialization = ? " +
                "WHERE id = ?");

        ps.setString(1, entity.getFirstName());
        ps.setString(2, entity.getMiddleName());
        ps.setString(3, entity.getLastName());
        ps.setString(4, entity.getSpecialization());
        ps.setLong(5, id);

        if (ps.executeUpdate() != 1) {
            throw new RuntimeException("Changed more than 1 rows");
        }

        ps.clearParameters();

        return true;
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        ps = connection.prepareStatement("DELETE FROM doctor WHERE id = ?");

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
                "WHERE doctor_id = ?");

        ps.setLong(1, id);

        ResultSet rs = ps.executeQuery();

        return rs.next();
    }

    @Override
    public boolean create(Doctor entity) throws SQLException {
        ps = connection.prepareStatement("INSERT INTO doctor(first_name, middle_name, last_name, specialization) " +
                "VALUES (?, ?, ?, ?)");

        ps.setString(1, entity.getFirstName());
        ps.setString(2, entity.getMiddleName());
        ps.setString(3, entity.getLastName());
        ps.setString(4, entity.getSpecialization());

        if (ps.executeUpdate() != 1) {
            throw new RuntimeException("Inserted more than 1 rows");
        }

        ps.clearParameters();

        return true;
    }

    private Doctor getDoctor(ResultSet rs) throws SQLException {
        Doctor toReturn = new Doctor();

        toReturn.setId(rs.getLong(1));
        toReturn.setFirstName(rs.getString(2));
        toReturn.setMiddleName(rs.getString(3));
        toReturn.setLastName(rs.getString(4));
        toReturn.setSpecialization(rs.getString(5));

        return toReturn;
    }
}
