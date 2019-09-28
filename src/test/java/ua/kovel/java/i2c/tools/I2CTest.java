/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kovel.java.i2c.tools;

import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ihor
 */
public class I2CTest {

    @Test
    public void version() {
        I2C i2c = new I2C(9, 0x76);
        assertEquals("0.0.1", i2c.version());
    }

    @Test
    public void bme280() throws IOException {
        I2C i2c = new I2C(9, 0x76);
        assertEquals(0x60, i2c.readByte(0xD0));
    }
}
