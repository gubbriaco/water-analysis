#include <Msp430Adc12.h>


module Msp430SparkfunTDSP {

  	provides {
  		interface AdcConfigure<const msp430adc12_channel_config_t*> as Sensor;
	}
	
}



implementation {

  	const msp430adc12_channel_config_t configSens = {
      		inch: INPUT_CHANNEL_A0, 		//0
      		sref: REFERENCE_AVcc_AVss,		//0
      		ref2_5v: REFVOLT_LEVEL_1_5,		//0
      		adc12ssel: SHT_SOURCE_ACLK,		//1
      		adc12div: SHT_CLOCK_DIV_1,		//0
      		sht: SAMPLE_HOLD_4_CYCLES,		//0
      		sampcon_ssel: SAMPCON_SOURCE_SMCLK,	//2
      		sampcon_id: SAMPCON_CLOCK_DIV_1		//0
  	};
  
  
  	async command const msp430adc12_channel_config_t* Sensor.getConfiguration() {
    		return &configSens;
  	}
  	
}
