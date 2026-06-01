package org.ivanrevich.managers;

import org.ivanrevich.models.Vehicle;
import org.ivanrevich.repository.ReflectionCrudRepository;

import java.util.ArrayList;
import java.util.List;

public class PsqlStorageManagerImpl implements StorageManager{
//    String url = "jdbc:postgresql://localhost:5432/ivanrevich";
//    String user = "ivanrevich";
//    String password = "ivanrevich"; // TODO THAT's fucking shit, move it to configs file
    ReflectionCrudRepository<Vehicle, Integer> repository;

    @Override
    public void save(ArrayList<Vehicle> queue, String path) {
        for(Vehicle vehicle : queue){
            if(repository.existsById(vehicle.getId())) {
                repository.update(vehicle);
            }else {
                repository.save(vehicle);
            }
        }
    }

    @Override
    public List<Vehicle> load(String path) {
        return repository.findAll();
    }
}
