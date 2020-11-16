package org.haulmont.example.polyclinic.backend.service;

import org.haulmont.example.polyclinic.backend.entity.Patient;
import org.haulmont.example.polyclinic.backend.entity.Priority;
import org.haulmont.example.polyclinic.backend.entity.Receipt;
import org.haulmont.example.polyclinic.backend.util.ConnectionSingleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReceiptService implements AbstractService<Receipt, Long> {
    private static ReceiptService instance;
    private final Connection connection;
    private final DoctorService docService = DoctorService.getInstance();
    private final PatientService patientService = PatientService.getInstance();
    private PreparedStatement ps;

    public ReceiptService() throws SQLException, ClassNotFoundException {
        connection = ConnectionSingleton.getConnection();
        ps = null;
    }

    public static synchronized ReceiptService getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null) {
            return new ReceiptService();
        }
        return instance;
    }

    @Override
    public Receipt get(Long id) throws SQLException {
        Receipt toReturn = new Receipt();
        int rowsCount = 0;

        ps = connection.prepareStatement("SELECT * FROM receipt WHERE id = ?");

        ps.setLong(1, id);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            toReturn = createReceiptObj(rs);
            rowsCount++;
        }

        if (rowsCount > 1) {
            throw new RuntimeException("Returned more than 1 rows");
        }

        ps.clearParameters();

        return toReturn;
    }

    //method not specified
    @Override
    public List<Receipt> getAll(String midNameFilter) throws SQLException {
        return null;
    }

    @Override
    public List<Receipt> getAll() throws SQLException {
        List<Receipt> receipts = new ArrayList<>();

        ps = connection.prepareStatement("SELECT * FROM receipt");

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            receipts.add(createReceiptObj(rs));
        }

        ps.clearParameters();

        return receipts;
    }

    public List<Receipt> getAllFiltered(String midName, String description, Priority priority) throws SQLException {
        List<Receipt> receipts = new ArrayList<>();

        ps = connection.prepareStatement("SELECT * FROM receipt " +
                "WHERE patient_id = " +
                "(SELECT id FROM patient " +
                    "WHERE middle_name LIKE ?) " +
                "AND description LIKE ? " +
                "AND priority LIKE ?");

        ps.setString(1, "%" + midName + "%");
        ps.setString(2, "%" + description + "%");
        ps.setString(3, "%" + priority + "%");

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            receipts.add(createReceiptObj(rs));
        }

        ps.clearParameters();

        return receipts;
    }

    @Override
    public boolean update(Long id, Receipt entity) throws SQLException {
        ps = connection.prepareStatement("UPDATE receipt " +
                "SET description = ?, " +
                "patient_id = ?, " +
                "doctor_id = ?, " +
                "date = ?, " +
                "expiration_date = ?, " +
                "priority = ? " +
                "WHERE id = ?");

        setParameters(entity, ps);
        ps.setLong(7, id);

        if (ps.executeUpdate() != 1) {
            throw new RuntimeException("Changed more than 1 rows");
        }

        ps.clearParameters();

        return true;
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        ps = connection.prepareStatement("DELETE FROM receipt WHERE id = ?");

        ps.setLong(1, id);

        if (ps.executeUpdate() != 1) {
            throw new RuntimeException("Deleted more than 1 rows");
        }

        ps.clearParameters();

        return true;
    }

    //method not specified
    @Override
    public boolean hasRelatedReceipt(Long id) throws SQLException {
        return false;
    }

    @Override
    public boolean create(Receipt entity) throws SQLException {
        ps = connection.prepareStatement("INSERT INTO receipt(description, patient_id, doctor_id, date, expiration_date, priority) " +
                "VALUES (?, ?, ?, ?, ?, ?)");

        setParameters(entity, ps);

        if (ps.executeUpdate() != 1) {
            throw new RuntimeException("Inserted more than 1 rows");
        }

        ps.clearParameters();

        return true;
    }

    private void setParameters(Receipt entity, PreparedStatement ps) throws SQLException {
        ps.setString(1, entity.getDescription());
        ps.setLong(2, entity.getPatient().getId());
        ps.setLong(3, entity.getDoctor().getId());
        ps.setDate(4, entity.getDate());
        ps.setDate(5, entity.getExpirationDate());
        ps.setString(6, entity.getPriority().toString());
    }

    private Receipt createReceiptObj(ResultSet rs) throws SQLException {
        Receipt receipt = new Receipt();

        receipt.setId(rs.getLong(1));
        receipt.setDescription(rs.getString(2));
        receipt.setPatient(patientService.get(rs.getLong(3)));
        receipt.setDoctor(docService.get(rs.getLong(4)));
        receipt.setDate(Date.valueOf(rs.getString(5)));
        receipt.setExpirationDate(Date.valueOf(rs.getString(6)));
        receipt.setPriority(Priority.getByValue(rs.getString(7)));

        return receipt;
    }
}