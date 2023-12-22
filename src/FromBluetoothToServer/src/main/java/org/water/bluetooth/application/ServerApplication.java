package org.water.bluetooth.application;

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.IOException;
import java.io.InputStream;

import org.water.bluetooth.application.executor.BluetoothServerExecutor;
import org.water.bluetooth.application.executor.BluetoothServerExecutorApplication;
import org.water.bluetooth.application.utils.Logging;

public class ServerApplication implements Server {


    /** I ensure that the class has only one instance and that this instance is easily accessible. Therefore, the class
     * itself ensures that no other instance can be created and, via the static and synchronized getIstance(), it
     * provides an easy way to access the only instance created. This technique used is the so-called Lazy
     * Initialisation.
     */
    private static ServerApplication INSTANCE = null;

    /**
     *
     */
    BluetoothServerExecutor bluetoothServerExecutor;

    private ServerApplication() {}

    /**
     * Allows access to the only instance that can be created of the class. It is made synchronised to guarantee the
     * atomicity of the creation process in the case of concurrent access of the class.
     * @return Instance of the class {@link ServerApplication}
     */
    public static synchronized ServerApplication getInstance() {
        if(INSTANCE == null)
            INSTANCE = new ServerApplication();
        return INSTANCE;
    }


    @Override public void start() {
        try {
            bluetoothServerExecutor = new BluetoothServerExecutorApplication();
        }catch (IOException e) {
            Logging.msg(
                    "Bluetooth Server Error Initialisation."
            );
        }
        try {
            bluetoothServerExecutor.execute();
        }catch (IOException e) {
            Logging.msg(
                    "Bluetooth Server Error Booting."
            );
        }
    }

}
