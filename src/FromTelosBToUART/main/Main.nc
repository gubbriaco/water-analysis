#include "printf.h"


module Main {

    uses {
        interface Boot;
        interface Leds;
        interface Timer<TMilli> as Timer;
        interface Timer<TMilli> as TimerDriver;
        interface Driver;
        interface Analysis;
        interface BusyWait<TMicro,uint32_t> as WaitForParams;
    }

}


implementation {

	uint16_t QualityParams[NR_QUALITY_PARAMS] = {-1, -1, -1};


	event void Boot.booted() {
		call Timer.startPeriodic(1024);
	}

	
	event void Timer.fired() {
		call Analysis.get(QualityParams);
		call TimerDriver.startPeriodic(2 * 1024);
	}

	event void TimerDriver.fired() {
		call Driver.send(QualityParams); 
	}


}
