package org.ivanrevich.models;


import java.io.Serializable;

/**
 * Модель координат.
 * <p>
 * Содержит координаты X и Y с ограничениями:
 * X > -371, Y <= 376, оба поля не могут быть null.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 */
public class Coordinates implements Serializable {
    private Double x; //Значение поля должно быть больше -371, Поле не может быть null
    private Float y; //Максимальное значение поля: 376, Поле не может быть null

    public Double getX() {
        return x;
    }

    public Float getY() {
        return y;
    }

    public Coordinates() {
    }

    public Coordinates(Double x, Float y) {
        this.x = x;
        this.y = y;
    }

    public void setY(Float y) {
        this.y = y;
    }
    public void setX(Double x) {
        this.x = x;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
