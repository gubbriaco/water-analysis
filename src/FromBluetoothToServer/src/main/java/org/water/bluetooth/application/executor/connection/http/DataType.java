package org.water.bluetooth.application.executor.connection.http;

/**
 * The DataType enumeration facilitates HTTP POST to a related endpoint, corresponding to a specific data type, to the server.
 *
 * <p>
 * The enumeration includes values for different data types, such as TEMPERATURE, DISSOLVED_METALS, and PH,
 * each representing a specific type of data to be sent via HTTP POST.
 * </p>
 *
 * @version 1.0
 * @since 2023-12-09
 * @author gubbriaco
 * @author agrandinietti
 * @author fnicoletti
 */
public enum DataType {

    /**
     * Data type TEMPERATURE.
     */
    TEMPERATURE,

    /**
     * Data type DISSOLVED_METALS.
     */
    DISSOLVED_METALS,

    /**
     * Data type PH.
     */
    PH;

}
