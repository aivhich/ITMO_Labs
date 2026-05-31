package org.ivanrevich.factory;

import org.ivanrevich.managers.IOManager;
import org.ivanrevich.models.Coordinates;
import org.ivanrevich.utils.GenericBuilder;
import org.ivanrevich.validators.ValidateManager;
import org.ivanrevich.validators.ValidateManagerImpl;

/**
 * Фабрика для создания и обновления координат.
 * <p>
 * Запрашивает координаты X и Y с валидацией.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Coordinates
 */
public class CoordinatesFactory {
    private final IOManager ioManager;
    public CoordinatesFactory(IOManager ioManager) {
        this.ioManager = ioManager;
    }

    public Coordinates createCoordinates() throws IllegalArgumentException{
        ValidateManager validator = new ValidateManagerImpl();
        GenericBuilder<Coordinates> coordinatesGenericBuilder = GenericBuilder.of(Coordinates::new);


        Double x = ioManager.askValue(
                null,
                () -> ioManager.askDouble("Enter x(should be more then -371):"),
                v -> (validator.checkNumValue(v, true, true, true, -371, Double.MAX_VALUE, false, true))
        );

        Float y = ioManager.askValue(
                null,
                () -> ioManager.askFloat("Enter y(should be equal or less then 376):"),
                v -> (validator.checkNumValue(v, true, true, true, -Float.MAX_VALUE, 376, false, true))
        );

        return coordinatesGenericBuilder
                .with(Coordinates::setX, x)
                .with(Coordinates::setY, y)
                .build();
    }

    public Coordinates updateCoordinates(Coordinates old) throws IllegalArgumentException{
        ValidateManager validator = new ValidateManagerImpl();
        GenericBuilder<Coordinates> coordinatesGenericBuilder = GenericBuilder.of(Coordinates::new);


        Double x = ioManager.askValue(
                old.getX(),
                () -> ioManager.askDouble("Enter x or enter to skip(should be more then -371):"),
                v -> (validator.checkNumValue(v, false, true, true, -371, Double.MAX_VALUE, false, true))
        );

        Float y = ioManager.askValue(
                old.getY(),
                () -> ioManager.askFloat("Enter y or enter to skip(should be equal or less then 376):"),
                v -> (validator.checkNumValue(v, false, true, true, -Float.MAX_VALUE, 376, false, true))
        );


        return coordinatesGenericBuilder
                .with(Coordinates::setX, x)
                .with(Coordinates::setY, y)
                .build();
    }
}
