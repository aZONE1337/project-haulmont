package org.haulmont.example.polyclinic.backend.entity;

import java.io.Serializable;
import java.util.Objects;

public class Patient implements Serializable {
    private long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String phone;

    public Patient() {

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient)) return false;
        Patient patient = (Patient) o;
        return id == patient.id &&
                firstName.equals(patient.firstName) &&
                middleName.equals(patient.middleName) &&
                lastName.equals(patient.lastName) &&
                phone.equals(patient.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, middleName, lastName, phone);
    }
}
