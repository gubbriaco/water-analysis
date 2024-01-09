/**
 * @file Msp430SparkfunPHP.nc
 * @brief Implementation of the Msp430SparkfunPHP module for pH sensor in nesC on TinyOS.
 * @author [gubbriaco, fnicoletti, agrandinetti]
 */


#include <Msp430Adc12.h>


/**
 * @module Msp430SparkfunPHP
 * @desc Module for configuring the MSP430 ADC for pH sensor.
 */
module Msp430SparkfunPHP {

	/** 
	 * @provides
	 * @desc Provided interface by the module.
	 */
  	provides {
  		interface AdcConfigure<const msp430adc12_channel_config_t*> as Sensor;
	}
	
}



/**
 * @implementation
 * @desc Implementation of the Msp430SparkfunPHP module.
 */
implementation {

	/** 
	 * @var configSens
	 * @desc Declaration of configuration settings for the pH sensor.
	 */
  	const msp430adc12_channel_config_t configSens = {
      		inch: INPUT_CHANNEL_A1, 		//0
      		sref: REFERENCE_AVcc_AVss,		//0
      		ref2_5v: REFVOLT_LEVEL_2_5,		//0
      		adc12ssel: SHT_SOURCE_ACLK,		//1
      		adc12div: SHT_CLOCK_DIV_1,		//0
      		sht: SAMPLE_HOLD_4_CYCLES,		//0
      		sampcon_ssel: SAMPCON_SOURCE_SMCLK,	//2
      		sampcon_id: SAMPCON_CLOCK_DIV_1		//0
  	};
  

  	/**
	 * @async_command
	 * @desc Asynchronous command to obtain the configuration settings for the pH sensor.
	 * @return Configuration settings for the TDS sensor.
	 */
  	async command const msp430adc12_channel_config_t* Sensor.getConfiguration() {
    		return &configSens;
  	}
  	
}
