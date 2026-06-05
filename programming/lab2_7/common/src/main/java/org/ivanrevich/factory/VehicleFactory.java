package org.ivanrevich.factory;

import org.ivanrevich.managers.IOManager;
import org.ivanrevich.models.FuelType;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.models.VehicleType;
import org.ivanrevich.utils.GenericBuilder;
import org.ivanrevich.validators.ValidateManager;
import org.ivanrevich.validators.ValidateManagerImpl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
/**
 * Фабрика для создания и обновления транспортных средств.
 * <p>
 * и {@link GenericBuilder} для построения объектов.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Vehicle
 * @see GenericBuilder
 */
public class VehicleFactory {
    private IOManager ioManager;

    public VehicleFactory(IOManager ioManager) {
        this.ioManager = ioManager;
    }

    public Vehicle createVehicle()  throws IllegalArgumentException{
        ValidateManager validator = new ValidateManagerImpl();
        GenericBuilder<Vehicle> vehicleGenericBuilder = GenericBuilder.of(Vehicle::new);

        String name = ioManager.askValue(
                null,
                () -> ioManager.askString("Enter vehicle name(shouldn't be empty, max length 255):"),
                v -> validator.checkStringValue(v, true, true, 1, 255)
        );

        Float enginePower = ioManager.askValue(
                null,
                () -> ioManager.askFloat("Enter vehicle engine power(should be more than 0):"),
                v -> (validator.checkNumValue(v, true, true, true, 0, Float.MAX_VALUE, false, true))
        );

        long numberOfWheels = ioManager.askValue(
                null,
                () -> ioManager.askLong("Enter vehicle count of wheels(should be more than 0):"),
                v -> (validator.checkNumValue(v, true, true, true, 0, Long.MAX_VALUE, false, true))
        );

        VehicleType type = ioManager.askValue(
                null,
                () -> {
                    int v = ioManager.askInt(
                            "Choose vehicle type:\r\n" +
                                    "1: HELICOPTER\r\n2: MOTORCYCLE\r\n3: CHOPPER\r\n4: HOVERBOARD\r\n");
                    return v > 0 && v <= VehicleType.values().length ? VehicleType.values()[v - 1] : null;
                },
                Objects::nonNull
        );

        FuelType fuelType = ioManager.askValue(
                null,
                () -> {
                    int v = ioManager.askInt(
                            "Choose fuel type:\r\n" +
                                    "1: KEROSENE\r\n2: MANPOWER\r\n3: NUCLEAR\r\n4: PLASMA\r\n5: ANTIMATTER\r\n");
                    return v > 0 && v <= FuelType.values().length ? FuelType.values()[v - 1] : null;
                },
                Objects::nonNull
        );


        return vehicleGenericBuilder
                .with(Vehicle::setName, name)
                .with(Vehicle::setEnginePower, enginePower)
                .with(Vehicle::setCoordinates, (new CoordinatesFactory(ioManager)).createCoordinates())
                .with(Vehicle::setCreationDate, Date.from(Instant.now()))
                .with(Vehicle::setNumberOfWheels, numberOfWheels)
                .with(Vehicle::setType, type)
                .with(Vehicle::setFuelType, fuelType)
                .with(Vehicle::setAuthorId, 0) // TODO ADD ADMIN ACCOUNT
                .build();
    }

    public Vehicle updateVehicle(Vehicle old){
        ValidateManager validator = new ValidateManagerImpl();
        GenericBuilder<Vehicle> vehicleGenericBuilder = GenericBuilder.of(Vehicle::new);
        //may cause error was t, f, f
        String name = ioManager.askValue(
                old.getName(),
                () -> ioManager.askString("Enter vehicle name or enter to skip(shouldn't be empty, max length 255):"),
                v -> validator.checkStringValue(v, true, false, 1, 255)
        );

        Float enginePower = ioManager.askValue(
                old.getEnginePower(),
                () -> ioManager.askFloat("Enter vehicle engine power or enter to skip(should be more than 0):"),
                v -> (validator.checkNumValue(v, true, true, true, 0, Float.MAX_VALUE, false, true))
        );

        Long numberOfWheels = ioManager.askValue(
                old.getNumberOfWheels(),
                () -> ioManager.askLong("Enter vehicle number of wheels or enter to skip(should be more than 0):"),
                v -> (validator.checkNumValue(v, true, true, true, 0, Long.MAX_VALUE, false, true))
        );

        VehicleType type = ioManager.askValue(
                old.getType(),
                () -> {
                    Integer v = ioManager.askInt(
                            "Choose vehicle type or enter to skip:\r\n" +
                                    "1: HELICOPTER\r\n2: MOTORCYCLE\r\n3: CHOPPER\r\n4: HOVERBOARD\r\nOr type other num to skip that step");
                    if (v!=null) return v > 0 && v <= VehicleType.values().length ? VehicleType.values()[v - 1] : null;
                    else return old.getType();
                },
                Objects::nonNull
        );

        FuelType fuelType = ioManager.askValue(
                old.getFuelType(),
                () -> {
                    Integer v = ioManager.askInt(
                            "Choose fuel type or enter to skip:\r\n" +
                                    "1: KEROSENE\r\n2: MANPOWER\r\n3: NUCLEAR\r\n4: PLASMA\r\n5: ANTIMATTER\r\nOr type other num to skip that step");
                    if (v!=null) return v > 0 && v <= FuelType.values().length ? FuelType.values()[v - 1] : null;
                    else return old.getFuelType();
                },
                Objects::nonNull
        );


        return vehicleGenericBuilder
                .with(Vehicle::setId, old.getId())
                .with(Vehicle::setName, name)
                .with(Vehicle::setEnginePower, enginePower)
                .with(Vehicle::setCoordinates, (new CoordinatesFactory(ioManager)).updateCoordinates(old.getCoordinates()))
                .with(Vehicle::setCreationDate, old.getCreationDate()).with(Vehicle::setType, type)
                .with(Vehicle::setNumberOfWheels, numberOfWheels)
                .with(Vehicle::setFuelType, fuelType)
                .with(Vehicle::setAuthorId, 0) // TODO ADD ADMIN ACCOUNT
                .build();
    }

    public Vehicle createVehicleForRef(){
        ValidateManager validator = new ValidateManagerImpl();
        Float enginePower = ioManager.askValue(
                null,
                () -> ioManager.askFloat("Enter vehicle engine power(should be more than 0):"),
                v -> (validator.checkNumValue(v, true, true, true, 0, Float.MAX_VALUE, false, true))
        );

        long numberOfWheels = ioManager.askValue(
                null,
                () -> ioManager.askLong("Enter vehicle count of wheels(should be more than 0):"),
                v -> (validator.checkNumValue(v, true, true, true, 0, Long.MAX_VALUE, false, true))
        );

        return GenericBuilder.of(Vehicle::new)
                .with(Vehicle::setEnginePower, enginePower)
                .with(Vehicle::setNumberOfWheels, numberOfWheels).build();
    }

}
