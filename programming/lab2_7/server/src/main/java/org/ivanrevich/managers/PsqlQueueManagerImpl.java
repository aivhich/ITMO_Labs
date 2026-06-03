package org.ivanrevich.managers;

import org.ivanrevich.models.Vehicle;
import org.ivanrevich.persistence.EntityManager;

import javax.sql.DataSource;
import java.time.Instant;
import java.util.List;
import java.util.PriorityQueue;

public class PsqlQueueManagerImpl implements QueueManager{
    private final EntityManager entityManager;
    private final PriorityQueue<Vehicle> inMemoryPriorityQueue = new PriorityQueue<>();
    private final Instant initDate = Instant.now();

    public PsqlQueueManagerImpl(DataSource dataSource) {
        this.entityManager = new EntityManager(dataSource);
        this.entityManager.register(Vehicle.class);

        List<Vehicle> all = entityManager.findAll(Vehicle.class);
        inMemoryPriorityQueue.addAll(all);
    }

    @Override
    public void add(Vehicle vehicle) {
        Vehicle v=entityManager.save(vehicle);
        inMemoryPriorityQueue.add(v);
    }

    @Override
    public Instant getInitDate() {
        return initDate;
    }

    @Override
    public int size() {
        return inMemoryPriorityQueue.size();
    }

    @Override
    public Boolean isExistWithId(int id) {
        return inMemoryPriorityQueue.stream().anyMatch(vehicle -> vehicle.getId() == id);
    }

    @Override
    public Vehicle getById(int id) {
        return inMemoryPriorityQueue.stream().filter(vehicle -> vehicle.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void updateById(int id, Vehicle v) {
        if(entityManager.existsById(Vehicle.class, id)) {
            entityManager.update(v);
            inMemoryPriorityQueue.removeIf(vehicle -> vehicle.getId() == id);
            inMemoryPriorityQueue.add(v);
        }
    }

    @Override
    public PriorityQueue<Vehicle> getAll() {
        return inMemoryPriorityQueue;
    }

    @Override
    public Vehicle remove_head() {
        Vehicle v=inMemoryPriorityQueue.poll();
        entityManager.deleteById(Vehicle.class, v.getId());
        return v;
    }

    @Override
    public void remove_by_id(int id) {
        entityManager.deleteById(Vehicle.class, id);
        inMemoryPriorityQueue.removeIf(vehicle -> vehicle.getId() == id);
    }

    @Override
    public void clear() {
        for(Vehicle v:entityManager.findAll(Vehicle.class)){
            inMemoryPriorityQueue.remove(v);
        }
        inMemoryPriorityQueue.clear();
    }

    /// ACTUALLY it's doing nothing now
    @Override
    public int generateId() {
        return inMemoryPriorityQueue.stream()
                .mapToInt(Vehicle::getId)
                .max()
                .orElse(0) + 1;
    }

    @Override
    public void set(List<Vehicle> vehicles) {
        for(Vehicle v:vehicles){
            Vehicle v2 = entityManager.save(v);
            inMemoryPriorityQueue.add(v2);
        }
    }
}
