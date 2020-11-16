package org.haulmont.example.polyclinic.ui.doctor;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.haulmont.example.polyclinic.backend.entity.Doctor;
import org.haulmont.example.polyclinic.backend.service.DoctorService;

import java.sql.SQLException;

public class DoctorForm extends FormLayout {
    private final DoctorService service = DoctorService.getInstance();
    private final DoctorGrid grid;
    private final Dialog parent;

    private final TextField mName = new TextField("Фамилия:");
    private final TextField fName = new TextField("Имя:");
    private final TextField lName = new TextField("Отчество:");
    private final TextField spec = new TextField("Специальность:");

    private final Button save = new Button("Сохранить");
    private final Button cancel = new Button("Отменить");

    public DoctorForm(DoctorGrid grid, Dialog parent) throws SQLException, ClassNotFoundException {
        this.grid = grid;
        this.parent = parent;

        setSizeFull();

        add(mName, fName, lName, spec, configuredButtons());
    }

    private HorizontalLayout configuredButtons() {
        save.addClickListener(click -> {

            Doctor fromGrid = grid.asSingleSelect().getValue();

            if (isValidInput()) {
                Doctor doctor = new Doctor();
                doctor.setFirstName(fName.getValue());
                doctor.setMiddleName(mName.getValue());
                doctor.setLastName(lName.getValue());
                doctor.setSpecialization(spec.getValue());
                try {
                    if (fromGrid != null) {
                        service.update(fromGrid.getId(), doctor);
                    } else {
                        service.create(doctor);
                    }
                    clearFields();
                    parent.close();
                    grid.updateGrid();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            } else {
                Notification alert = new Notification("Неверно заданы поля");
                alert.setDuration(1500);
                alert.open();
            }
        });
        cancel.addClickListener(click -> {
            clearFields();
            parent.close();
        });

        return new HorizontalLayout(save, cancel);
    }

    private boolean isValidString(String string) {
        return string.length() >= 2 && string.matches("[а-яА-ЯёЁ]+");
    }

    private void clearFields() {
        fName.clear();
        mName.clear();
        lName.clear();
        spec.clear();
    }

    public void setFields(Doctor doctor) {
        fName.setValue(doctor.getFirstName());
        mName.setValue(doctor.getMiddleName());
        lName.setValue(doctor.getLastName());
        spec.setValue(doctor.getSpecialization());
    }

    private boolean isValidInput() {
        return isValidString(fName.getValue())
                && isValidString(mName.getValue())
                && isValidString(lName.getValue())
                && isValidString(spec.getValue());
    }
}
