package org.ivanrevich.repository;


import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


public interface CrudRepository<T, ID> {
    T save(T entity) throws Exception;
    void update(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    void deleteById(ID id);
    boolean existsById(ID id);
    void initTable();
    Optional<T> findByField(String columnName, Object value);
}
