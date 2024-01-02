#!/bin/bash

if [ "$#" -eq 0 ]; then
  echo "Usage: $0 <argument>"
  exit 1
fi

# Access the first argument using $1
arg1="$1"

MAIN_DIR="./main/Main.nc"


cat <<EOL > "$MAIN_DIR"
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
EOL


generate_array() {
    case $arg1 in
        "home")
            cat <<EOL >> "$MAIN_DIR"
	uint16_t QualityParams[NR_QUALITY_PARAMS] = {-1, -1};
EOL
            ;;
        "sea")
            cat <<EOL >> "$MAIN_DIR"
	uint16_t QualityParams[NR_QUALITY_PARAMS] = {-1, -1, -1};
EOL
            ;;
        "pool")
            cat <<EOL >> "$MAIN_DIR"
	uint16_t QualityParams[NR_QUALITY_PARAMS] = {-1, -1};
EOL
            ;;
        *)
            echo "Unknown value in argument: $arg1"
            exit 1
            ;;
    esac
}

generate_array


cat <<EOL >> "$MAIN_DIR"


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
EOL
