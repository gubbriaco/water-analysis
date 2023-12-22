package org.water.bluetooth.application.executor;

import org.water.bluetooth.application.executor.connection.RemoteConnection;
import org.water.bluetooth.application.utils.Logging;
import org.water.bluetooth.application.utils.UUIDgenerator;

import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.IOException;
import java.io.InputStream;

public class BluetoothServerExecutorApplication implements BluetoothServerExecutor {

    private final LocalDevice localDevice;
    private final String url;
    private final UUID uuid;

    private final StreamConnectionNotifier notifier;

    private boolean running;


    public BluetoothServerExecutorApplication() throws IOException {
        localDevice = LocalDevice.getLocalDevice();
        Logging.msg(
                "Bluetooth server running on: " + localDevice.getFriendlyName()
        );
        UUIDgenerator.generate();
        uuid = UUIDgenerator.getUUID();
        url = "btspp://localhost:" + uuid.toString() + ";name=BluetoothServer";
        notifier = (StreamConnectionNotifier) Connector.open(url);
        running = true;
    }


    /**
     *
     */
    @Override public void execute() throws IOException {

        while ( running ) {

            Logging.msg(
                    "Waiting for connections..."
            );

            // The incoming connection is provisionally accepted.
            StreamConnection connection = notifier.acceptAndOpen();

            // The remote device that is trying to establish a Bluetooth connection with the local device is
            // intercepted.
            RemoteDevice remoteDevice = RemoteDevice.getRemoteDevice(connection);
            String remoteDeviceName = remoteDevice.getFriendlyName(true);

            // If the remote device is in the list of permitted remote devices then the remote connection is
            // established, otherwise the established temporary connection is closed.
            if( true ) {

                Logging.msg(
                        remoteDeviceName + " connected to " + localDevice.getFriendlyName()
                );

                Thread remoteConnection = new RemoteConnection(
                        localDevice,
                        remoteDevice,
                        connection
                );
                remoteConnection.start();

            } else {

                Logging.msg(
                        "Connection refused. Device not recognised: " + remoteDeviceName
                );
                connection.close();

            }

        }

    }

}
