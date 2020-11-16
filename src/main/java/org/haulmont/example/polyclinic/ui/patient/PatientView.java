package org.haulmont.example.polyclinic.ui.patient;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.haulmont.example.MainLayout;
import org.haulmont.example.polyclinic.backend.entity.Patient;
import org.haulmont.example.polyclinic.backend.service.PatientService;

import java.sql.SQLException;

@Route(value = "patients", layout = MainLayout.class)
public class PatientView extends VerticalLayout {
    private final PatientService service = PatientService.getInstance();

    private final PatientGrid grid = new PatientGrid();
    private final Dialog dialog = new Dialog();
    private final PatientForm form = new PatientForm(grid, dialog);

    private final TextField midNameFilter = new TextField();
    private final Button apply = new Button("Применить");

    private final H1 header = new H1("Пациенты");

    private final Button add = new Button("Добавить");
    private final Button set = new Button("Изменить");
    private final Button delete = new Button("Удалить");

    public PatientView() throws SQLException, ClassNotFoundException {
        setSizeFull();

        configureDialog();

        header.addClassName("header");

        add(header, configuredFilter(), grid, configuredButtons());
    }

    private void configureDialog() {
        dialog.add(form);
        dialog.setCloseOnOutsideClick(false);
        dialog.setModal(true);
    }

    private HorizontalLayout configuredFilter() {
        midNameFilter.setClearButtonVisible(true);
        midNameFilter.setPlaceholder("Фамилия");

        apply.addClickListener(click -> {
            try {
                grid.updateGridFiltered(midNameFilter.getValue());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        return new HorizontalLayout(midNameFilter, apply);
    }

    private HorizontalLayout configuredButtons() {
        add.addClickListener(click -> {
            grid.deselectAll();
            dialog.open();
        });

        set.addClickListener(click -> {
            form.setFields(grid.asSingleSelect().getValue());
            dialog.open();
        });

        delete.addClickListener(click -> {

            Patient patient = grid.asSingleSelect().getValue();

            try {
                if (service.hasRelatedReceipt(patient.getId())) {
                    Notification alert = new Notification("Для данного пациента существуют выписанные рецепты." +
                            "\nСначала удалите их.");
                    alert.setDuration(3000);
                    alert.open();
                } else {
                    service.delete(patient.getId());
                    grid.updateGrid();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        return new HorizontalLayout(add, set, delete);
    }
}
