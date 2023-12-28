/**
 * @file QualityParam.nc
 * @brief QualityParam interface for obtaining specific quality parameters.
 * @author [gubbriaco, fnicoletti, agrandinetti]
 */

/**
 * @interface QualityParam
 * @desc Interface for retrieving specific quality parameters.
 */
interface QualityParam {

    /**
     * @command get
     * @desc Retrieves a specific quality parameter through the QualityParam interface.
     * @param ParamType Type of the quality parameter to retrieve.
     * @return The value of the specified quality parameter.
     */
    command uint16_t get(uint8_t ParamType);

}

