/**
 * @file PHC.nc
 * @brief Implementation of the PHC module for obtaining pH value in nesC on TinyOS.
 * @author [gubbriaco, fnicoletti, agrandinetti]
 */


#include "printf.h"


/**
 * @module PHC
 * @desc Module for retrieving pH value.
 */
module PHC {

	/** 
	 * @provides
	 * @desc Provided interfaces by the module.
	 */
	provides {
		interface PH;	
	}
  
	/**
	 * @uses
	 * @desc Used interfaces by the module.
	 */
	uses {
        	interface Boot;
        	interface Leds;
		interface Read<uint16_t> as PHmeasure;
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
	 * @var ph
	 * @desc Declaration of variable to store pH value.
	 */
	uint16_t ph;
	
	
	/**
	 * @command PH.get
	 * @desc Retrieves the pH value through the pH interface.
	 * @return The pH value.
	 */
	command uint16_t PH.get() {
		// Trigger the pH measurement
		call PHmeasure.read();
		return ph;
	}
	
	
	/**
	 * @event PHmeasure.readDone
	 * @desc Event triggered when the pH measurement is completed.
	 * @param result The result of the pH measurement.
	 * @param data The pH value obtained from the measurement.
	 */
	event void PHmeasure.readDone(error_t result, uint16_t data) {
		// Store the pH value obtained from the measurement
		ph = data;
	}

}

