/**
 * @file TDSC.nc
 * @brief Implementation of the TDSC module for obtaining Total Dissolved Solids (TDS) value in nesC on TinyOS.
 * @author [gubbriaco, fnicoletti, agrandinetti]
 */


#include "printf.h"


/**
 * @module TDSC
 * @desc Module for retrieving Total Dissolved Solids (TDS) value.
 */
module TDSC {

	/** 
	 * @provides
	 * @desc Provided interfaces by the module.
	 */
	provides {
		interface TDS;	
	}
  
	/**
	 * @uses
	 * @desc Used interfaces by the module.
	 */
	uses {
        	interface Boot;
        	interface Leds;
		interface Read<uint16_t> as TDSmeasure;
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
	 * @var tds
	 * @desc Declaration of variable to store TDS value.
	 */
	uint16_t tds;
	
	
	/**
	 * @command TDS.get
	 * @desc Retrieves the Total Dissolved Solids (TDS) value through the TDS interface.
	 * @return The TDS value.
	 */
	command uint16_t TDS.get() {
		// Trigger the TDS measurement
		call TDSmeasure.read();
		return tds;
	}
	
	
	/**
	 * @event TDSmeasure.readDone
	 * @desc Event triggered when the TDS measurement is completed.
	 * @param result The result of the TDS measurement.
	 * @param data The TDS value obtained from the measurement.
	 */
	event void TDSmeasure.readDone(error_t result, uint16_t data) {
		// Store the TDS value obtained from the measurement
		tds = data;
	}

}
