package org.ivanrevich.managers;

import org.ivanrevich.auth.Credentials;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.persistence.EntityManager;

import javax.sql.DataSource;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.PriorityQueue;

public class PsqlQueueManagerImpl implements QueueManager{
    private final EntityManager entityManager;
    private final PriorityQueue<Vehicle> inMemoryPriorityQueue = new PriorityQueue<>();
//    private final Instant initDate = Instant.now();

    public PsqlQueueManagerImpl(DataSource dataSource) {
        this.entityManager = new EntityManager(dataSource);
        this.entityManager.register(Vehicle.class);

        List<Vehicle> all = entityManager.findAll(Vehicle.class);
        inMemoryPriorityQueue.addAll(all);
    }

    @Override
    public synchronized void add(Vehicle vehicle) {
        Vehicle v = null;
        try {
            v = entityManager.save(vehicle);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        inMemoryPriorityQueue.add(v);
    }


    @Override
    public synchronized int size() {
        return inMemoryPriorityQueue.size();
    }

    @Override
    public synchronized Boolean isExistWithId(int id) {
        return inMemoryPriorityQueue.stream().anyMatch(vehicle -> vehicle.getId() == id);
    }

    @Override
    public synchronized Integer getOwnerById(int id) {
        try {
            return inMemoryPriorityQueue.stream().filter(vehicle -> vehicle.getId() == id).findFirst().orElseThrow().getAuthorId();
        }catch (NoSuchElementException e){
            return -1;
        }
    }

    @Override
    public synchronized Vehicle getById(int id) {
        return inMemoryPriorityQueue.stream().filter(vehicle -> vehicle.getId() == id).findFirst().orElse(null);
    }

    @Override
    public synchronized void updateById(int id, Vehicle v) {
        if(entityManager.existsById(Vehicle.class, id)) {
            entityManager.update(v);
            inMemoryPriorityQueue.removeIf(vehicle -> vehicle.getId() == id);
            inMemoryPriorityQueue.add(v);
        }
    }

    @Override
    public synchronized PriorityQueue<Vehicle> getAll() {
        return inMemoryPriorityQueue;
    }

    @Override
    public synchronized Vehicle remove_head() {
        Vehicle v=inMemoryPriorityQueue.poll();
        entityManager.deleteById(Vehicle.class, v.getId());
        return v;
    }

    @Override
    public synchronized void remove_by_id(int id) {
        entityManager.deleteById(Vehicle.class, id);
        inMemoryPriorityQueue.removeIf(vehicle -> vehicle.getId() == id);
    }

    @Override
    public synchronized void clear() {
        for(Vehicle v:entityManager.findAll(Vehicle.class)){
            entityManager.deleteById(Vehicle.class, v.getId());
        }
        inMemoryPriorityQueue.clear();
    }

    @Override
    public synchronized void clear(Integer byUserId) {
        for(Vehicle v:entityManager.findAll(Vehicle.class)){
            if(Objects.equals(v.getAuthorId(), byUserId)) {
                entityManager.deleteById(Vehicle.class, v.getId());
            }
        }
        inMemoryPriorityQueue.removeIf(vehicle -> Objects.equals(vehicle.getAuthorId(), byUserId));
    }

    /// ACTUALLY it's doing nothing now
    @Override
    public synchronized int generateId() {
        return inMemoryPriorityQueue.stream()
                .mapToInt(Vehicle::getId)
                .max()
                .orElse(0) + 1;
    }

    @Override
    public synchronized void set(List<Vehicle> vehicles) {
        for(Vehicle v:vehicles){
            Vehicle v2 = null;
            try {
                v2 = entityManager.save(v);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            inMemoryPriorityQueue.add(v2);
        }
    }

    @Override
    public synchronized Vehicle getLast() {
        return inMemoryPriorityQueue.peek();
    }
}
