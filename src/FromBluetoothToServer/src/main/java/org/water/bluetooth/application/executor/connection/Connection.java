package org.water.bluetooth.application.executor.connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Properties;

public class Connection {

    public static boolean allowed(String remoteDeviceName) throws IOException {

        Path path = FileSystems.getDefault().getPath("");
        String directoryName = path.toAbsolutePath().toString();
        File allowedDevices = new File(
                directoryName + "/src/main/resources/config.properties"
        );
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream(
                allowedDevices.getAbsolutePath()
        );
        properties.load(fileInputStream);
        String device1 = properties.getProperty("DEVICE1");
        String device2 = properties.getProperty("DEVICE2");
        String device3 = properties.getProperty("DEVICE3");

        return
                remoteDeviceName.equalsIgnoreCase(device1) ||
                remoteDeviceName.equalsIgnoreCase(device2) ||
                remoteDeviceName.equalsIgnoreCase(device3);
    }


}
