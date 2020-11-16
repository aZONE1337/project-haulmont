package org.haulmont.example.polyclinic.ui.patient;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.haulmont.example.polyclinic.backend.entity.Patient;
import org.haulmont.example.polyclinic.backend.service.PatientService;

import java.sql.SQLException;

public class PatientForm extends FormLayout {
    private final PatientService service = PatientService.getInstance();
    private final PatientGrid grid;
    private final Dialog parent;

    private final TextField mName = new TextField("Фамилия:");
    private final TextField fName = new TextField("Имя:");
    private final TextField lName = new TextField("Отчество:");
    private final TextField phone = new TextField("Номер:");

    private final Button save = new Button("Сохранить");
    private final Button cancel = new Button("Отменить");

    public PatientForm(PatientGrid grid, Dialog parent) throws SQLException, ClassNotFoundException {
        this.grid = grid;
        this.parent = parent;

        setSizeFull();

        add(mName, fName, lName, phone, configuredButtons());
    }

    private HorizontalLayout configuredButtons() {
        save.addClickListener(click -> {

            Patient fromGrid = grid.asSingleSelect().getValue();

            if (isValidInput()) {
                Patient patient = new Patient();
                patient.setFirstName(fName.getValue());
                patient.setMiddleName(mName.getValue());
                patient.setLastName(lName.getValue());
                patient.setPhone(phone.getValue());
                try {
                    if (fromGrid != null) {
                        service.update(fromGrid.getId(), patient);
                    } else {
                        service.create(patient);
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

    private boolean isValidPhone(String string) {
        return string.length() >= 11 && string.matches("8[0-9]{10}");
    }

    private void clearFields() {
        fName.clear();
        mName.clear();
        lName.clear();
        phone.clear();
    }

    public void setFields(Patient patient) {
        fName.setValue(patient.getFirstName());
        mName.setValue(patient.getMiddleName());
        lName.setValue(patient.getLastName());
        phone.setValue(patient.getPhone());
    }

    private boolean isValidInput() {
        return isValidString(fName.getValue())
                && isValidString(mName.getValue())
                && isValidString(lName.getValue())
                && isValidPhone(phone.getValue());
    }
}
