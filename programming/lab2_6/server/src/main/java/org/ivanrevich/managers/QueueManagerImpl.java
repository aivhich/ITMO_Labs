package org.ivanrevich.managers;

import org.ivanrevich.exceptions.AppException;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.utils.ResultCode;

import java.time.Instant;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Реализация менеджера очереди транспортных средств.
 *
 * @author Ivan Prokhorevich
 * @version 2.0
 */
public class QueueManagerImpl implements QueueManager {
    private final PriorityQueue<Vehicle> priorityQueue = new PriorityQueue<>();
    private final Instant initDate = Instant.now();

    @Override
    public void add(Vehicle vehicle) {
        vehicle.setId(generateId());
        priorityQueue.add(vehicle);
    }

    @Override
    public void set(List<Vehicle> vehicles) {
        priorityQueue.addAll(vehicles);
    }

    @Override
    public void remove_by_id(int id) {
        boolean removed = priorityQueue.removeIf(vehicle -> vehicle.getId() == id);
        if (!removed) throw new AppException(ResultCode.ID_ISN_EXIST);
    }

    @Override
    public PriorityQueue<Vehicle> getAll() {
        return priorityQueue;
    }

    @Override
    public Vehicle remove_head() {
        return priorityQueue.poll();
    }

    @Override
    public void clear() {
        priorityQueue.clear();
    }

    @Override
    public int generateId() {
        return priorityQueue.stream()
                .mapToInt(Vehicle::getId)
                .max()
                .orElse(0) + 1;
    }

    @Override
    public Boolean isExistWithId(int id) {
        return priorityQueue.stream().anyMatch(vehicle -> vehicle.getId() == id);
    }

    @Override
    public Vehicle getById(int id) {
        return priorityQueue.stream()
                .filter(vehicle -> vehicle.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void updateById(int id, Vehicle v) {
        priorityQueue.removeIf(vehicle -> vehicle.getId() == id);
        priorityQueue.add(v);
    }

    @Override
    public int size() {
        return priorityQueue.size();
    }

    @Override
    public Instant getInitDate() {
        return initDate;
    }
}