package org.water.bluetooth.application.executor;

import org.water.bluetooth.application.executor.connection.RemoteConnection;
import org.water.bluetooth.application.utils.Logging;
import org.water.bluetooth.application.utils.UUIDgenerator;

import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.IOException;

import static org.water.bluetooth.application.executor.connection.Connection.toStringProperties;

/**
 * The BluetoothServerExecutorApplication class represents a Bluetooth server application that listens for incoming connections.
 * It implements the {@link BluetoothServerExecutor} interface and provides methods for initializing and executing the Bluetooth server.
 *
 * <p>
 * The class uses a Universally Unique Identifier (UUID) to identify the Bluetooth server and creates a StreamConnectionNotifier
 * to accept incoming Bluetooth connections.
 * </p>
 *
 * <p>
 * Usage:
 * <pre>
 * // Initialize the Bluetooth server
 * BluetoothServerExecutorApplication bluetoothServer = new BluetoothServerExecutorApplication();
 *
 * // Start the Bluetooth server and wait for incoming connections
 * bluetoothServer.execute();
 * </pre>
 * </p>
 *
 * @version 1.0
 * @since 2023-12-09
 * @author gubbriaco
 * @author agrandinetti
 * @author fnicoletti
 */
public class BluetoothServerExecutorApplication implements BluetoothServerExecutor {

    /**
     * Local device on which the Bluetooth server runs.
     */
    private final LocalDevice localDevice;

    /**
     * The URL used for Bluetooth server connection.
     */
    private final String url;

    /**
     * Universally Unique Identifier (UUID) generated for the Bluetooth server.
     */
    private final UUID uuid;

    /**
     * The notifier used to accept incoming Bluetooth connections.
     */
    private final StreamConnectionNotifier notifier;

    /**
     * Flag indicating whether the Bluetooth server is running.
     */
    private boolean running;


    /**
     * Constructs a new instance of BluetoothServerExecutorApplication.
     *
     * @throws IOException If an I/O error occurs while initializing the Bluetooth server.
     */
    public BluetoothServerExecutorApplication() throws IOException {

        // Get the local Bluetooth device.
        localDevice = LocalDevice.getLocalDevice();

        // Log the name of the local Bluetooth device.
        Logging.msg("Bluetooth server running on: " + localDevice.getFriendlyName());

        // Generate a Universally Unique Identifier (UUID) for the Bluetooth server.
        UUIDgenerator.generate();
        uuid = UUIDgenerator.getUUID();

        // Create the URL for the Bluetooth server using the generated UUID.
        url = "btspp://localhost:" + uuid.toString() + ";name=BluetoothServer";

        // Open a StreamConnectionNotifier to accept incoming Bluetooth connections.
        notifier = (StreamConnectionNotifier) Connector.open(url);

        // Log the type of environment where devices is located.
        Logging.msg(
                "ENVIRONMENT: " + toStringProperties("ENVIRONMENT")
        );

        // Set the running flag to true, indicating that the Bluetooth server is ready to accept connections.
        running = true;

    }


    /**
     * Executes the Bluetooth server, waiting for incoming connections.
     *
     * @throws IOException If an I/O error occurs while waiting for connections.
     */
    @Override
    public void execute() throws IOException {

        Logging.msg("Waiting for connections...");

        while (running) {
            // Create a new thread for each incoming connection
            Thread remoteConnection = new RemoteConnection(localDevice, notifier);
            remoteConnection.start();
        }

    }

}
