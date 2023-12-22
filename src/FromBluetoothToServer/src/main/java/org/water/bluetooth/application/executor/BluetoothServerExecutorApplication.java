package org.water.bluetooth.application.executor;

import org.water.bluetooth.application.executor.connection.RemoteConnection;
import org.water.bluetooth.application.utils.Logging;
import org.water.bluetooth.application.utils.UUIDgenerator;

import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.IOException;

/**
 * Implementation of {@link BluetoothServerExecutor}.
 * This class represents a Bluetooth server application that listens for incoming connections.
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
