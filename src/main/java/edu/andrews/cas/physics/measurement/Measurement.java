package edu.andrews.cas.physics.measurement;

import edu.andrews.cas.physics.exception.OperationOnQuantitiesException;
import lombok.NonNull;

import java.util.HashMap;
import java.util.function.Function;

public abstract class Measurement {
    private final Unit siUnit;
    private HashMap<Unit, Function<Quantity, Quantity>> conversionsToSI;
    private HashMap<Unit, Function<Quantity, Quantity>> conversionsFromSI;

    public Measurement(Unit siUnit) {
        this.siUnit = siUnit;
    }

    public Unit getSIUnit() {
        return siUnit;
    }

    void loadConversions(HashMap<Unit, Function<Quantity, Quantity>> toSI,
                         HashMap<Unit, Function<Quantity, Quantity>> fromSI) {
        this.conversionsToSI = toSI;
        this.conversionsFromSI = fromSI;
    }

    abstract void loadConversions();

    public Quantity convert(@NonNull Quantity q, @NonNull Unit to) throws OperationOnQuantitiesException {
        if (q.getUnit().getMeasurementClass().equals(to.getMeasurementClass()))
            return conversionsFromSI.get(to).apply(conversionsToSI.get(q.getUnit()).apply(q));
        else throw new OperationOnQuantitiesException("Units must be of the same measurement to be converted.");
    }
}
