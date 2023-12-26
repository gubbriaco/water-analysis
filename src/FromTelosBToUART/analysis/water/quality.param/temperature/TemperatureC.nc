#include "printf.h"


module TemperatureC {

	provides {
		interface Temperature;	
	}
  
	uses {
        	interface Boot;
        	interface Leds;
    	}

}



implementation {

	
	event void Boot.booted() {
		printf("Temperature Analysis initialized.");
	}
	
	
	uint16_t temperature;
	
	command uint16_t Temperature.get() {
		temperature = 18;
		return temperature;
	}


}
