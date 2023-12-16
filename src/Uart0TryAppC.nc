configuration Uart0TryAppC {
}

implementation {
	
	components Uart0TryC, MainC, LedsC;
	components new TimerMilliC() as Timer;
	components new Msp430Uart0C();
	components new SensirionSht11C() as TemperatureDriver;
	components PrintfC, SerialStartC;
	
	Uart0TryC.Boot -> MainC;
	Uart0TryC.Leds -> LedsC;
	Uart0TryC.Timer -> Timer;
	Uart0TryC.Resource -> Msp430Uart0C;
	Uart0TryC.UartStream -> Msp430Uart0C;
	Uart0TryC.Temperature -> TemperatureDriver.Temperature;
	Uart0TryC.Msp430UartConfigure <- Msp430Uart0C;

}
