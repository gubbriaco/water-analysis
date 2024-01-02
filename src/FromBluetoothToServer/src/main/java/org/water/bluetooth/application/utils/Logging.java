package org.water.bluetooth.application.utils;

/**
 * The {@code Logging} class provides a simple utility for generating textual logs related to the operation of the
 * Bluetooth Server.
 *
 * <p>
 * It contains a single method, {@link #msg(String)}, which allows log messages to be printed on the terminal.
 * Developers can use this class to log various events and information during the execution of the Bluetooth Server.
 * </p>
 *
 * <p>
 * The class is designed to be static, ensuring that the logging functionality is easily accessible without the need to
 * instantiate an object. Log messages are printed to the terminal using the standard output stream.
 * </p>
 *
 * @see System#out
 *
 * @version 1.0
 * @since 2023-12-09
 * @author gubbriaco
 * @author agrandinetti
 * @author fnicoletti
 */
public class Logging {

    /**
     * Prints the specified text as a log message on the terminal.
     *
     * @param text The text to be printed as a log message.
     */
    public static void msg(String text) {
        System.out.println(text);
    }

}
