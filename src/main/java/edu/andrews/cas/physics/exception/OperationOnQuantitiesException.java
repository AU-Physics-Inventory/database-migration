package edu.andrews.cas.physics.exception;

public class OperationOnQuantitiesException extends Exception {
    public OperationOnQuantitiesException(ReflectiveOperationException e) {
        super();
    }

    public OperationOnQuantitiesException(String s) {
        super(s);
    }
}
