configuration MainApp {
}

implementation {
	
	components MainC;
	components Main;
	components new TimerMilliC() as Timer;
	components LedsC;
	components PrintfC, SerialStartC;
	components UARTdriver;
	components new Msp430Uart0C(); 
	components WaterAnalysis;
	components QualityParamC;
	components TemperatureC;
	components TDSC;
	components new Msp430SparkfunTDSC() as TDSsensorDriver;
	components PHC;
	
	
	Main.Boot -> MainC;
	Main.Leds -> LedsC;
	Main.Timer -> Timer;
	Main.Driver -> UARTdriver;
	Main.Analysis -> WaterAnalysis;
	Main.TimerDriver -> Timer;
	WaterAnalysis.Leds -> LedsC;
	WaterAnalysis.QualityParam -> QualityParamC;
	QualityParamC.Temperature -> TemperatureC;
	QualityParamC.TDS -> TDSC;
	TDSC.TDSmeasure -> TDSsensorDriver.TDSreadInterface;
	QualityParamC.PH -> PHC;
	UARTdriver.Resource -> Msp430Uart0C;
	UARTdriver.UartStream -> Msp430Uart0C;
	UARTdriver.Msp430UartConfigure <- Msp430Uart0C;
	UARTdriver.Leds -> LedsC;
	
}
