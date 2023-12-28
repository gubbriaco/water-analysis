/**
 * @file QualityParamC.nc
 * @brief Implementation of the QualityParamC module for obtaining specific quality parameters in nesC on TinyOS.
 * @author [gubbriaco, fnicoletti, agrandinetti]
 */

#include "printf.h"

/**
 * @module QualityParamC
 * @desc Module for retrieving specific quality parameters.
 */
module QualityParamC {

	/** 
	 * @provides
	 * @desc Provided interfaces by the module.
	 */
	provides {
	  	interface QualityParam;
	}
  
	/**
	 * @uses
	 * @desc Used interfaces by the module.
	 */
	uses {
        	interface Boot;
        	interface Leds;
        	interface Temperature;
        	interface TDS;
        	interface PH;
    	}

}



/**
 * @implementation
 */
implementation {

	/**
	 * @event Boot.booted
	 * @desc Event triggered when the system is booted.
	 */
	event void Boot.booted() {
		// Add initialization code if needed.
	}
	
	/**
	 * @command QualityParam.get
	 * @desc Retrieves a specific quality parameter through the QualityParam interface.
	 * @param ParamType Type of the quality parameter to retrieve.
	 * @return The value of the specified quality parameter.
	 */
	command uint16_t QualityParam.get(uint8_t ParamType) {
		
		// Check the type of quality parameter and retrieve accordingly
		if (ParamType == TEMPERATURE_POS) {
			return call Temperature.get();
		} else if (ParamType == TDS_POS) {
			return call TDS.get();
		} else {
			return call PH.get();
		}
	
	}

}
