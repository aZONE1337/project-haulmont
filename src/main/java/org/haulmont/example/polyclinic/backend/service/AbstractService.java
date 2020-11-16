package org.haulmont.example.polyclinic.backend.service;

import java.sql.SQLException;
import java.util.List;

public interface AbstractService<E, K> {

    E get(K id) throws SQLException;

    List<E> getAll() throws SQLException;

    List<E> getAll(String midNameFilter) throws SQLException;

    boolean update(K id, E entity) throws SQLException;

    boolean delete(K id) throws SQLException;

    boolean hasRelatedReceipt(K id) throws SQLException;

    boolean create(E entity) throws SQLException;
}
