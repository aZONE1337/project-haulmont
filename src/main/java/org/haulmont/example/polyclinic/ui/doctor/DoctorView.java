package org.haulmont.example.polyclinic.ui.doctor;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.atmosphere.interceptor.AtmosphereResourceStateRecovery;
import org.haulmont.example.MainLayout;
import org.haulmont.example.polyclinic.backend.entity.Doctor;
import org.haulmont.example.polyclinic.backend.service.DoctorService;

import java.sql.SQLException;

@Route(value = "doctors", layout = MainLayout.class)
public class DoctorView extends VerticalLayout {
    private final DoctorService service = DoctorService.getInstance();

    private final DoctorGrid grid = new DoctorGrid();
    private final Dialog dialog = new Dialog();
    private final DoctorForm form = new DoctorForm(grid, dialog);

    private final H1 header = new H1("Врачи");

    private final TextField midNameFilter = new TextField();
    private final Button apply = new Button("Применить");

    private final Button add = new Button("Добавить");
    private final Button set = new Button("Изменить");
    private final Button delete = new Button("Удалить");

    public DoctorView() throws SQLException, ClassNotFoundException {
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

            Doctor doctor = grid.asSingleSelect().getValue();

            try {
                if (service.hasRelatedReceipt(doctor.getId())) {
                    Notification alert = new Notification("У данного врача существуют выписанные рецепты." +
                            "\nСначала удалите их.");
                    alert.setDuration(3000);
                    alert.open();
                } else {
                    service.delete(doctor.getId());
                    grid.updateGrid();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        return new HorizontalLayout(add, set, delete);
    }
}
