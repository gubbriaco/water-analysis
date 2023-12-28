/**
 * @file Analysis.nc
 * @brief Analysis interface for obtaining quality parameters.
 * @author [gubbriaco, fnicoletti, agrandinetti]
 */

/**
 * @interface Analysis
 * @desc Interface for retrieving quality parameters.
 */
interface Analysis {

    /**
     * @command get
     * @desc Retrieves the quality parameters through the analysis interface.
     * @param QualityParams Array to store the obtained quality parameters.
     */
    command void get(uint16_t QualityParams[NR_QUALITY_PARAMS]);

}

