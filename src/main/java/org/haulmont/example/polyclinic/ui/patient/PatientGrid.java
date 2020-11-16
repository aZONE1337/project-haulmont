package org.haulmont.example.polyclinic.ui.patient;

import com.vaadin.flow.component.grid.Grid;
import org.haulmont.example.polyclinic.backend.entity.Patient;
import org.haulmont.example.polyclinic.backend.service.PatientService;

import java.sql.SQLException;

public class PatientGrid extends Grid<Patient> {
    private final PatientService patientService = PatientService.getInstance();

    public PatientGrid() throws SQLException, ClassNotFoundException {
        addColumn(Patient::getMiddleName).setHeader("Фамилия").setSortable(true).setAutoWidth(true);
        addColumn(Patient::getFirstName).setHeader("Имя").setSortable(true).setAutoWidth(true);
        addColumn(Patient::getLastName).setHeader("Отчество").setSortable(true).setAutoWidth(true);
        addColumn(Patient::getPhone).setHeader("Номер телефона").setSortable(true).setAutoWidth(true);

        setSizeFull();

        updateGrid();
    }

    public void updateGrid() throws SQLException {
        setItems(patientService.getAll());
    }

    public void updateGridFiltered(String midNameFilter) throws SQLException {
        setItems(patientService.getAll(midNameFilter));
    }
}
