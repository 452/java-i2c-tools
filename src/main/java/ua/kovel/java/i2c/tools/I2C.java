package ua.kovel.java.i2c.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author ihor.lavryniuk
 */
public class I2C {

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    private final int adapter;
    private final int deviceAddress;

    public I2C(int adapter, int deviceAddress) {
        this.adapter = adapter;
        this.deviceAddress = deviceAddress;
    }

    public I2C(String i2cAdapterName, int deviceAddress) throws IOException {
        this.adapter = availableAdapters().get(i2cAdapterName).getId();
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

    public void readBytes(int memoryAddress, byte[] buffer, int length) throws Exception {
        if (buffer == null) {
            throw new Exception("Buffer not initialized");
        }
        if (buffer.length < length) {
            throw new Exception("Buffer size cannot be smaller than length of requested data");
        }
        byte[] data = readBytes(memoryAddress, length);
        for (int i = 0; i < length; i++) {
            buffer[i] = data[i];
        }
    }

    public int readByte(int memoryAddress) throws IOException {
        String cmd = "i2cget -f -y " + Integer.toString(adapter) + " 0x" + Integer.toHexString(deviceAddress) + " 0x" + Integer.toHexString(memoryAddress);
        return Integer.parseInt(exec(cmd).findFirst().get().substring(2), 16);
    }

    public String readByteAsHex(int memoryAddress) throws IOException {
        return Integer.toHexString(readByte(memoryAddress));
    }

    public void writeByte(int address, int command) throws IOException {
        String cmd = "i2cset -f -y " + Integer.toString(adapter) + " 0x" + Integer.toHexString(deviceAddress) + " 0x" + Integer.toHexString(address) + " 0x" + Integer.toHexString(command);
        exec(cmd);
    }

    public static Map<String, Adapter> availableAdapters() throws IOException {
        Map<String, Adapter> result = new HashMap();
        String cmd = "i2cdetect -y -l";
        List<String> detectedAdapters = exec(cmd).collect(Collectors.toList());
        Pattern namePattern = Pattern.compile("(i2c-)(\\d)");
        Pattern typePattern = Pattern.compile("(?<=\\t)(i2c|smbus)(?=\\s)");
        Pattern descPattern = Pattern.compile("(?<=\\s\\t).*\\d(?=\\s)");
        for (String adapterInfoLine : detectedAdapters) {
            Adapter adapter = new Adapter();
            Matcher name = namePattern.matcher(adapterInfoLine);
            Matcher type = typePattern.matcher(adapterInfoLine);
            Matcher desc = descPattern.matcher(adapterInfoLine);
            if (name.find()) {
                adapter.setId(Integer.valueOf(name.group(2)));
            }
            if (type.find()) {
                adapter.setType(type.group(0));
            }
            if (desc.find()) {
                String description = desc.group(0);
                adapter.setName(description.substring(0, description.indexOf(" at ")));
                adapter.setDescription(description);
            }
            result.put(adapter.getName(), adapter);
        }
        return result;
    }

    public String version() {
        return "0.0.1";
    }

    private static Stream<String> exec(String command) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(command.split(" "));
        builder.redirectErrorStream(true);
        Process process = builder.start();
        InputStream is = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        return reader.lines();
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