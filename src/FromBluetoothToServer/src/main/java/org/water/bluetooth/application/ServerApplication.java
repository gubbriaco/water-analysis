package org.water.bluetooth.application;

import java.io.IOException;

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
     * The Bluetooth Server Executor is responsible for booting the Bluetooth Server.
     */
    BluetoothServerExecutor bluetoothServerExecutor;


    /**
     * The constructor is implemented as private since the instantiation of the {@link ServerApplication} class is
     * obtained via the static {@link ServerApplication#getInstance()} method.
     */
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


    /**
     * Initialises the execution of the Bluetooth server.
     */
    @Override public void start() {
        // The execution of the Bluetooth Server is initialised. In the event of an error, the exception is handled.
        try {
            bluetoothServerExecutor = new BluetoothServerExecutorApplication();
        }catch (IOException e) {
            Logging.msg(
                    "Bluetooth Server Error Initialisation."
            );
        }
        // The execution of the Bluetooth Server is started. In the event of an error, the exception is handled.
        try {
            bluetoothServerExecutor.execute();
        }catch (IOException e) {
            Logging.msg(
                    "Bluetooth Server Error Booting."
            );
        }
    }

}
