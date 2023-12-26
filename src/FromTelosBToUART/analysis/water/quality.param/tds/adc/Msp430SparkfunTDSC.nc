generic configuration Msp430SparkfunTDSC() {

	provides {
	  	interface Read<uint16_t> as TDSreadInterface;
	}
	
}



implementation {

	components new AdcReadClientC() as ClientTDS;
	TDSreadInterface = ClientTDS;

	components Msp430SparkfunTDSP;
	ClientTDS.AdcConfigure -> Msp430SparkfunTDSP.Sensor;
	
}
