package org.haulmont.example;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.material.Material;
import org.haulmont.example.polyclinic.ui.doctor.DoctorView;
import org.haulmont.example.polyclinic.ui.patient.PatientView;
import org.haulmont.example.polyclinic.ui.receipt.ReceiptView;

/**
 * The main view contains a button and a click listener.
 */
@Route("")
@Theme(value = Material.class)
@PWA(name = "Project Base for Vaadin", shortName = "Project Base", enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Поликлиника");
        logo.addClassName("logo");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink doctorLink = new RouterLink("Врачи", DoctorView.class);
        RouterLink patientLink = new RouterLink("Пациенты", PatientView.class);
        RouterLink receiptLink = new RouterLink("Рецепты", ReceiptView.class);

        addToDrawer(new VerticalLayout(
                doctorLink,
                patientLink,
                receiptLink
        ));
    }
}
