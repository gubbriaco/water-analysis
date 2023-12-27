#include "printf.h"


module TDSC {

	provides {
		interface TDS;	
	}
  
	uses {
        	interface Boot;
        	interface Leds;
			interface Read<uint16_t> as TDSmeasure;
    	}

}



implementation {

	
	event void Boot.booted() {
	}
	
	
	uint16_t tds;
	
	command uint16_t TDS.get() {
		call TDSmeasure.read();
		return tds;
	}
	
	event void TDSmeasure.readDone(error_t result, uint16_t data) {
		tds = data;
	}


}
