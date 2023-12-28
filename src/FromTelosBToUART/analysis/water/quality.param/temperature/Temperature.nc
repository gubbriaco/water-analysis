/**
 * @file Temperature.nc
 * @brief Temperature interface for obtaining temperature value in nesC on TinyOS.
 * @author [gubbriaco, fnicoletti, agrandinetti]
 */

/**
 * @interface Temperature
 * @desc Interface for retrieving temperature value.
 */
interface Temperature {

    /**
     * @command get
     * @desc Retrieves the temperature value through the Temperature interface.
     * @return The temperature value.
     */
    command uint16_t get();

}

