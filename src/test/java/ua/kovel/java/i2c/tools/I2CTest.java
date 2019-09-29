/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kovel.java.i2c.tools;

import java.io.IOException;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ihor
 */
public class I2CTest {

    private static I2C i2c = null;

    @BeforeClass
    public static void before() throws IOException {
        i2c = new I2C("i2c-tiny-usb", 0x76);
    }

    @Test
    public void version() {
        assertEquals("0.0.1", i2c.version());
    }

    @Test
    public void bme280() throws IOException {
        assertEquals(0x60, i2c.readByte(0xD0));
    }

    @Test
    public void i2cAdaptersList() throws IOException {
        Map<String, Adapter> adapters = I2C.availableAdapters();
        assertTrue(adapters.size() > 0);
    }
}
