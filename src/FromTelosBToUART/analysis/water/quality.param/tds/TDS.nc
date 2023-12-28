/**
 * @file TDS.nc
 * @brief TDS interface for obtaining Total Dissolved Solids (TDS) value in nesC on TinyOS.
 * @author [gubbriaco, fnicoletti, agrandinetti]
 */

/**
 * @interface TDS
 * @desc Interface for retrieving Total Dissolved Solids (TDS) value.
 */
interface TDS {

    /**
     * @command get
     * @desc Retrieves the Total Dissolved Solids (TDS) value through the TDS interface.
     * @return The TDS value.
     */
    command uint16_t get();

}

