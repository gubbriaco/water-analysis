package org.water.bluetooth.application.executor.connection;

import org.water.bluetooth.application.utils.Logging;

import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.IOException;
import java.io.InputStream;

/**
 * Represents a thread handling an incoming Bluetooth connection request.
 * This class extends Thread and is designed to be instantiated for each incoming connection.
 */
public class RemoteConnection extends Thread {

    /**
     * The local Bluetooth device on which the connection is accepted.
     */
    private final LocalDevice localDevice;

    /**
     * The established stream connection to the remote device.
     */
    private final StreamConnection connection;

    /**
     * The remote Bluetooth device attempting to establish a connection.
     */
    private final RemoteDevice remoteDevice;

    /**
     * The friendly name of the remote Bluetooth device.
     */
    private final String remoteDeviceName;


    /**
     * Constructs a new instance of RemoteConnection.
     *
     * @param localDevice The local Bluetooth device.
     * @param notifier    The StreamConnectionNotifier used for accepting incoming connections.
     * @throws IOException If an I/O error occurs during the connection setup.
     */
    public RemoteConnection(LocalDevice localDevice, StreamConnectionNotifier notifier) throws IOException {
        this.localDevice = localDevice;

        // The incoming connection is provisionally accepted.
        this.connection = notifier.acceptAndOpen();

        // The remote device that is trying to establish a Bluetooth connection with the local device is
        // intercepted.
        this.remoteDevice = RemoteDevice.getRemoteDevice(connection);
        this.remoteDeviceName = remoteDevice.getFriendlyName(true);
    }


    /**
     * Executes the thread logic for handling the Bluetooth connection.
     */
    @Override
    public void run() {

        try {

            // If the remote device is in the list of permitted remote devices, then the remote connection is
            // established; otherwise, the established temporary connection is closed.
            if (true) {

                Logging.msg(
                        remoteDeviceName + " connected to " + localDevice.getFriendlyName()
                );

                try {

                    InputStream inputStream = connection.openInputStream();
                    StringBuilder receivedData = new StringBuilder();
                    int character;

                    while ((character = inputStream.read()) != -1) {
                        char receivedChar = (char) character;

                        // The ; character at the end of the message is excluded.
                        if (receivedChar != ';') {
                            receivedData.append(receivedChar);
                        }

                        // If the current character is ;, then reading of the message is finished, and the text can be
                        // displayed on the screen.
                        String data;
                        if (receivedChar == ';') {
                            data = receivedData.toString().trim();
                            Logging.msg(
                                    "temperature_received_from_" + remoteDeviceName + ": " + data
                            );
                            // Reset buffer for the next message
                            receivedData.setLength(0);
                        }
                    }

                    // At the end of the data flow, the connection is closed.
                    connection.close();
                    Logging.msg(
                            "The connection between " + localDevice + " and " + remoteDevice + " has been closed."
                    );

                } catch (IOException e) {
                    Logging.msg(
                            "Error during data flow."
                    );
                }

            } else {

                Logging.msg(
                        "Connection refused. Device not recognized: " + remoteDeviceName
                );
                connection.close();

            }

        } catch (IOException e) {
            Logging.msg(
                    "Remote Connection Error."
            );
        }

    }

}
