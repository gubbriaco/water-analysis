#include "printf.h"


module PHC {

	provides {
		interface PH;	
	}
  
	uses {
        	interface Boot;
        	interface Leds;
    	}

}



implementation {

	
	event void Boot.booted() {
	}
	
	
	uint16_t pH;
	
	command uint16_t PH.get() {
		pH = 7.1;
		return pH;
	}


}
