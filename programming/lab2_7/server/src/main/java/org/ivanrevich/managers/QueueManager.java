package org.ivanrevich.managers;

import org.ivanrevich.auth.Credentials;
import org.ivanrevich.models.Vehicle;

import java.time.Instant;
import java.util.List;
import java.util.PriorityQueue;


/**
 * Стек менеджеров ввода-вывода.
 * <p>
 * Позволяет переключаться между источниками ввода (консоль/файл)
 * с возможностью возврата к предыдущему источнику.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see IOManager
 */
public interface QueueManager {
    void add(Vehicle vehicle);
    Vehicle getLast();
    int size();
    Boolean isExistWithId(int id);
    Integer getOwnerById(int id);
    Vehicle getById(int id);
    void updateById(int id, Vehicle v);
    PriorityQueue<Vehicle> getAll();
    Vehicle remove_head();
    void remove_by_id(int id);
    void clear();
    void clear(Integer byUserId);
    int generateId();
    void set(List<Vehicle> vehicles);
}
