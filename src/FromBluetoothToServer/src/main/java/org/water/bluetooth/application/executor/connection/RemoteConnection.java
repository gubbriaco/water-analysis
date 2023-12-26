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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.water.bluetooth.application.executor.connection.Connection.allowed;

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
            //if (allowed(remoteDeviceName)) {

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
                            // Split the received data into individual values
                            String[] values = data.split(",");
                            // Check if the array has at least three elements
                            if (values.length >= 3) {
                                String temperature = values[0];
                                String dissolvedMetals = values[1];
                                String ph = values[2];

                                // Get current date and time
                                LocalDateTime currentTime = LocalDateTime.now();
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                                // Print the formatted output with current date and time
                                Logging.msg("");
                                Logging.msg("....................................................................");
                                Logging.msg(currentTime.format(formatter));
                                Logging.msg("#  Temperature = " + temperature + " Celsius");
                                Logging.msg("#  Dissolved Metals = " + dissolvedMetals + " PPM");
                                Logging.msg(" |--> " + getDissolvedMetalsDetails(dissolvedMetals));
                                Logging.msg("#  pH = " + ph);
                                Logging.msg(" |--> " + getPhDetails(ph));
                            } else {
                                Logging.msg("Invalid data format: " + data);
                            }
                            // Reset buffer for the next message
                            receivedData.setLength(0);

                            // Performs an HTTP POST request for temperature, dissolved metals, and pH data to a server.
                            //httpPostRequest(data);

                        }
                    }

                    // At the end of the data flow, the connection is closed.
                    connection.close();
                    Logging.msg(
                            "The connection between " + localDevice + " and " + remoteDevice + " has been closed."
                    );

//                } catch (InterruptedException e) {
//                    Logging.msg(
//                            "Error during HTTP POST Request. \n Interrupted Exception."
//                    );
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


    /**
     * Determines the dissolved metals details based on the provided dissolved metals value.
     *
     * @param dissolvedMetalsString The dissolved metals value as a string.
     * @return Details about the dissolved metals, such as water quality information.
     * @throws IllegalArgumentException If the provided dissolved metals value is invalid or out of range.
     */
    private String getDissolvedMetalsDetails(String dissolvedMetalsString) throws IllegalArgumentException {
        float dissolvedMetals = Float.parseFloat(dissolvedMetalsString);
        String details = "";

        if (dissolvedMetals >= 0 && dissolvedMetals < 200) {
            details += "Hard water.";
        } else if (dissolvedMetals >= 200 && dissolvedMetals < 400) {
            details += "Average tap water.";
        }

        if (dissolvedMetals >= 0 && dissolvedMetals < 50) {
            return details + "Ideal drinking water from reverse osmosis, deionization, microfiltration, distillation, etc.";
        } else if (dissolvedMetals >= 50 && dissolvedMetals < 200) {
            return details + "Carbon filtration, mountain springs or aquifers.";
        } else if (dissolvedMetals >= 200 && dissolvedMetals < 300) {
            return details + "Marginally acceptable.";
        } else if (dissolvedMetals >= 300 && dissolvedMetals < 500) {
            return details + "High TDS water from the tap or mineral springs.";
        } else if (dissolvedMetals >= 500) {
            return details + "U.S. EPA's maximum contamination level.";
        }

        return details + "Invalid dissolved metals value: " + dissolvedMetalsString;
        //throw new IllegalArgumentException("Invalid dissolved metals value: " + dissolvedMetalsString);
    }


    /**
     * Determines the pH details based on the provided pH value.
     *
     * @param pHString The pH value as a string.
     * @return Details about the pH, such as acidity or alkalinity, and equivalent substances.
     * @throws IllegalArgumentException If the provided pH value is invalid or out of range.
     */
    private String getPhDetails(String pHString) throws IllegalArgumentException {
        float pH = Float.parseFloat(pHString);
        String details = "";

        if (pH >= 0 && pH < 7) {
            details += "Acid.";
        } else if (pH >= 7 && pH < 7.5) {
            details += "Neutral.";
        } else if (pH >= 7.5 && pH <= 14) {
            details += "Base.";
        }

        details += "Equivalent to ";

        if (pH >= 0 && pH < 1) {
            return details + "battery acid.";
        } else if (pH >= 1.0 && pH < 3.3) {
            return details + "gastric acid";
        } else if (pH >= 3.3 && pH < 4.2) {
            return details + "orange juice.";
        } else if (pH >= 4.2 && pH < 5) {
            return details + "vinegar.";
        } else if (pH >= 5 && pH < 5.03) {
            return details + "black coffee.";
        } else if (pH >= 5.03 && pH < 6.8) {
            return details + "milk.";
        } else if (pH >= 6.8 && pH < 7.5) {
            return details + "pure water at 25 Celsius.";
        } else if (pH >= 7.5 && pH < 8.4) {
            return details + "sea water.";
        } else if (pH >= 8.4 && pH < 11.5) {
            return details + "ammonia.";
        } else if (pH >= 11.5 && pH < 12.5) {
            return details + "bleach.";
        } else if (pH >= 12.5 && pH <= 14) {
            return details + "1 M NaOH (Sodium Hydroxide).";
        }

        return details + "Invalid pH value: " + pHString;
        //throw new IllegalArgumentException("Invalid pH value: " + pHString);
    }


}
