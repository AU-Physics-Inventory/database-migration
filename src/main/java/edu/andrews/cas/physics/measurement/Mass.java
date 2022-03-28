package edu.andrews.cas.physics.measurement;

import java.util.HashMap;
import java.util.function.Function;

public class Mass extends Measurement {
    public final static Unit siUnit = Unit.KILOGRAMS;
    private final HashMap<Unit, Function<Quantity, Quantity>> conversionsToSI = new HashMap<>();
    private final HashMap<Unit, Function<Quantity, Quantity>> conversionsFromSI = new HashMap<>();

    public Mass() {
        super(siUnit);
        this.loadConversions();
    }

    @Override
    void loadConversions() {
        conversionsToSI.put(Unit.OUNCES, quantity -> new Quantity(0.02834952 * quantity.getValue(), siUnit));
        conversionsToSI.put(Unit.POUNDS, quantity -> new Quantity(0.4535924 * quantity.getValue(), siUnit));
        conversionsToSI.put(Unit.MILLIGRAMS, quantity -> new Quantity(1e-6 * quantity.getValue(), siUnit));
        conversionsToSI.put(Unit.GRAMS, quantity -> new Quantity(0.001 * quantity.getValue(), siUnit));
        conversionsToSI.put(Unit.KILOGRAMS, quantity -> quantity);

        conversionsFromSI.put(Unit.OUNCES, quantity -> new Quantity(35.27396 * quantity.getValue(), siUnit));
        conversionsFromSI.put(Unit.POUNDS, quantity -> new Quantity(2.204623 * quantity.getValue(), siUnit));
        conversionsFromSI.put(Unit.MILLIGRAMS, quantity -> new Quantity(1e6 * quantity.getValue(), siUnit));
        conversionsFromSI.put(Unit.GRAMS, quantity -> new Quantity(1e3 * quantity.getValue(), siUnit));
        conversionsFromSI.put(Unit.KILOGRAMS, quantity -> quantity);

        super.loadConversions(conversionsToSI, conversionsFromSI);
    }
}
