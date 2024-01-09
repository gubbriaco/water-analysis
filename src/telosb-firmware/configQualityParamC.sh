#!/bin/bash

if [ "$#" -eq 0 ]; then
  echo "Usage: $0 <argument>"
  exit 1
fi

# Access the first argument using $1
arg1="$1"

QUALITYPARAMC_DIR="./analysis/water/quality.param/QualityParamC.nc"

cat <<EOL > "$QUALITYPARAMC_DIR"
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
EOL


generate_uses() {
    case $arg1 in
        "home")
            cat <<EOL >> "$QUALITYPARAMC_DIR"
		interface Temperature;
        	interface TDS;
        	interface PH;
EOL
            ;;
        "sea")
            cat <<EOL >> "$QUALITYPARAMC_DIR"
        	interface Temperature;
        	interface TDS;
        	interface PH;
EOL
            ;;
        "pool")
            cat <<EOL >> "$QUALITYPARAMC_DIR"
		interface Temperature;
        	interface PH;
EOL
            ;;
        *)
            echo "Unknown value in argument: $arg1"
            exit 1
            ;;
    esac
}

generate_uses


cat <<EOL >> "$QUALITYPARAMC_DIR"
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
EOL


generate_paramtype() {
    case $arg1 in
        "home")
            cat <<EOL >> "$QUALITYPARAMC_DIR"
		if (ParamType == TEMPERATURE_POS) {
			return call Temperature.get();
		} else if (ParamType == TDS_POS) {
			return call TDS.get();
		} else {
			return call PH.get();
		}
	
	}

}
EOL
            ;;
        "sea")
            cat <<EOL >> "$QUALITYPARAMC_DIR"
		if (ParamType == TEMPERATURE_POS) {
			return call Temperature.get();
		} else if (ParamType == TDS_POS) {
			return call TDS.get();
		} else {
			return call PH.get();
		}
	
	}

}
EOL
            ;;
        "pool")
            cat <<EOL >> "$QUALITYPARAMC_DIR"
		if (ParamType == TEMPERATURE_POS) {
			return call Temperature.get();
		} else {
			return call PH.get();
		}
	
	}

}
EOL
            ;;
        *)
            echo "Unknown value in argument: $arg1"
            exit 1
            ;;
    esac
}

generate_paramtype
