/**
 * @file Msp430SparkfunTemperatureC.nc
 * @brief Implementation of the generic configuration Msp430SparkfunTemperatureC for Temperature sensor in nesC on TinyOS.
 * @author [gubbriaco, fnicoletti, agrandinetti]
 */

/**
 * @generic
 * @config Msp430SparkfunTemperatureC
 * @desc Generic configuration for Msp430SparkfunTemperatureC providing Temperature sensor functionality.
 */
generic configuration Msp430SparkfunTemperatureC() {

	/** 
	 * @provides
	 * @desc Provided interface by the configuration.
	 */
	provides {
	  	interface Read<uint16_t> as TemperaturereadInterface;
	}
	
}



/**
 * @implementation
 * @desc Implementation of the Msp430SparkfunTemperatureC configuration.
 */
implementation {

	/** 
	 * @var ClientTemperature
	 * @desc Declaration of AdcReadClientC component as ClientTemperature.
	 */
	components new AdcReadClientC() as ClientTemperature;


	/** 
	 * @assignment
	 * @desc Assignment of TemperaturereadInterface to the ClientTemperature component.
	 */
	TemperaturereadInterface = ClientTemperature;


	/** 
	 * @var Msp430SparkfunTemperatureP
	 * @desc Declaration of Msp430SparkfunTemperatureP component.
	 */
	components Msp430SparkfunTemperatureP;


	/** 
	 * @assignment
	 * @desc Assignment of Msp430SparkfunTemperatureP.Sensor to ClientTemperature.AdcConfigure.
	 */
	ClientTemperature.AdcConfigure -> Msp430SparkfunTemperatureP.Sensor;
	
}

