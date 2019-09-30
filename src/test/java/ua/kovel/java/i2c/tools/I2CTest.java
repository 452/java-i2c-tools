/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kovel.java.i2c.tools;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import ua.kovel.java.i2c.tools.exceptions.I2CException;
import ua.kovel.java.i2c.tools.exceptions.NoAdapterAvailableException;

import static org.junit.Assert.*;

/**
 *
 * @author ihor.lavryniuk
 */
public class I2CTest {

    private static I2C i2c = null;

    @BeforeClass
    public static void before() throws I2CException {
        i2c = new I2C("i2c-tiny-usb", 0x76);
    }

    @Test
    public void version() throws I2CException {
        assertEquals("i2cdetect version 4.1", i2c.version());
    }

    @Test
    public void bme280() throws I2CException {
        assertEquals(0x60, i2c.readByte(0xD0));
    }

    @Test
    public void i2cAdaptersList() throws I2CException {
        List<Adapter> adapters = I2C.availableAdapters();
        assertTrue(adapters.size() > 0);
    }

    @Test(expected = NoAdapterAvailableException.class)
    public void i2cInitAdapterNotAvailable() throws I2CException {
        new I2C("i2c-tiny-usb-not-exist", 0x76);
    }
}
