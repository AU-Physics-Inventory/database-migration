package edu.andrews.cas.physics.measurement;

import java.util.HashMap;
import java.util.function.Function;

public class Length extends Measurement {
    private final HashMap<Unit, Function<Quantity, Quantity>> conversionsToSI = new HashMap<>();
    private final HashMap<Unit, Function<Quantity, Quantity>> conversionsFromSI = new HashMap<>();
    public final static Unit siUnit = Unit.METERS;

    public Length() {
        super(siUnit);
        this.loadConversions();
    }

    @Override
    void loadConversions() {
        conversionsToSI.put(Unit.INCHES, quantity -> new Quantity(0.0254 * quantity.getValue(), siUnit));
        conversionsToSI.put(Unit.FEET, quantity -> new Quantity(0.3048 * quantity.getValue(), siUnit));
        conversionsToSI.put(Unit.YARDS, quantity -> new Quantity(0.9144 * quantity.getValue(), siUnit));
        conversionsToSI.put(Unit.MILLIMETERS, quantity -> new Quantity(0.001 * quantity.getValue(), siUnit));
        conversionsToSI.put(Unit.CENTIMETERS, quantity -> new Quantity(0.01 * quantity.getValue(), siUnit));
        conversionsToSI.put(Unit.METERS, quantity -> quantity);

        conversionsFromSI.put(Unit.INCHES, quantity -> new Quantity(39.37008 * quantity.getValue(), Unit.INCHES));
        conversionsFromSI.put(Unit.FEET, quantity -> new Quantity(3.28084 * quantity.getValue(), Unit.FEET));
        conversionsFromSI.put(Unit.YARDS, quantity -> new Quantity(1.093613 * quantity.getValue(), Unit.YARDS));
        conversionsFromSI.put(Unit.MILLIMETERS, quantity -> new Quantity(1000 * quantity.getValue(), Unit.MILLIMETERS));
        conversionsFromSI.put(Unit.CENTIMETERS, quantity -> new Quantity(100 * quantity.getValue(), Unit.CENTIMETERS));
        conversionsFromSI.put(Unit.METERS, quantity -> quantity);

        super.loadConversions(conversionsToSI, conversionsFromSI);
    }
}