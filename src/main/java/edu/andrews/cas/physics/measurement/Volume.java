package edu.andrews.cas.physics.measurement;

import java.util.HashMap;
import java.util.function.Function;

public class Volume extends Measurement {
    public final static Unit siUnit = Unit.CUBIC_METERS;
    private final HashMap<Unit, Function<Quantity, Quantity>> conversionsToSI = new HashMap<>();
    private final HashMap<Unit, Function<Quantity, Quantity>> conversionsFromSI = new HashMap<>();

    public Volume() {
        super(siUnit);
        this.loadConversions();
    }

    @Override
    void loadConversions() {
        conversionsToSI.put(Unit.TEASPOONS, quantity -> new Quantity(4.929e-6 * quantity.getValue(), siUnit));
        conversionsToSI.put(Unit.TABLESPOONS, quantity -> new Quantity(1.479e-5 * quantity.getValue(), siUnit));
        conversionsToSI.put(Unit.PINTS, quantity -> new Quantity(4.732e-4 * quantity.getValue(), siUnit));
        conversionsToSI.put(Unit.QUARTS, quantity -> new Quantity(9.464e-4 * quantity.getValue(), siUnit));
        conversionsToSI.put(Unit.FLUID_OUNCES, quantity -> new Quantity(2.957e-5 * quantity.getValue(), siUnit));
        conversionsToSI.put(Unit.CUPS, quantity -> new Quantity(2.366e-4 * quantity.getValue(), siUnit));
        conversionsToSI.put(Unit.GALLONS, quantity -> new Quantity(0.003785 * quantity.getValue(), siUnit));
        conversionsToSI.put(Unit.MILLILITERS, quantity -> new Quantity(1e-6 * quantity.getValue(), siUnit));
        conversionsToSI.put(Unit.LITERS, quantity -> new Quantity(0.001 * quantity.getValue(), siUnit));
        conversionsToSI.put(Unit.CUBIC_INCHES, quantity -> new Quantity(1.639e-5 * quantity.getValue(), siUnit));
        conversionsToSI.put(Unit.CUBIC_FEET, quantity -> new Quantity(0.02832 * quantity.getValue(), siUnit));
        conversionsToSI.put(Unit.CUBIC_YARDS, quantity -> new Quantity(0.7646 * quantity.getValue(), siUnit));
        conversionsToSI.put(Unit.CUBIC_MILLIMETERS, quantity -> new Quantity(1e-9 * quantity.getValue(), siUnit));
        conversionsToSI.put(Unit.CUBIC_CENTIMETERS, quantity -> new Quantity(1e-6 * quantity.getValue(), siUnit));
        conversionsToSI.put(Unit.CUBIC_METERS, quantity -> quantity);


        conversionsFromSI.put(Unit.TEASPOONS,
                quantity -> new Quantity(202884 * quantity.getValue(), Unit.TEASPOONS));
        conversionsFromSI.put(Unit.TABLESPOONS,
                quantity -> new Quantity(67628 * quantity.getValue(), Unit.TABLESPOONS));
        conversionsFromSI.put(Unit.PINTS, quantity -> new Quantity(2113 * quantity.getValue(), Unit.PINTS));
        conversionsFromSI.put(Unit.QUARTS, quantity -> new Quantity(1057 * quantity.getValue(), Unit.QUARTS));
        conversionsFromSI.put(Unit.FLUID_OUNCES,
                quantity -> new Quantity(33814 * quantity.getValue(), Unit.FLUID_OUNCES));
        conversionsFromSI.put(Unit.CUPS, quantity -> new Quantity(4227 * quantity.getValue(), Unit.CUPS));
        conversionsFromSI.put(Unit.GALLONS, quantity -> new Quantity(264.2 * quantity.getValue(), Unit.GALLONS));
        conversionsFromSI.put(Unit.MILLILITERS,
                quantity -> new Quantity(1e6 * quantity.getValue(), Unit.MILLILITERS));
        conversionsFromSI.put(Unit.LITERS, quantity -> new Quantity(1000 * quantity.getValue(), Unit.LITERS));
        conversionsFromSI.put(Unit.CUBIC_INCHES,
                quantity -> new Quantity(61024 * quantity.getValue(), Unit.CUBIC_INCHES));
        conversionsFromSI.put(Unit.CUBIC_FEET,
                quantity -> new Quantity(35.31 * quantity.getValue(), Unit.CUBIC_FEET));
        conversionsFromSI.put(Unit.CUBIC_YARDS,
                quantity -> new Quantity(1.308 * quantity.getValue(), Unit.CUBIC_YARDS));
        conversionsFromSI.put(Unit.CUBIC_MILLIMETERS,
                quantity -> new Quantity(1e9 * quantity.getValue(), Unit.CUBIC_MILLIMETERS));
        conversionsFromSI.put(Unit.CUBIC_CENTIMETERS,
                quantity -> new Quantity(1e6 * quantity.getValue(), Unit.CUBIC_CENTIMETERS));
        conversionsFromSI.put(Unit.CUBIC_METERS, quantity -> quantity);

        super.loadConversions(conversionsToSI, conversionsFromSI);
    }
}
