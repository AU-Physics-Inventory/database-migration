package edu.andrews.cas.physics.inventory.model.mongodb.maintenance;

public enum Status {
    WORKING("W"),
    CALIBRATION("C"),
    REPAIR("R"),
    TESTING("T"),
    UNKNOWN("U");

    private final String code;

    Status(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Status lookup(String s) {
        return switch (s) {
            case "W", "Working" -> WORKING;
            case "C", "Out for calibration" -> CALIBRATION;
            case "R", "Out for repair" -> REPAIR;
            case "T", "Out for testing" -> TESTING;
            case "U", "Unknown" -> UNKNOWN;
            default -> throw new IllegalArgumentException(String.format("Could not find status code: %s", s));
        };
    }

    @Override
    public String toString() {
        return code;
    }
}
