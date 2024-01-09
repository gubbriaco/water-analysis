package org.water.bluetooth.application.executor.connection;

import org.water.bluetooth.application.EnvironmentType;
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
import static org.water.bluetooth.application.executor.connection.Connection.getEnvironment;

/**
 * The RemoteConnection class represents a thread handling an incoming Bluetooth connection request.
 * This class extends Thread and is designed to be instantiated for each incoming connection.
 *
 * <p>
 * The class includes methods for handling the Bluetooth connection, extracting data from the incoming stream,
 * and performing HTTP POST requests for temperature, dissolved metals, and pH data to a server.
 * </p>
 *
 * <p>
 * It uses the "config.properties" file located in the "src/main/resources" directory for configuration properties
 * such as allowed devices and the current environment.
 * </p>
 *
 * <p>
 * The class also includes methods for extracting details about dissolved metals and pH based on their values.
 * </p>
 *
 * @version 1.0
 * @since 2023-12-09
 * @author gubbriaco
 * @author agrandinetti
 * @author fnicoletti
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
                            if (values.length >= 1 && !values[0].equals("nan") && !values[1].equals("nan") && !values[2].equals("nan")) {
                                // Get current date and time
                                LocalDateTime currentTime = LocalDateTime.now();
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                String request= makeJsonString(values);
                                HTTPPOST httppost= new HTTPPOST(request);
                                //Logging.msg(httppost.getResponse());

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
                            "The connection between " + localDevice.getBluetoothAddress() + " (base station) and " + remoteDevice + " has been closed."
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
            details += "Hard water. ";
        } else if (dissolvedMetals >= 200 && dissolvedMetals < 400) {
            details += "Average tap water. ";
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
            details += "Acid. ";
        } else if (pH >= 7 && pH < 7.5) {
            details += "Neutral. ";
        } else if (pH >= 7.5 && pH <= 14) {
            details += "Base. ";
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
    public String makeJsonString(String[] values){
        StringBuilder sb = new StringBuilder();
        sb.append("{\"deviceAddress\":\""+remoteDevice.getBluetoothAddress()+"\",");
        sb.append("\"temperature\":"+values[0]+",");
        sb.append("\"dissolvedMetals\":"+values[1]+",");
        sb.append("\"ph\":"+values[2]+"}");
        return sb.toString();
    }


}
