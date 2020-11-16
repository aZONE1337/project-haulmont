package org.haulmont.example.polyclinic.backend.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

public class Receipt implements Serializable {
    private Long id;
    private String description;
    private Patient patient;
    private Doctor doctor;
    private Date date;
    private Date expirationDate;
    private Priority priority = Priority.NORMAL;

    public Receipt() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Receipt)) return false;
        Receipt receipt = (Receipt) o;
        return id.equals(receipt.id) &&
                description.equals(receipt.description) &&
                patient.equals(receipt.patient) &&
                doctor.equals(receipt.doctor) &&
                date.equals(receipt.date) &&
                expirationDate.equals(receipt.expirationDate) &&
                priority == receipt.priority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, patient, doctor, date, expirationDate, priority);
    }
}
