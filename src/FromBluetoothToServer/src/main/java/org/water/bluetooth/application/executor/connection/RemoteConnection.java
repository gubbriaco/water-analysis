package org.water.bluetooth.application.executor.connection;

import org.water.bluetooth.application.executor.connection.http.DataType;
import org.water.bluetooth.application.executor.connection.http.HTTPPOST;
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
            // if (allowed(remoteDeviceName)) {
                
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

                            // Performs an HTTP POST request for temperature, dissolved metals, and pH data to a server.
                            httpPostRequest(data);

                        }
                    }

                    // At the end of the data flow, the connection is closed.
                    connection.close();
                    Logging.msg(
                            "The connection between " + localDevice + " and " + remoteDevice + " has been closed."
                    );

                } catch (InterruptedException e) {
                    Logging.msg(
                            "Error during HTTP POST Request. \n Interrupted Exception."
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


    /**
     * Performs an HTTP POST request for temperature, dissolved metals, and pH data to a server.
     *
     * @param data The data string containing values separated by commas and semicolons (e.g., "24.12, 4325, 7.8;").
     * @throws IOException              If an I/O error occurs while making the HTTP POST request.
     * @throws InterruptedException     If the current thread is interrupted while waiting for the HTTP POST threads to
     *                                  complete.
     * @throws IllegalArgumentException If the provided data string does not contain valid temperature, dissolved
     *                                  metals, or pH data.
     */
    private void httpPostRequest(String data) throws
            IOException,
            InterruptedException,
            IllegalArgumentException {

        String temperature = getTemperature(data);
        String dissolvedMetals = getDissolvedMetals(data);
        String ph = getPh(data);

        // Create and start separate threads for each HTTP POST request
        Thread temperatureHttpPost = new HTTPPOST(temperature, DataType.TEMPERATURE);
        Thread dissolvedMetalsHttpPost = new HTTPPOST(dissolvedMetals, DataType.DISSOLVED_METALS);
        Thread phHttpPost = new HTTPPOST(ph, DataType.PH);

        // Start each thread
        temperatureHttpPost.start();
        dissolvedMetalsHttpPost.start();
        phHttpPost.start();

        // Wait for each thread to complete before continuing
        temperatureHttpPost.join();
        dissolvedMetalsHttpPost.join();
        phHttpPost.join();

    }



    /**
     * Extracts the temperature value from the provided data string.
     *
     * @param data The data string containing values separated by commas and semicolons (e.g., "24.12, 4325, 7.8;").
     * @return The temperature value extracted from the data string.
     * @throws IllegalArgumentException If the provided data string does not contain valid temperature data.
     */
    private String getTemperature(String data) throws IllegalArgumentException {

        String[] values = data.split("[,;]");
        if (values.length > 0) {
            return values[0];
        } else {
            throw new IllegalArgumentException("Illegal temperature data: " + data);
        }

    }


    /**
     * Extracts the dissolved metals value from the provided data string.
     *
     * @param data The data string containing values separated by commas and semicolons (e.g., "24.12, 4325, 7.8;").
     * @return The dissolved metals value extracted from the data string.
     * @throws IllegalArgumentException If the provided data string does not contain valid dissolved metals data.
     */
    private String getDissolvedMetals(String data) {

        String[] values = data.split("[,;]");
        if (values.length > 1) {
            return values[1];
        } else {
            throw new IllegalArgumentException("Illegal dissolved metals data: " + data);
        }

    }


    /**
     * Extracts the pH value from the provided data string.
     *
     * @param data The data string containing values separated by commas and semicolons (e.g., "24.12, 4325, 7.8;").
     * @return The pH value extracted from the data string.
     * @throws IllegalArgumentException If the provided data string does not contain valid pH data.
     */
    private String getPh(String data) {

        String[] values = data.split("[,;]");
        if (values.length > 2) {
            return values[2];
        } else {
            throw new IllegalArgumentException("Illegal pH data: " + data);
        }

    }

}
