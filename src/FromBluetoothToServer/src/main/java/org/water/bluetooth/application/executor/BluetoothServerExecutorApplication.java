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

        Logging.msg(
                "Waiting for connections..."
        );

        while ( running ) {

            Thread remoteConnection = new RemoteConnection(
                    localDevice,
                    notifier
            );
            remoteConnection.start();

        }

    }

}
