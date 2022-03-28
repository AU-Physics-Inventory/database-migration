package edu.andrews.cas.physics.measurement;

import edu.andrews.cas.physics.exception.OperationOnQuantitiesException;
import lombok.NonNull;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Quantity {
    @BsonProperty("amount")
    private final double value;
    private final Unit unit;

    public Quantity(double value) {
        this.value = value;
        this.unit = Unit.UNITS;
    }

    public Quantity(double value, Unit unit) {
        this.value = value;
        this.unit = unit;
    }

    public Quantity add(Quantity q) throws OperationOnQuantitiesException {
        try {
            return sameUnitsAs(q)
                    ? new Quantity(q.getValue() + this.getValue(), this.getUnit())
                    : new Quantity((
                            (Quantity) this.unit.getMeasurementClass()
                                    .getMethod("convert", Quantity.class, Unit.class)
                                    .invoke(this.unit.getMeasurementClass()
                                            .getConstructor()
                                            .newInstance(),
                                            q, this.getUnit()))
                    .getValue() + this.getValue(), this.getUnit());
        } catch (ReflectiveOperationException e) {
            throw new OperationOnQuantitiesException(e);
        }
    }

    public Quantity subtract(Quantity q) throws OperationOnQuantitiesException {
        try {
            return sameUnitsAs(q)
                    ? new Quantity(q.getValue() + this.getValue(), this.getUnit())
                    : new Quantity((
                    (Quantity) this.unit.getMeasurementClass()
                            .getMethod("convert", Quantity.class, Unit.class)
                            .invoke(this.unit.getMeasurementClass()
                                            .getConstructor()
                                            .newInstance(),
                                    q, this.getUnit()))
                    .getValue() - this.getValue(), this.getUnit());
        } catch (ReflectiveOperationException e) {
            throw new OperationOnQuantitiesException(e);
        }
    }

    public Quantity divide(Quantity q) throws OperationOnQuantitiesException {
        try {
            return sameUnitsAs(q)
                    ? new Quantity(q.getValue() + this.getValue(), this.getUnit())
                    : new Quantity((
                    (Quantity) this.unit.getMeasurementClass()
                            .getMethod("convert", Quantity.class, Unit.class)
                            .invoke(this.unit.getMeasurementClass()
                                            .getConstructor()
                                            .newInstance(),
                                    q, this.getUnit()))
                    .getValue() / this.getValue(), this.getUnit());
        } catch (ReflectiveOperationException e) {
            throw new OperationOnQuantitiesException(e);
        }
    }

    public Quantity multiply(Quantity q) throws OperationOnQuantitiesException {
        try {
            return sameUnitsAs(q)
                    ? new Quantity(q.getValue() + this.getValue(), this.getUnit())
                    : new Quantity((
                    (Quantity) this.unit.getMeasurementClass()
                            .getMethod("convert", Quantity.class, Unit.class)
                            .invoke(this.unit.getMeasurementClass()
                                            .getConstructor()
                                            .newInstance(),
                                    q, this.getUnit()))
                    .getValue() * this.getValue(), this.getUnit());
        } catch (ReflectiveOperationException e) {
            throw new OperationOnQuantitiesException(e);
        }
    }

    public double getValue() {
        return value;
    }

    public Unit getUnit() {
        return unit;
    }

    public boolean sameUnitsAs(@NonNull Quantity q) {
        return sameUnitsAs(q.getUnit());
    }

    public boolean sameUnitsAs(@NonNull Unit u) {
        return this.unit.equals(u);
    }

    public static boolean compareUnits(@NonNull Quantity q1, @NonNull Quantity q2) {
        return q1.getUnit().equals(q2.getUnit());
    }
}
