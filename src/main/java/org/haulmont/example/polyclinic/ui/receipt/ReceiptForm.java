package org.haulmont.example.polyclinic.ui.receipt;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import org.haulmont.example.polyclinic.backend.entity.Doctor;
import org.haulmont.example.polyclinic.backend.entity.Patient;
import org.haulmont.example.polyclinic.backend.entity.Priority;
import org.haulmont.example.polyclinic.backend.entity.Receipt;
import org.haulmont.example.polyclinic.backend.service.DoctorService;
import org.haulmont.example.polyclinic.backend.service.PatientService;
import org.haulmont.example.polyclinic.backend.service.ReceiptService;

import java.sql.Date;
import java.sql.SQLException;

public class ReceiptForm extends FormLayout {
    private final ReceiptService service = ReceiptService.getInstance();
    private final PatientService patientService = PatientService.getInstance();
    private final DoctorService doctorService = DoctorService.getInstance();
    private final ReceiptGrid grid;
    private final Dialog parent;

    private final TextField description = new TextField("Описание:");
    private final TextField patient = new TextField("Пациент:");
    private final TextField doctor = new TextField("Врач:");
    private final DatePicker date = new DatePicker("Дата назначения:");
    private final DatePicker expDate = new DatePicker("Дата окончания действия:");
    private final Select<Priority> priority = new Select<>(Priority.values());

    private final Button save = new Button("Сохранить");
    private final Button cancel = new Button("Отменить");

    public ReceiptForm(ReceiptGrid grid, Dialog parent) throws SQLException, ClassNotFoundException {
        this.grid = grid;
        this.parent = parent;

        setSizeFull();

        patient.setPlaceholder("Фамилия Имя Отчество");
        doctor.setPlaceholder("Фамилия Имя Отчество");
        priority.setPlaceholder("Выберите приоритет");

        add(
                description,
                patient,
                doctor,
                date,
                expDate,
                priority,
                configuredButtons()
        );
    }

    private HorizontalLayout configuredButtons() {
        save.addClickListener(click -> {

            Receipt fromGrid = grid.asSingleSelect().getValue();

            if (isValidInput()) {
                Receipt receipt = new Receipt();
                receipt.setDescription(description.getValue());
                receipt.setDate(Date.valueOf(date.getValue()));
                receipt.setExpirationDate(Date.valueOf(expDate.getValue()));
                receipt.setPriority(priority.getValue());
                try {
                    Notification noSuchPerson = new Notification();
                    noSuchPerson.setDuration(1500);

                    Patient p = patientService.getByFullName(patient.getValue());
                    Doctor d = doctorService.getByFullname(doctor.getValue());

                    if (p.getFirstName() == null) {
                        noSuchPerson.setText("Пациент не найден");
                        noSuchPerson.open();
                    } else {
                        receipt.setPatient(p);
                    }

                    if (d.getFirstName() == null) {
                        noSuchPerson.setText("Врач не найден");
                        noSuchPerson.open();
                    } else {
                        receipt.setDoctor(d);
                    }

                    if (fromGrid != null) {
                        service.update(fromGrid.getId(), receipt);
                    } else {
                        service.create(receipt);
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
        return string.length() >= 2;
    }

    private boolean isValidPerson(String personData) {
        return personData.length() >= 8 && personData.matches("[а-яА-ЯёЁ]+\\s[а-яА-ЯёЁ]+\\s[а-яА-ЯёЁ]+");
    }

    private boolean isValidDateRange(Date from, Date to) {
        return to.after(from);
    }

    private void clearFields() {
        description.clear();
        patient.clear();
        doctor.clear();
        date.clear();
        expDate.clear();
        priority.clear();
    }

    public void setFields(Receipt receipt) {
        description.setValue(receipt.getDescription());
        patient.setValue(receipt.getPatient().getMiddleName() +
                " " + receipt.getPatient().getFirstName() +
                " " + receipt.getPatient().getLastName());
        doctor.setValue(receipt.getDoctor().getMiddleName() +
                " " + receipt.getDoctor().getFirstName() +
                " " + receipt.getDoctor().getLastName());
        //troubles with converting toLocalDate
        date.setValue(receipt.getDate().toLocalDate());
        expDate.setValue(receipt.getExpirationDate().toLocalDate());
        priority.setValue(receipt.getPriority());
    }

    private boolean isValidInput() {
        return isValidString(description.getValue())
                && isValidPerson(patient.getValue())
                && isValidPerson(patient.getValue())
                && isValidDateRange(
                        new Date(date.getValue().toEpochDay()),
                        new Date(expDate.getValue().toEpochDay())
                );
    }
}
