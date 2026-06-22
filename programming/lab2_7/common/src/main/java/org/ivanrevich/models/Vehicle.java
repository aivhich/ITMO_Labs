package org.ivanrevich.models;

import org.ivanrevich.annotations.*;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;


/**
 * Модель транспортного средства.
 * <p>
 * Представляет транспортное средство с полями: ID, имя, координаты,
 * дата создания, мощность двигателя, количество колёс, тип и тип топлива.
 * Реализует Comparable для сортировки по ID.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 * @see Coordinates
 * @see VehicleType
 * @see FuelType
 * @see Comparable
 */
@Entity
@Table(name = "vehicles")
public class Vehicle implements Comparable<Vehicle>, Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    @Column(name="name")
    private String name; //Поле не может быть null, Строка не может быть пустой

    @Embedded(overrides = {
            @AttributeOverride(field = "x", column = "coord_x"),
            @AttributeOverride(field = "y", column = "coord_y")
    })
    private Coordinates coordinates; //Поле не может быть null

    @Column(name="creationDate")
    private Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    @Column(name="enginePower")
    private Float enginePower; //Поле не может быть null, Значение поля должно быть больше 0

    @Column(name="numberOfWheels")
    private long numberOfWheels; //Значение поля должно быть больше 0

    @Column(name="type")
    private VehicleType type; //Поле не может быть null

    @Column(name="fuelType")
    private FuelType fuelType; //Поле не может быть null

    @Column(name="author")
    private Integer authorId;


    public String toCsvString() {
        return id +
                "," + escapeCSV(name) +
                "," + coordinates.getX()+
                "," + coordinates.getY()+
                "," + creationDate.toInstant().toString() +
                "," + enginePower +
                "," + numberOfWheels +
                "," + type +
                "," + fuelType+
                "," + authorId;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String escapeCSV(String field) {
        if (field == null) return "";
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            field = field.replace("\"", "\"\"");
            field = "\"" + field + "\"";
        }
        return field;
    }

    public Vehicle() {
    }

    public Vehicle(int id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Vehicle vehicle)) return false;
        return id == vehicle.id
                && numberOfWheels == vehicle.numberOfWheels
                && Objects.equals(name, vehicle.name)
                && Objects.equals(coordinates, vehicle.coordinates)
                && Objects.equals(creationDate, vehicle.creationDate)
                && Objects.equals(enginePower, vehicle.enginePower)
                && type == vehicle.type
                && fuelType == vehicle.fuelType
                && Objects.equals(authorId, vehicle.authorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, enginePower, numberOfWheels, type, fuelType, authorId);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", enginePower=" + enginePower +
                ", numberOfWheels=" + numberOfWheels +
                ", type=" + type +
                ", fuelType=" + fuelType +
                ", authorId="+authorId+'}';
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    public void setEnginePower(Float enginePower) {
        this.enginePower = enginePower;
    }
    public void setNumberOfWheels(long numberOfWheels) {
        this.numberOfWheels = numberOfWheels;
    }
    public void setType(VehicleType type) {
        this.type = type;
    }
    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Float getEnginePower() {
        return enginePower;
    }

    public long getNumberOfWheels() {
        return numberOfWheels;
    }

    public VehicleType getType() {
        return type;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    @Override
    public int compareTo(Vehicle other) {
        return Comparator
                .comparingDouble(Vehicle::getEnginePower)
                .thenComparingLong(Vehicle::getNumberOfWheels)
                .compare(this, other);
    }
}