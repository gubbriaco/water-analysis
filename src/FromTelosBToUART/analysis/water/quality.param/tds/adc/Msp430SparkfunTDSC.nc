/**
 * @file Msp430SparkfunTDSC.nc
 * @brief Implementation of the generic configuration Msp430SparkfunTDSC for Total Dissolved Solids (TDS) sensor in nesC on TinyOS.
 * @author [gubbriaco, fnicoletti, agrandinetti]
 */

/**
 * @generic
 * @config Msp430SparkfunTDSC
 * @desc Generic configuration for Msp430SparkfunTDSC providing TDS sensor functionality.
 */
generic configuration Msp430SparkfunTDSC() {

	/** 
	 * @provides
	 * @desc Provided interface by the configuration.
	 */
	provides {
	  	interface Read<uint16_t> as TDSreadInterface;
	}
	
}



/**
 * @implementation
 * @desc Implementation of the Msp430SparkfunTDSC configuration.
 */
implementation {

	/** 
	 * @var ClientTDS
	 * @desc Declaration of AdcReadClientC component as ClientTDS.
	 */
	components new AdcReadClientC() as ClientTDS;


	/** 
	 * @assignment
	 * @desc Assignment of TDSreadInterface to the ClientTDS component.
	 */
	TDSreadInterface = ClientTDS;


	/** 
	 * @var Msp430SparkfunTDSP
	 * @desc Declaration of Msp430SparkfunTDSP component.
	 */
	components Msp430SparkfunTDSP;


	/** 
	 * @assignment
	 * @desc Assignment of Msp430SparkfunTDSP.Sensor to ClientTDS.AdcConfigure.
	 */
	ClientTDS.AdcConfigure -> Msp430SparkfunTDSP.Sensor;
	
}

