# java-i2c-tools

Java i2c-tools wrapper

## How to build and use
Build
```sh
./gradlew publishMavenJavaPublicationToMavenLocal
```
add to your pom.xml
```xml
<dependency>
    <groupId>com.github.452</groupId>
    <artifactId>java-i2c-tools</artifactId>
    <version>0.0.1</version>
</dependency>
```

Install i2c-tools
```sh
sudo apt install i2c-tools
# if you need permissions to your i2c bus
sudo chown $USER:$USER /dev/i2c-9
```

## i2c-tools basics commands example
```sh
# all instantiated I2C adapters
i2cdetect -l
# Get the list of detected peripherals on the specific I2C bus
i2cdetect -y 9
# Read all the registers from a peripheral
i2cdump -f -y 9 0x76
# read directly one register
i2cget -f -y 9 0x76 0xD0
# write directly a register
i2cset -f -y 0 0x5f 0x0f 0xac
```

## Info
https://wiki.st.com/stm32mpu/wiki/I2C_i2c-tools