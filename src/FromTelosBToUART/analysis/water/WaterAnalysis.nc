#include "printf.h"


module WaterAnalysis {

	provides {
	  	interface Analysis;
	}
  
	uses {
        	interface Boot;
        	interface Leds;
        	interface QualityParam;
    	}

}



implementation {

	
	event void Boot.booted() {
	}
	
	
	uint16_t temperature = -1;
	uint16_t tds = -1;
	uint16_t pH = -1;
	
	command void Analysis.get(uint16_t QualityParams[NR_QUALITY_PARAMS]) {
	
		temperature = call QualityParam.get(TEMPERATURE_POS);
		QualityParams[TEMPERATURE_POS] = temperature;
		
		tds = call QualityParam.get(TDS_POS);
		QualityParams[TDS_POS] = tds;
		
		pH = call QualityParam.get(PH_POS);
		QualityParams[PH_POS] = pH;
		
	}


}
