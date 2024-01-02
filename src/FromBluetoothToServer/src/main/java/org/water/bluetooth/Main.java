package org.water.bluetooth;
import org.water.bluetooth.application.Server;
import org.water.bluetooth.application.ServerApplication;

/**
 * The Main class serves as the entry point for the Bluetooth server application.
 * It initializes and starts the Bluetooth server by obtaining an instance of the {@link ServerApplication}.
 *
 * <p>
 * The class contains the main method that creates an instance of the {@link ServerApplication} using the
 * {@link ServerApplication#getInstance()} method and starts the Bluetooth server by calling the {@link Server#start()} method.
 * </p>
 *
 * <p>
 * Usage:
 * <pre>
 * // Create an instance of ServerApplication
 * Server serverInstance = ServerApplication.getInstance();
 *
 * // Start the Bluetooth server
 * serverInstance.start();
 * </pre>
 * </p>
 *
 * @version 1.0
 * @since 2023-12-09
 * @author gubbriaco
 * @author agrandinetti
 * @author fnicoletti
 */
public class Main {

    /**
     * The main method serves as the entry point for the Bluetooth server application.
     * It initializes and starts the Bluetooth server by obtaining an instance of the {@link ServerApplication}.
     *
     * @param strings Command-line arguments (not used).
     */
    public static void main(String...strings) {
        // Create an instance of ServerApplication
        Server bs = ServerApplication.getInstance();

        // Start the Bluetooth server
        bs.start();

    }

}