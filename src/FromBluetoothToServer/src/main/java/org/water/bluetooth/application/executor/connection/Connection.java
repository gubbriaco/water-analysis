package org.water.bluetooth.application.executor.connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Utility class for controlling remote devices allowed to communicate a data stream via Bluetooth connection to the
 * local device. Utility class for controlling remote devices allowed to communicate a data stream via Bluetooth
 * connection to the local device. Permitted devices are listed in {@link config.properties}.
 */
public class Connection {

    /**
     * This utility, by consulting the resource {@see config.properties}, checks whether or not the remote device that
     * wants to establish Bluetooth communication with the local device is actually allowed.
     * @param remoteDeviceName Name of the remote device that wants to establish a Bluetooth connection with the local
     *                         device.
     * @return Permission to communicate granted.
     * @throws IOException
     */
    public static boolean allowed(String remoteDeviceName) throws IOException {

        // Extracting permitted devices from the file {@see config.properties} containing the list of permitted devices.
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

        // Check whether the remote device that wants to establish Bluetooth communication is among the allowed devices.
        return
                remoteDeviceName.equalsIgnoreCase(device1) ||
                remoteDeviceName.equalsIgnoreCase(device2) ||
                remoteDeviceName.equalsIgnoreCase(device3);

    }

}
