package org.haulmont.example.polyclinic.ui.doctor;

import com.vaadin.flow.component.grid.Grid;
import org.haulmont.example.polyclinic.backend.entity.Doctor;
import org.haulmont.example.polyclinic.backend.service.DoctorService;

import java.sql.SQLException;

public class DoctorGrid extends Grid<Doctor> {
    private final DoctorService doctorService = DoctorService.getInstance();

    public DoctorGrid() throws SQLException, ClassNotFoundException {
        addColumn(Doctor::getMiddleName).setHeader("Фамилия").setSortable(true).setAutoWidth(true);
        addColumn(Doctor::getFirstName).setHeader("Имя").setSortable(true).setAutoWidth(true);
        addColumn(Doctor::getLastName).setHeader("Отчество").setSortable(true).setAutoWidth(true);
        addColumn(Doctor::getSpecialization).setHeader("Специальность").setSortable(true).setAutoWidth(true);

        setSizeFull();

        updateGrid();
    }

    public void updateGrid() throws SQLException {
        setItems(doctorService.getAll());
    }

    public void updateGridFiltered(String midNameFilter) throws SQLException {
        setItems(doctorService.getAll(midNameFilter));
    }
}
