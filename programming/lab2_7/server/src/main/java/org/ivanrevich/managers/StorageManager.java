package org.ivanrevich.managers;

import org.ivanrevich.models.Vehicle;

import java.util.ArrayList;
import java.util.List;

/**
 * Интерфейс менеджера хранения данных.
 * <p>
 * Отвечает за сохранение и загрузку коллекции из файла.
 * </p>
 *
 * @author Ivan Revich
 * @version 1.0
 * @see Vehicle
 * @see StorageManagerImpl
 */
public interface StorageManager {
    void save(ArrayList<Vehicle> queue, String path);

    List<Vehicle> load(String path);
}
