package ua.kovel.java.i2c.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author ihor.lavryniuk
 */
public class I2C {

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    private final int bus;
    private final int deviceAddress;

    public I2C(int bus, int deviceAddress) {
        this.bus = bus;
        this.deviceAddress = deviceAddress;
    }

    public byte[] readBytes(int memoryAddress, int length) throws IOException {
        byte[] result = new byte[length];
        int position = 0;
        for (int i = memoryAddress; i < memoryAddress + length; i++) {
            result[position] = (byte) readByte(i);
            position++;
        }
        return result;
    }

    public int readByte(int memoryAddress) throws IOException {
        String cmd = "i2cget -f -y " + Integer.toString(bus) + " 0x" + Integer.toHexString(deviceAddress) + " 0x" + Integer.toHexString(memoryAddress);
        return Integer.parseInt(exec(cmd).substring(2), 16);
    }

    public String readByteAsHex(int memoryAddress) throws IOException {
        return Integer.toHexString(readByte(memoryAddress));
    }

    public void writeByte(int address, int command) throws IOException {
        String cmd = "i2cset -f -y " + Integer.toString(bus) + " 0x" + Integer.toHexString(deviceAddress) + " 0x" + Integer.toHexString(address) + " 0x" + Integer.toHexString(command);
        exec(cmd);
    }

    public String version() {
        return "0.0.1";
    }

    private String exec(String command) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(command.split(" "));
        builder.redirectErrorStream(true);
        Process process = builder.start();
        InputStream is = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        return reader.readLine();
    }
    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
