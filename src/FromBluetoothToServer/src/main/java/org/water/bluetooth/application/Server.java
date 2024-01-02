package org.water.bluetooth.application;

/**
 * The {@code Server} interface defines the contract for initializing the execution of the Bluetooth server within the
 * application.
 *
 * <p>
 * Implementing classes are expected to provide the necessary logic to start the Bluetooth server, allowing it to listen
 * for incoming connections and handle communication with remote devices. The method {@link #start()} is the entry point
 * for initiating the execution of the Bluetooth server.
 * </p>
 *
 * <p>
 * This interface also includes versioning information, authorship details, and references to related classes using the
 * {@link ServerApplication} class.
 * </p>
 *
 * <p>
 * The version information follows the semantic versioning format (MAJOR.MINOR.PATCH), and the authors are mentioned
 * with their respective usernames. Additionally, the interface is part of the "water-analysis" application with version
 * 1.1 and has been available since version 1.0.
 * </p>
 *
 * @version 1.0
 * @since 2023-12-09
 * @see ServerApplication
 * @author gubbriaco
 * @author fnicoletti
 * @author agrandinetti
 */
public interface Server {

    /**
     * Initialises the execution of the Bluetooth server.
     */
    void start();

}
