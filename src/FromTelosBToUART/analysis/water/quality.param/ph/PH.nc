/**
 * @file PH.nc
 * @brief PH interface for obtaining pH value in nesC on TinyOS.
 * @author [gubbriaco, fnicoletti, agrandinetti]
 */

/**
 * @interface PH
 * @desc Interface for retrieving pH value.
 */
interface PH {

    /**
     * @command get
     * @desc Retrieves the pH value through the PH interface.
     * @return The pH value.
     */
    command uint16_t get();

}
