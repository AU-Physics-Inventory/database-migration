package edu.andrews.cas.physics.measurement;

import java.util.HashMap;
import java.util.function.Function;

public class Area extends Measurement {
    private final HashMap<Unit, Function<Quantity, Quantity>> conversionsToSI = new HashMap<>();
    private final HashMap<Unit, Function<Quantity, Quantity>> conversionsFromSI = new HashMap<>();
    public final static Unit siUnit = Unit.SQUARE_METERS;

    public Area() {
        super(siUnit);
        this.loadConversions();
    }

    @Override
    void loadConversions() {
        conversionsToSI.put(Unit.SQUARE_INCHES, quantity -> new Quantity(6.4516e-4 * quantity.getValue(), siUnit));
        conversionsToSI.put(Unit.SQUARE_FEET, quantity -> new Quantity(0.09290304 * quantity.getValue(), siUnit));
        conversionsToSI.put(Unit.SQUARE_YARDS, quantity -> new Quantity(0.8361274 * quantity.getValue(), siUnit));
        conversionsToSI.put(Unit.SQUARE_MILLIMETERS, quantity -> new Quantity(1e-6 * quantity.getValue(), siUnit));
        conversionsToSI.put(Unit.SQUARE_CENTIMETERS, quantity -> new Quantity(1e-4 * quantity.getValue(), siUnit));
        conversionsToSI.put(Unit.SQUARE_METERS, quantity -> quantity);

        conversionsFromSI.put(Unit.SQUARE_INCHES, quantity -> new Quantity(1550.003 * quantity.getValue(), Unit.SQUARE_INCHES));
        conversionsFromSI.put(Unit.SQUARE_FEET, quantity -> new Quantity(10.76391 * quantity.getValue(), Unit.SQUARE_FEET));
        conversionsFromSI.put(Unit.SQUARE_YARDS, quantity -> new Quantity(1.19599 * quantity.getValue(), Unit.SQUARE_YARDS));
        conversionsFromSI.put(Unit.SQUARE_MILLIMETERS, quantity -> new Quantity(1e6 * quantity.getValue(), Unit.SQUARE_MILLIMETERS));
        conversionsFromSI.put(Unit.SQUARE_CENTIMETERS, quantity -> new Quantity(10000 * quantity.getValue(), Unit.SQUARE_CENTIMETERS));
        conversionsFromSI.put(Unit.SQUARE_METERS, quantity -> quantity);

        super.loadConversions(conversionsToSI, conversionsFromSI);
    }
}
