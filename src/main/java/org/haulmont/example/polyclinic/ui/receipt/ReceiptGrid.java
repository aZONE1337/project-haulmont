package org.haulmont.example.polyclinic.ui.receipt;

import com.vaadin.flow.component.grid.Grid;
import org.haulmont.example.polyclinic.backend.entity.Doctor;
import org.haulmont.example.polyclinic.backend.entity.Patient;
import org.haulmont.example.polyclinic.backend.entity.Priority;
import org.haulmont.example.polyclinic.backend.entity.Receipt;
import org.haulmont.example.polyclinic.backend.service.ReceiptService;

import java.sql.SQLException;

public class ReceiptGrid extends Grid<Receipt> {
    private final ReceiptService receiptService = ReceiptService.getInstance();

    public ReceiptGrid() throws SQLException, ClassNotFoundException {
        addColumn(Receipt::getDescription).setHeader("Описание").setSortable(true).setAutoWidth(true);

        addColumn(receipt -> {
            Patient patient = receipt.getPatient();
            return patient == null ? "-" :
                    patient.getMiddleName()
                            + " " + patient.getFirstName()
                            + " " + patient.getLastName();
        }).setHeader("Пацент").setSortable(true).setAutoWidth(true);

        addColumn(receipt -> {
            Doctor doctor = receipt.getDoctor();
            return doctor == null ? "-" :
                    doctor.getMiddleName()
                    + " " + doctor.getFirstName()
                    + " " + doctor.getLastName();
        }).setHeader("Врач").setSortable(true).setAutoWidth(true);

        addColumn(Receipt::getDate).setHeader("Дата назначения").setSortable(true).setAutoWidth(true);
        addColumn(Receipt::getExpirationDate).setHeader("Дата окончания").setSortable(true).setAutoWidth(true);
        addColumn(Receipt::getPriority).setHeader("Приоритет").setSortable(true).setAutoWidth(true);

        setSizeFull();

        updateGrid();
    }

    public void updateGrid() throws SQLException {
        setItems(receiptService.getAll());
    }

    public void updateGridFiltered(String midName, String desc, Priority priority) throws SQLException {
        setItems(receiptService.getAllFiltered(midName, desc, priority));
    }
}
