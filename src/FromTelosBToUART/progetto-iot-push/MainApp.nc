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
	components new Msp430SparkfunTemperatureC() as TemperaturesensorDriver;
	components TDSC;
	components new Msp430SparkfunTDSC() as TDSsensorDriver;
	components PHC;
	components new Msp430SparkfunPHC() as PHsensorDriver;

	Main.Boot -> MainC;
	Main.Leds -> LedsC;
	Main.TimerSampling -> Timer;
	Main.Driver -> UARTdriver;
	Main.Analysis -> WaterAnalysis;
	Main.TimerDriver -> Timer;
	WaterAnalysis.Leds -> LedsC;
	WaterAnalysis.QualityParam -> QualityParamC;
	UARTdriver.Resource -> Msp430Uart0C;
	UARTdriver.UartStream -> Msp430Uart0C;
	UARTdriver.Msp430UartConfigure <- Msp430Uart0C;
	UARTdriver.Leds -> LedsC;
	QualityParamC.Temperature -> TemperatureC;
	TemperatureC.Temperaturemeasure -> TemperaturesensorDriver.TemperaturereadInterface;
	QualityParamC.TDS -> TDSC;
	TDSC.TDSmeasure -> TDSsensorDriver.TDSreadInterface;
	QualityParamC.PH -> PHC;
	PHC.PHmeasure -> PHsensorDriver.PHreadInterface;

}
