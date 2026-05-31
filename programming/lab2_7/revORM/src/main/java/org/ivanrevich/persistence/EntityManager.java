package org.ivanrevich.persistence;

import org.ivanrevich.repository.ReflectionCrudRepository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EntityManager {

    private final DataSource dataSource;

    @SuppressWarnings("rawtypes")
    private final Map<Class<?>, ReflectionCrudRepository> repos = new HashMap<>();

    public EntityManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T, ID> void register(Class<T> clazz, Class<ID> idClass) {
        repos.put(clazz, new ReflectionCrudRepository<>(dataSource, clazz, idClass));
    }

    @SuppressWarnings("unchecked")
    public <T> T save(T entity) {
        return (T) repoFor(entity.getClass()).save(entity);
    }

    @SuppressWarnings("unchecked")
    public <T> void update(T entity) {
        repoFor(entity.getClass()).update(entity);
    }

    @SuppressWarnings("unchecked")
    public <T, ID> Optional<T> findById(Class<T> clazz, ID id) {
        return repoFor(clazz).findById(id);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> findAll(Class<T> clazz) {
        return repoFor(clazz).findAll();
    }

    @SuppressWarnings("unchecked")
    public <T, ID> void deleteById(Class<T> clazz, ID id) {
        repoFor(clazz).deleteById(id);
    }

    @SuppressWarnings("unchecked")
    public <T, ID> boolean existsById(Class<T> clazz, ID id) {
        return repoFor(clazz).existsById(id);
    }

    @SuppressWarnings("rawtypes")
    private ReflectionCrudRepository repoFor(Class<?> clazz) {
        ReflectionCrudRepository repo = repos.get(clazz);
        if (repo == null) {
            throw new IllegalStateException(
                    "No repository registered for " + clazz.getName()
                            + ". Call em.register() first.");
        }
        return repo;
    }
}