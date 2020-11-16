package org.haulmont.example.polyclinic.backend.entity;

import java.io.Serializable;

public enum Priority implements Serializable {
    NORMAL("Нормальный"), CITO("Срочный"), STATIM("Немедленный");

    private final String name;

    Priority(String name) {
        this.name = name;
    }

    public static Priority getByValue(String string) {
        for (Priority p : Priority.values()) {
            if (p.name.equals(string))
                return p;
        }
        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
