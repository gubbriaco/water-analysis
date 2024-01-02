package org.water.bluetooth.application.executor.connection;

import org.water.bluetooth.application.EnvironmentType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Properties;

/**
 * The Connection class provides methods for managing Bluetooth communication and
 * checking the allowed devices based on the configuration file.
 * It includes functionality to verify if a remote device is allowed for Bluetooth communication
 * and to retrieve the current environment type.
 *
 * <p>
 * Usage:
 * <pre>
 * // Check if a remote device is allowed
 * boolean isAllowed = Connection.allowed("RemoteDeviceName");
 *
 * // Retrieve the current environment type
 * EnvironmentType currentEnvironment = Connection.getEnvironment();
 * </pre>
 * </p>
 *
 * <p>
 * The class uses the "config.properties" file located in the "src/main/resources" directory
 * to store configuration properties such as allowed devices and the current environment.
 * </p>
 *
 * @version 1.0
 * @since 2023-12-09
 * @author gubbriaco
 * @author agrandinetti
 * @author fnicoletti
 */
public class Connection {

    /**
     * Checks if the specified remote device is allowed for Bluetooth communication
     * based on the list of permitted devices read from the configuration file.
     *
     * @param remoteDeviceName The name of the remote device to be checked.
     * @return {@code true} if the remote device is allowed, {@code false} otherwise.
     * @throws IOException If an I/O error occurs while reading the configuration file.
     */
    public static boolean allowed(String remoteDeviceName) throws IOException {
        // Retrieve the names of allowed devices from the configuration file.
        String device1 = toStringProperties("DEVICE1");
        String device2 = toStringProperties("DEVICE2");
        String device3 = toStringProperties("DEVICE3");

        // Check whether the remote device matches any of the allowed devices.
        return remoteDeviceName.equalsIgnoreCase(device1) ||
                remoteDeviceName.equalsIgnoreCase(device2) ||
                remoteDeviceName.equalsIgnoreCase(device3);
    }


    /**
     * Retrieves the current environment type based on the "ENVIRONMENT" property
     * read from the configuration file.
     *
     * @return The {@link EnvironmentType} representing the current environment.
     * @throws IOException              If an I/O error occurs while reading the configuration file.
     * @throws NullPointerException     If the "ENVIRONMENT" property is not found or has an invalid value.
     */
    public static EnvironmentType getEnvironment() throws IOException, NullPointerException {
        // Retrieve the "ENVIRONMENT" property value from the configuration file.
        String environmentStr = toStringProperties("ENVIRONMENT");

        // Map the environment string to the corresponding EnvironmentType.
        EnvironmentType environment = null;
        if (environmentStr.equalsIgnoreCase("HOME")) {
            environment = EnvironmentType.HOME;
        } else if (environmentStr.equalsIgnoreCase("POOL")) {
            environment = EnvironmentType.POOL;
        } else if (environmentStr.equalsIgnoreCase("SEA")) {
            environment = EnvironmentType.SEA;
        } else {
            // Throw an exception if the environment property is not valid.
            throw new NullPointerException("Environment property not valid.");
        }

        // Return the determined environment type.
        return environment;
    }


    /**
     * Converts a specified property key to its corresponding value by reading the configuration
     * from the "config.properties" file.
     *
     * @param property The key of the property whose value needs to be retrieved.
     * @return The value associated with the specified property key.
     * @throws IOException If an I/O error occurs while reading the configuration file.
     */
    public static String toStringProperties(String property) throws IOException {
        // Get the current working directory path.
        Path path = FileSystems.getDefault().getPath("");
        String directoryName = path.toAbsolutePath().toString();

        // Construct the absolute path to the "config.properties" file.
        File configPropertiesFile = new File(
                directoryName + "/src/main/resources/config.properties"
        );

        // Load the properties from the configuration file.
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream(
                configPropertiesFile.getAbsolutePath()
        );
        properties.load(fileInputStream);

        // Retrieve the value associated with the specified property key.
        String propertyStr = properties.getProperty(property);

        return propertyStr;
    }

}
