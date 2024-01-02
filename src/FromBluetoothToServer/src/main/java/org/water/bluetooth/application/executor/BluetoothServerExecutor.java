package org.water.bluetooth.application.executor;

import java.io.IOException;

/**
 * The {@code BluetoothServerExecutor} interface represents the contract for executing the Bluetooth Server.
 *
 * <p>
 * This interface provides a method, {@link #execute()}, that should be implemented by classes intending to act as
 * Bluetooth Server Executors. Implementing classes are responsible for handling the execution logic of the Bluetooth
 * Server, including initialization and waiting for incoming connections.
 * </p>
 *
 * <p>
 * Implementations of this interface can encapsulate different strategies for executing the Bluetooth Server, providing
 * flexibility in adapting to various use cases and requirements.
 * </p>
 *
 * @version 1.0
 * @since 2023-12-09
 * @author gubbriaco
 * @author agrandinetti
 * @author fnicoletti
 */
public interface BluetoothServerExecutor {

    /**
     * Enables the execution of the Bluetooth Server Executor.
     *
     * @throws IOException If an I/O error occurs during the execution.
     */
    void execute() throws IOException;

}
