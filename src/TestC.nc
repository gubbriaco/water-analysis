#include "Timer.h"
#include "printf.h"

module TestC @safe() {
    uses interface Timer<TMilli> as TemperatureTimer;
    uses interface Leds;
    uses interface Boot;
    uses interface HplMsp430GeneralIO as dht11;
}

implementation {

    event void Boot.booted() {
        call TemperatureTimer.startPeriodic(1000);
        call dht11.makeInput();
    }
    
    
    event void TemperatureTimer.fired() {
        uint16_t val = call dht11.getRaw();
        printf("temperature: %d Celsius\n",  val);
        printfflush();
        call Leds.led0Toggle();
    }
    
}





