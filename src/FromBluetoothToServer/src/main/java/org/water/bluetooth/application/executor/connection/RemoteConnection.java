package org.water.bluetooth.application.executor.connection;

import org.water.bluetooth.application.utils.Logging;

import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.microedition.io.StreamConnection;
import java.io.IOException;
import java.io.InputStream;

public class RemoteConnection extends Thread {

    private final LocalDevice localDevice;
    private final String localDeviceName;

    private final RemoteDevice remoteDevice;
    private final String remoteDeviceName;
    private final StreamConnection connection;

    private InputStream inputStream;
    private StringBuilder receivedData;


    public RemoteConnection(
            LocalDevice localDevice,
            RemoteDevice remoteDevice,
            StreamConnection connection
    ) throws IOException {

        this.localDevice = localDevice;
        localDeviceName = localDevice.getFriendlyName();
        if (remoteDevice == null) {
            throw new IOException();
        }
        this.remoteDevice = remoteDevice;
        this.remoteDeviceName = remoteDevice.getFriendlyName(true);

        this.connection = connection;

    }


    @Override public void run() {

        Logging.msg(
                remoteDeviceName + " connected to " + localDeviceName
        );

        try {

            inputStream = connection.openInputStream();
            receivedData = new StringBuilder();
            int character;

            while ( (character = inputStream.read()) != -1 ) {
                char receivedChar = (char) character;

                // The ; character at the end of the message is excluded.
                if (receivedChar != ';') {
                    receivedData.append(receivedChar);
                }

                // If the current character is ; then reading of the message is finished and the text can be displayed
                // on the screen.
                String data;
                if ( receivedChar == ';' ) {
                    data = receivedData.toString().trim();
                    Logging.msg(
                            "temperature_received: " + data
                    );
                    // Reset buffer for next message
                    receivedData.setLength(0);
                }
            }

            // At the end of the data flow, the connection is closed.
            connection.close();
            Logging.msg(
                    "The connection between " +  localDevice + " and " +  remoteDevice + " has been closed."
            );

        } catch (IOException e) {
            Logging.msg(
                    "Error during data flow."
            );
        }
    }


}
