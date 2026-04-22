package org.ivanrevich.managers;

import org.ivanrevich.exceptions.Exceptions;
import org.ivanrevich.models.Coordinates;
import org.ivanrevich.models.FuelType;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.models.VehicleType;
import org.ivanrevich.utils.GenericBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.ivanrevich.validators.FileValidator.validateCollectionNewFile;


/**
 * Реализация менеджера хранения в CSV-формате.
 * <p>
 * Сохраняет и загружает данные в CSV-файл с валидацией.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see StorageManager
 * @see Vehicle
 */
public class StorageManagerImpl implements StorageManager {


    public StorageManagerImpl(String path, QueueManager queueManager) {
        queueManager.set(load(path));
    }

    @Override
    public void save(ArrayList<Vehicle> queue, String path){
        validateCollectionNewFile(path, ".csv", false);
        try (FileOutputStream fos = new FileOutputStream(path);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             BufferedWriter writer = new BufferedWriter(osw)) {
            StringBuilder data = new StringBuilder("id,name,x,y,creationDate,enginePower,numberOfWheels,type,fuelType\n");
            for(Vehicle v: queue){
                data.append(v.toCsvString()).append("\n");
            }
            writer.write(data.toString());
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(Exceptions.FILE_NOT_FOUND);
        }
    }

    @Override
    public List<Vehicle> load(String path) {
        validateCollectionNewFile(path, ".csv", true);
        ValidateManager validator = new ValidateManagerImpl();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                    new FileInputStream(path),
                    StandardCharsets.UTF_8)
        )) {

            List<Vehicle> loadedVehicles = new ArrayList<>();
            List<Integer> ids = new ArrayList<>();
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] parts = line.split(",", -1);
                if (parts.length < 9) continue;

                try {
                    int id = Integer.parseInt(parts[0].trim());
                    if(!validator.checkNumValue(id, true, true, true, -1, Integer.MAX_VALUE, false, true) || ids.contains(id))
                        throw new Exception("LOADING_ERROR ID");
                    ids.add(id);

                    String name = parts[1].trim();
                    if(!validator.checkStringValue(name, true, true, 0, 0))
                        throw new Exception("LOADING_ERROR name");

                    double x = Double.parseDouble(parts[2].trim());
                    if(!validator.checkNumValue(x, true, true, true, -371, Double.MAX_VALUE, false, true))
                        throw new Exception("LOADING_ERROR x");

                    float y = Float.parseFloat(parts[3].trim());
                    if(!validator.checkNumValue(y, true, true, true, -Float.MAX_VALUE, 376, false, true))
                        throw new Exception("LOADING_ERROR y");

                    Date creationDate = Date.from(
                            Instant.parse(parts[4].trim())
                    );

                    float enginePower = Float.parseFloat(parts[5].trim());
                    if (!validator.checkNumValue(enginePower, true, true, true, 0, Float.MAX_VALUE, false, true))
                        throw new Exception("LOADING_ERROR enginePower");

                    long numberOfWheels = Long.parseLong(parts[6].trim());
                    if (!validator.checkNumValue(numberOfWheels, true, true, true, 0, Float.MAX_VALUE, false, true))
                        throw new Exception("LOADING_ERROR numberOfWheels");

                    VehicleType type = VehicleType.valueOf(parts[7].trim());
                    FuelType fuelType = FuelType.valueOf(parts[8].trim());
                    Vehicle v = GenericBuilder.of(Vehicle::new)
                            .with(Vehicle::setId, id)
                            .with(Vehicle::setName, name)
                            .with(Vehicle::setEnginePower, enginePower)
                            .with(Vehicle::setCoordinates, (
                                    GenericBuilder.of(Coordinates::new)
                                    .with(Coordinates::setX, x)
                                    .with(Coordinates::setY, y)
                                    .build()))
                            .with(Vehicle::setCreationDate, creationDate)
                            .with(Vehicle::setNumberOfWheels, numberOfWheels)
                            .with(Vehicle::setType, type)
                            .with(Vehicle::setFuelType, fuelType)
                            .build();

                    loadedVehicles.add(v);

                } catch (Exception e) {
                    System.out.println("Skipping invalid line: " + line);
                    System.out.println(e.getMessage());
                }
            }
            return loadedVehicles;

        } catch (FileNotFoundException e) {
            System.out.println("The file to store the collection does not exist.");
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + path);
        }
        return List.of();
    }
}
