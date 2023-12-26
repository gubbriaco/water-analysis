#include "printf.h"


module QualityParamC {

	provides {
	  	interface QualityParam;
	}
  
	uses {
        	interface Boot;
        	interface Leds;
        	interface Temperature;
        	interface TDS;
        	interface PH;
    	}

}



implementation {

	
	event void Boot.booted() {
	}
	
	
	command uint16_t QualityParam.get(uint8_t ParamType) {
		
		if(ParamType == TEMPERATURE_POS) {
			return call Temperature.get();
		} else if(ParamType == TDS_POS) {
			return call TDS.get();
		} else {
			return call PH.get();
		}
	
	}


}
