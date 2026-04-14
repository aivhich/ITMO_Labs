package org.ivanrevich.managers;

import org.ivanrevich.exceptions.Exceptions;
import org.ivanrevich.models.Vehicle;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Интерфейс менеджера очереди транспортных средств.
 * <p>
 * Управляет коллекцией {@link Vehicle} с операциями добавления,
 * удаления, поиска и обновления.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Vehicle
 * @see QueueManagerImpl
 */
public class QueueManagerImpl implements QueueManager{
    private final PriorityQueue<Vehicle> priorityQueue = new PriorityQueue<Vehicle>();
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
        if(!priorityQueue.removeIf(vehicle -> vehicle.getId()==id)) throw  new NullPointerException(Exceptions.ID_ISN_EXIST);
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
        AtomicInteger out= new AtomicInteger(-1);
        if(!priorityQueue.isEmpty()) {
            priorityQueue.forEach(vehicle -> {
                out.set(Integer.max(vehicle.getId(), out.get()));
            });
        }
        return out.get()+1;
    }

    @Override
    public Boolean isExistWithId(int id) {
        return priorityQueue.stream().anyMatch(vehicle -> vehicle.getId() == id);
    }

    @Override
    public Vehicle getById(int id) {
        return priorityQueue.stream().filter(vehicle -> vehicle.getId()==id).findFirst().orElse(null);
    }

    @Override
    public void updateById(int id, Vehicle v) {
        priorityQueue.removeIf(vehicle -> vehicle.getId()==id);
        priorityQueue.add(v);
    }

    @Override
    public int size() { return priorityQueue.size(); }

    @Override
    public Instant getInitDate() { return initDate; }
}
