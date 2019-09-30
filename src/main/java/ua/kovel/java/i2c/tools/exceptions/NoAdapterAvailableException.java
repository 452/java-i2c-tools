package ua.kovel.java.i2c.tools.exceptions;

public class NoAdapterAvailableException extends I2CException {

    public NoAdapterAvailableException(String message) {
        super("Adapter with name " + message + " not available");
    }
}