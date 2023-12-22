configuration TestAppC {
}



implementation {
    
	components MainC;
        components LedsC;
        components TestC; 
        TestC.Boot -> MainC;
        TestC.Leds -> LedsC;

        components new TimerMilliC() as TemperatureTimer;
        TestC.TemperatureTimer -> TemperatureTimer;
        
        components HplMsp430GeneralIOC;
        TestC.dht11 -> HplMsp430GeneralIOC.Port23;
    
        components PrintfC, SerialStartC;
    
}

