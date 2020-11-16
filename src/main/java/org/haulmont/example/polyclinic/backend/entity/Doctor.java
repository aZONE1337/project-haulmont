package org.haulmont.example.polyclinic.backend.entity;

import java.io.Serializable;
import java.util.Objects;

public class Doctor implements Serializable {
    private long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String specialization;

    public Doctor() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doctor)) return false;
        Doctor doctor = (Doctor) o;
        return id == doctor.id &&
                firstName.equals(doctor.firstName) &&
                middleName.equals(doctor.middleName) &&
                lastName.equals(doctor.lastName) &&
                specialization.equals(doctor.specialization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, middleName, lastName, specialization);
    }
}
