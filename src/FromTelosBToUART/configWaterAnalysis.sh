#!/bin/bash

if [ "$#" -eq 0 ]; then
  echo "Usage: $0 <argument>"
  exit 1
fi

# Access the first argument using $1
arg1="$1"

WATERANALYSIS_DIR="./analysis/water/WaterAnalysis.nc"

cat <<EOL > "$WATERANALYSIS_DIR"
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

EOL


generate_variables() {
    case $arg1 in
        "home")
            cat <<EOL >> "$WATERANALYSIS_DIR"
	/** 
	 * @var temperature, tds, pH
	 * @desc Declaration of variables for quality parameters.
	 */
	uint16_t temperature = -1;
	uint16_t tds = -1;
	uint16_t pH = -1;
EOL
            ;;
        "sea")
            cat <<EOL >> "$WATERANALYSIS_DIR"
	/** 
	 * @var temperature, tds, pH
	 * @desc Declaration of variables for quality parameters.
	 */
	uint16_t temperature = -1;
	uint16_t tds = -1;
	uint16_t pH = -1;
EOL
            ;;
        "pool")
            cat <<EOL >> "$WATERANALYSIS_DIR"
	/** 
	 * @var temperature, pH
	 * @desc Declaration of variables for quality parameters.
	 */
	uint16_t temperature = -1;
	uint16_t pH = -1;
EOL
            ;;
        *)
            echo "Unknown value in argument: $arg1"
            exit 1
            ;;
    esac
}

generate_variables


cat <<EOL >> "$WATERANALYSIS_DIR"

	
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
	
EOL


generate_getters() {
    case $arg1 in
        "home")
            cat <<EOL >> "$WATERANALYSIS_DIR"
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

EOL
            ;;
        "sea")
            cat <<EOL >> "$WATERANALYSIS_DIR"
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

EOL
            ;;
        "pool")
            cat <<EOL >> "$WATERANALYSIS_DIR"
		// Retrieve temperature and store in QualityParams array
		temperature = call QualityParam.get(TEMPERATURE_POS);
		QualityParams[TEMPERATURE_POS] = temperature;
		
		// Retrieve pH and store in QualityParams array
		pH = call QualityParam.get(PH_POS);
		QualityParams[PH_POS] = pH;
		
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

generate_getters
