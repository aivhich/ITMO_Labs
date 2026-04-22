package org.ivanrevich.managers;


/**
 * Реализация менеджера валидации.
 * <p>
 * Содержит логику проверки числовых и строковых данных.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see ValidateManager
 */
public class ValidateManagerImpl implements ValidateManager{
    public boolean checkNumValue(Number value, boolean nonNull,
                                 boolean checkMin, boolean checkMax,
                                 double min, double max,
                                 boolean minInclusive, boolean maxInclusive) {
        if (nonNull && value == null) {
            return false;
        }else if (value == null) {
            return true;
        }
        double d = value.doubleValue();
        if (checkMin) {
            if (minInclusive) {
                if (d < min) return false;
            } else {
                if (d <= min) return false;
            }
        }
        if (checkMax) {
            if (maxInclusive) {
                if (d > max) return false;
            } else {
                if (d >= max) return false;
            }
        }
        return true;
    }

    public boolean checkStringValue(String value, boolean nonNull, boolean nonEmpty, long minLength, long maxLength){
        if(nonNull && value==null){
            return false;
        }
        if(nonEmpty && value.isEmpty()){
            return false;
        }
        if(minLength>0 && value.length()<minLength){
            return false;
        }
        if(maxLength>0 && value.length()>maxLength){
            return false;
        }
        return true;
    }
}
