package org.ivanrevich.validators;

import org.ivanrevich.models.Vehicle;

public class VehicleValidate {
    ValidateManagerImpl v = new ValidateManagerImpl();
    public boolean apply(Vehicle vehicle){
        return v.checkStringValue(vehicle.getName(), true, true, 1, 255)
        &&(v.checkNumValue(vehicle.getEnginePower(), true, true, true, 0, Float.MAX_VALUE, false, true))
        &&(v.checkNumValue(vehicle.getNumberOfWheels(), true, true, true, 0, Long.MAX_VALUE, false, true))
                && (new CoordinatesValidate()).apply(vehicle.getCoordinates());
    }
}
