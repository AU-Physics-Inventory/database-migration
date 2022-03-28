package edu.andrews.cas.physics.measurement;

public enum Unit {
    // GENERIC
    UNITS("units", Enumerable.class),

    // LENGTH
    INCHES("in", Length.class),
    FEET("ft", Length.class),
    YARDS("yd", Length.class),
    MILLIMETERS("mm", Length.class),
    CENTIMETERS("cm", Length.class),
    METERS("m", Length.class),

    // AREA
    SQUARE_INCHES("sq in", Area.class),
    SQUARE_FEET("sq ft", Area.class),
    SQUARE_YARDS("sq yd", Area.class),
    SQUARE_MILLIMETERS("sq mm", Area.class),
    SQUARE_CENTIMETERS("sq cm", Area.class),
    SQUARE_METERS("sq m", Area.class),

    // VOLUME
    CUBIC_INCHES("cu in", Volume.class),
    CUBIC_FEET("cu ft", Volume.class),
    CUBIC_YARDS("cu yd", Volume.class),
    CUBIC_MILLIMETERS("cu mm", Volume.class),
    CUBIC_CENTIMETERS("cu cm", Volume.class),
    CUBIC_METERS("cu m", Volume.class),
    CUPS("c", Volume.class),
    FLUID_OUNCES("fl oz", Volume.class),
    GALLONS("gal", Volume.class),
    LITERS("L", Volume.class),
    MILLILITERS("mL", Volume.class),
    PINTS("pt", Volume.class),
    QUARTS("qt", Volume.class),
    TABLESPOONS("tbsp", Volume.class),
    TEASPOONS("tsp", Volume.class),

    // MASS
    MILLIGRAMS("mg", Mass.class),
    GRAMS("g", Mass.class),
    KILOGRAMS("kg", Mass.class),
    OUNCES("oz", Mass.class),
    POUNDS("lb", Mass.class);

    private final String symbol;
    private final Class<?> measurementClass;

    Unit(String symbol, Class<?> measurementClass) {
        this.symbol = symbol;
        this.measurementClass = measurementClass;
    }

    public static Unit lookup(String s) throws IllegalArgumentException {
        for (Unit u : Unit.values()) {
            if (u.getSymbol().equals(s)) return u;
        }
        throw new IllegalArgumentException("Could not find Unit with the specified symbol.");
    }

    public String getSymbol() {
        return symbol;
    }

    public Class<?> getMeasurementClass() {
        return measurementClass;
    }
}