package org.water.bluetooth.application.utils;

/**
 * Allows the generation of textual logs for the management of the Bluetooth Server.
 */
public class Logging {

    /**
     * This static method allows log messages relating to the operation of the Bluetooth Server to be printed on the
     * terminal.
     * @param text Text to be printed as log.
     */
    public static void msg(String text) {
        System.out.println(
                text
        );
    }

}
