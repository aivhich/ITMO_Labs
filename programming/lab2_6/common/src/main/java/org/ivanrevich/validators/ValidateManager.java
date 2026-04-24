package org.ivanrevich.validators;


/**
 * Интерфейс менеджера валидации данных.
 * <p>
 * Предоставляет методы для проверки числовых и строковых значений.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see ValidateManagerImpl
 */
public interface ValidateManager {
    boolean checkNumValue(Number value, boolean nonNull, boolean checkMin, boolean checkMax, double min, double max, boolean minInclusive, boolean maxInclusive);
    boolean checkStringValue(String value, boolean nonNull, boolean nonEmpty, long minLength, long maxLength);
}
