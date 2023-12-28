/**
 * @file Main.nc
 * @brief Implementation of the Main module for TelosB in nesC on TinyOS.
 * @authors [gubbriaco, fnicoletti, agrandinetti]
 */


#include "printf.h"


/**
 * @module Main
 * @desc Main module for handling the sampling, analysis, and transmission of quality parameters.
 */
module Main {

    /**
     * @uses
     * @desc Declaration of interfaces used by the module.
     */
    uses {
        interface Boot;
        interface Leds;
        interface Timer<TMilli> as TimerSampling;
        interface Timer<TMilli> as TimerDriver;
        interface Driver;
        interface Analysis;
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
        // Start the periodic timer for sampling quality parameters.
        call TimerSampling.startPeriodic(SAMPLING_TIME);
    }
    

    /** 
     * @var QualityParams
     * @desc Global array to store quality parameters.
     */
    uint16_t QualityParams[NR_QUALITY_PARAMS] = {-1, -1, -1};
    

    /**
     * @event TimerSampling.fired
     * @desc Event triggered when the sampling timer fires.
     */
    event void TimerSampling.fired() {
        // Obtain quality parameters from the analysis interface.
        call Analysis.get(QualityParams);
        
        // Start the periodic timer for the subsequent data transmission.
        call TimerDriver.startPeriodic(2 * SAMPLING_TIME);
    }


    /**
     * @event TimerDriver.fired
     * @desc Event triggered when the data transmission timer fires.
     */
    event void TimerDriver.fired() {
        // Send quality parameters through the Driver communication interface.
        call Driver.send(QualityParams); 
    }

}
