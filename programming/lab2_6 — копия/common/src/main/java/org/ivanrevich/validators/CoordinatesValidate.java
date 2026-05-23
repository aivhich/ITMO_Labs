package org.ivanrevich.validators;


import org.ivanrevich.models.Coordinates;

public class CoordinatesValidate{
    ValidateManagerImpl v = new ValidateManagerImpl();
    boolean apply(Coordinates coordinates){
        return  (v.checkNumValue(coordinates.getX(), true, true, true, -371, Double.MAX_VALUE, false, true))
                && (v.checkNumValue(coordinates.getY(), true, true, true, -Float.MAX_VALUE, 376, false, true));
    }
}
