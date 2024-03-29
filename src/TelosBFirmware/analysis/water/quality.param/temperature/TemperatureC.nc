/**
 * @file TemperatureC.nc
 * @brief Implementation of the TemperatureC module for obtaining Temperature value in nesC on TinyOS.
 * @author [gubbriaco, fnicoletti, agrandinetti]
 */


#include "printf.h"


/**
 * @module TemperatureC
 * @desc Module for retrieving Temperature value.
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
			interface Read<uint16_t> as Temperaturemeasure;
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
		// Trigger the temperature measurement
		call Temperaturemeasure.read();
		return temperature;
	}
	
	
	/**
	 * @event Temperaturemeasure.readDone
	 * @desc Event triggered when the temperature measurement is completed.
	 * @param result The result of the temperature measurement.
	 * @param data The temperature value obtained from the measurement.
	 */
	event void Temperaturemeasure.readDone(error_t result, uint16_t data) {
		// Store the temperature value obtained from the measurement
		temperature = data;
	}

}

