package org.haulmont.example.polyclinic.ui.receipt;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.haulmont.example.MainLayout;
import org.haulmont.example.polyclinic.backend.entity.Priority;
import org.haulmont.example.polyclinic.backend.entity.Receipt;
import org.haulmont.example.polyclinic.backend.service.ReceiptService;

import java.sql.SQLException;

@Route(value = "receipts", layout = MainLayout.class)
public class ReceiptView extends VerticalLayout {
    private final ReceiptService service = ReceiptService.getInstance();

    private final ReceiptGrid grid = new ReceiptGrid();
    private final Dialog dialog = new Dialog();
    private final ReceiptForm form = new ReceiptForm(grid, dialog);

    private final H1 header = new H1("Рецепты");

    private final TextField midNameFilter = new TextField();
    private final TextField descriptionFilter = new TextField();
    private final Select<Priority> priorityFilter = new Select<>(Priority.values());
    private final Button apply = new Button("Применить");

    private final Button add = new Button("Добавить");
    private final Button set = new Button("Изменить");
    private final Button delete = new Button("Удалить");

    public ReceiptView() throws SQLException, ClassNotFoundException {
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
        descriptionFilter.setClearButtonVisible(true);

        midNameFilter.setPlaceholder("Фамилия");
        descriptionFilter.setPlaceholder("Описание");
        priorityFilter.setValue(Priority.NORMAL);

        apply.addClickListener(click -> {
            try {
                grid.updateGridFiltered(
                        midNameFilter.getValue(),
                        descriptionFilter.getValue(),
                        priorityFilter.getValue()
                );
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        return new HorizontalLayout(midNameFilter, descriptionFilter, priorityFilter, apply);
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

            Receipt receipt = grid.asSingleSelect().getValue();

            try {
                service.delete(receipt.getId());
                grid.updateGrid();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        return new HorizontalLayout(add, set, delete);
    }
}