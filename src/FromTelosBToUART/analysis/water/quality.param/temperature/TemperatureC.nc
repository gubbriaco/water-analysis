/**
 * @file TemperatureC.nc
 * @brief Implementation of the TemperatureC module for obtaining temperature value in nesC on TinyOS.
 * @author [gubbriaco, fnicoletti, agrandinetti]
 */


#include "printf.h"


/**
 * @module TemperatureC
 * @desc Module for retrieving temperature value.
 */
module TemperatureC {

	/** 
	 * @provides
	 * @desc Provided interfaces by the module.
	 */
	provides {
		interface Temperature;	
	}
  
	/**
	 * @uses
	 * @desc Used interfaces by the module.
	 */
	uses {
        	interface Boot;
        	interface Leds;
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
	 * @var temperature
	 * @desc Declaration of variable to store temperature value.
	 */
	uint16_t temperature;
	
	
	/**
	 * @command Temperature.get
	 * @desc Retrieves the temperature value through the Temperature interface.
	 * @return The temperature value.
	 */
	command uint16_t Temperature.get() {
		// TODO: Implement code to retrieve the actual temperature value
		temperature = 18;
		return temperature;
	}

}

