/**
 * @file WaterAnalysis.nc
 * @brief Implementation of the WaterAnalysis module for quality parameter analysis in nesC on TinyOS.
 * @author [gubbriaco, fnicoletti, agrandinetti]
 */


#include "printf.h"


/**
 * @module WaterAnalysis
 * @desc Module for analyzing quality parameters in water.
 */
module WaterAnalysis {

	/** 
	 * @provides
	 * @desc Provided interfaces by the module.
	 */
	provides {
	  	interface Analysis;
	}
  
	/**
	 * @uses
	 * @desc Used interfaces by the module.
	 */
	uses {
        	interface Boot;
        	interface Leds;
        	interface QualityParam;
    	}

}



/**
 * @implementation
 */
implementation{

	/** 
	 * @var temperature, tds, pH
	 * @desc Declaration of variables for quality parameters.
	 */
	uint16_t temperature = -1;
	uint16_t tds = -1;
	uint16_t pH = -1;

	
	/**
	 * @event Boot.booted
	 * @desc Event triggered when the system is booted.
	 */
	event void Boot.booted() {
		// Add initialization code if needed.
	}
	
	
	/**
	 * @command Analysis.get
	 * @desc Retrieves the quality parameters through the analysis interface.
	 * @param QualityParams Array to store the obtained quality parameters.
	 */
	command void Analysis.get(uint16_t QualityParams[NR_QUALITY_PARAMS]) {
	
		// Retrieve temperature and store in QualityParams array
		temperature = call QualityParam.get(TEMPERATURE_POS);
		QualityParams[TEMPERATURE_POS] = temperature;
		
		// Retrieve TDS and store in QualityParams array
		tds = call QualityParam.get(TDS_POS);
		QualityParams[TDS_POS] = tds;
		
		// Retrieve pH and store in QualityParams array
		pH = call QualityParam.get(PH_POS);
		QualityParams[PH_POS] = pH;
		
	}

}

