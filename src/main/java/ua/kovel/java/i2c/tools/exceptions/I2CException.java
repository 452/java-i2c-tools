package ua.kovel.java.i2c.tools.exceptions;

import java.io.IOException;

public class I2CException extends IOException {

    public I2CException(String message) {
        super(message);
    }

    public I2CException(Exception e) {
        super(e);
    }
}