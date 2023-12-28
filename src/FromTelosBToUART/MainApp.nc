/**
 * @file MainApp.nc
 * @brief TinyOS configuration for the MainApp.
 * @author [gubbriaco, fnicoletti, agrandinetti]
 */

/**
 * @configuration MainApp
 * @desc TinyOS configuration for the MainApp.
 */
configuration MainApp {
}



/**
 * @implementation
 */
implementation {
	
	/** 
	 * @var MainC, Timer, LedsC, PrintfC, SerialStartC, UARTdriver, WaterAnalysis, QualityParamC, TemperatureC, TDSC, PHC
	 * @desc Declaration of components used in the configuration.
	 */
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
	
	
	/** 
	 * @connection MainC, Timer, LedsC, PrintfC, SerialStartC, UARTdriver, WaterAnalysis, QualityParamC, TemperatureC, TDSC, PHC
	 * @desc Connection of components in the configuration.
	 */
	Main.Boot -> MainC;
	Main.Leds -> LedsC;
	Main.TimerSampling -> Timer;
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
