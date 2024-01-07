/**
 * @file Msp430SparkfunPHC.nc
 * @brief Implementation of the generic configuration Msp430SparkfunPHC for pH sensor in nesC on TinyOS.
 * @author [gubbriaco, fnicoletti, agrandinetti]
 */

/**
 * @generic
 * @config Msp430SparkfunPHC
 * @desc Generic configuration for Msp430SparkfunPHC providing pH sensor functionality.
 */
generic configuration Msp430SparkfunPHC() {

	/** 
	 * @provides
	 * @desc Provided interface by the configuration.
	 */
	provides {
	  	interface Read<uint16_t> as PHreadInterface;
	}
	
}



/**
 * @implementation
 * @desc Implementation of the Msp430SparkfunPHC configuration.
 */
implementation {

	/** 
	 * @var ClientPH
	 * @desc Declaration of AdcReadClientC component as ClientPH.
	 */
	components new AdcReadClientC() as ClientPH;


	/** 
	 * @assignment
	 * @desc Assignment of PHreadInterface to the ClientPH component.
	 */
	PHreadInterface = ClientPH;


	/** 
	 * @var Msp430SparkfunPHP
	 * @desc Declaration of Msp430SparkfunPHP component.
	 */
	components Msp430SparkfunPHP;


	/** 
	 * @assignment
	 * @desc Assignment of Msp430SparkfunPHP.Sensor to ClientPH.AdcConfigure.
	 */
	ClientPH.AdcConfigure -> Msp430SparkfunPHP.Sensor;
	
}

