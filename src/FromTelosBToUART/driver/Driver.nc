/**
 * @file Driver.nc
 * @brief Driver interface for quality parameter transmission.
 * @author [gubbriaco, fnicoletti, agrandinetti]
 */

/**
 * @interface Driver
 * @desc Interface for transmitting quality parameters.
 */
interface Driver {
	
    /**
     * @command send
     * @desc Sends the quality parameters through the driver interface.
     * @param QualityParameters Array containing quality parameters to be transmitted.
     */
	command void send(uint16_t QualityParameters[NR_QUALITY_PARAMS]);

}

