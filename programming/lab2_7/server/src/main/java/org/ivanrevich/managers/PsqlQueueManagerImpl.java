package org.ivanrevich.managers;

import org.ivanrevich.models.Vehicle;
import org.ivanrevich.repository.ReflectionCrudRepository;

import java.time.Instant;
import java.util.List;
import java.util.PriorityQueue;

public class PsqlQueueManagerImpl implements QueueManager{
    ReflectionCrudRepository<Vehicle, Integer> repository;
    private final PriorityQueue<Vehicle> inMemoryPriorityQueue = new PriorityQueue<>();
    private final Instant initDate = Instant.now();

    @Override
    public void add(Vehicle vehicle) {
        Vehicle v=repository.save(vehicle);
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
        if(repository.existsById(id)) {
            repository.update(v);
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
        repository.deleteById(v.getId());
        return v;
    }

    @Override
    public void remove_by_id(int id) {
        repository.deleteById(id);
        inMemoryPriorityQueue.removeIf(vehicle -> vehicle.getId() == id);
    }

    @Override
    public void clear() {
        for(Vehicle v:repository.findAll()){
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
            Vehicle v2 = repository.save(v);
            inMemoryPriorityQueue.add(v2);
        }
    }
}
