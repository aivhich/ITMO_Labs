package org.ivanrevich.managers;

import org.ivanrevich.models.Vehicle;
import org.ivanrevich.persistence.EntityManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;


// ЗАГЛУШКА
public class PsqlStorageManagerImpl implements StorageManager{
    private final EntityManager entityManager;

    public PsqlStorageManagerImpl(DataSource dataSource) {
        this.entityManager = new EntityManager(dataSource);
        this.entityManager.register(Vehicle.class);
    }

    @Override
    public void save(ArrayList<Vehicle> queue, String path) {
        for (Vehicle vehicle : queue) {
            if (entityManager.existsById(Vehicle.class, vehicle.getId())) {
                entityManager.update(vehicle);
            } else {
                try {
                    entityManager.save(vehicle);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public List<Vehicle> load(String path) {
        return entityManager.findAll(Vehicle.class);
    }
}
