/**
 * @file UARTdriver.nc
 * @brief Implementation of the UART driver module for MSP430 in nesC on TinyOS.
 * @author [gubbriaco, fnicoletti, agrandinetti]
 */


#include "msp430usart.h"
#include "printf.h"


/**
 * @module UARTdriver
 * @desc UART driver module providing communication between TelosB and an Arduino device.
 */
module UARTdriver {

	/** 
	 * @provides
	 * @desc Provided interfaces by the module.
	 */
	provides {
	  	interface Driver;
	  	interface Msp430UartConfigure;
	}
  
	/**
	 * @uses
	 * @desc Used interfaces by the module.
	 */
	uses {
        	interface Boot;
        	interface Leds;
        	interface Resource;
        	interface UartStream;
    	}

}



implementation {

	/** 
	 * @var DataUart, receivedData, ArduinoData
	 * @desc Declaration of variables for UART communication.
	 */
	uint8_t DataUart[UARTdata_DIM];
	uint8_t receivedData[ACK_DIM];
	uint16_t ArduinoData;


	/** 
	 * @var msp430_uart_9600_config
	 * @desc Configuration for MSP430 UART communication.
	 */
	msp430_uart_union_config_t msp430_uart_9600_config = {{
			ubr : UBR_1MHZ_9600,  
			umctl : UMCTL_1MHZ_9600, 
			ssel : 0X02, 
			pena : 0, 
			pev : 0, 
			spb : 1, 
			clen : 1, 
			listen : 0, 
			mm : 0, 
			ckpl : 0, 
			urxse : 0, 
			urxeie : 1, 
			urxwie : 0, 
			utxe : 1, 
			urxe : 1, 
	}};   


	/**
	 * @command getConfig
	 * @desc Asynchronous command to obtain the MSP430 UART configuration.
	 * @return Configuration for MSP430 UART communication.
	 */
	async command msp430_uart_union_config_t* Msp430UartConfigure.getConfig() {
		return &msp430_uart_9600_config;
	}


	/**
	 * @event Boot.booted
	 * @desc Event triggered when the system is booted.
	 */
	event void Boot.booted() {
		// Add initialization code if needed.
	}


	/**
	 * @command Driver.send
	 * @desc Sends the quality parameters through the UART driver interface.
	 * @param QualityParameters Array containing quality parameters to be transmitted.
	 */
	command void Driver.send(uint16_t QualityParameters[NR_QUALITY_PARAMS]) {
		
		// Populate DataUart array with quality parameters
		DataUart[2] = QualityParameters[TEMPERATURE_POS] >> 8;
		DataUart[1] = QualityParameters[TEMPERATURE_POS] & 0xff;
		DataUart[4] = QualityParameters[TDS_POS] >> 8;
		DataUart[3] = QualityParameters[TDS_POS] & 0xff;
		DataUart[6] = QualityParameters[PH_POS] >> 8;
		DataUart[5] = QualityParameters[PH_POS] & 0xff;
		
		// Request the resource before sending data
		call Resource.request();
		
		printf("data_to_arduino = %d, %d, %d\n", QualityParameters[TEMPERATURE_POS], QualityParameters[TDS_POS], QualityParameters[PH_POS]);

		// Receive acknowledgment from Arduino
		ArduinoData = receivedData[1] << 8 | receivedData[0];
		printf("ack = %d\n", ArduinoData);

		printfflush();
	
	}


	/**
	 * @event Resource.granted
	 * @desc Event triggered when the resource is granted.
	 */
	event void Resource.granted() {

		// Transmission: Send the DataUart array via UartStream and toggle the LED state
		DataUart[SOP_POS] = SOP;
		DataUart[EOP_POS] = EOP;
		if(call UartStream.send(DataUart, UARTdata_DIM) == SUCCESS) {
			call Leds.led0Toggle();
		}

		// Reception: Receive data into the receivedData array via UartStream and toggle the LED state
		if(call UartStream.receive(receivedData, ACK_DIM) == SUCCESS) {
			call Leds.led2Toggle();
		}

	}


	/**
	 * @async_event UartStream.sendDone
	 * @desc Asynchronous event triggered when the UartStream send operation is completed.
	 */
	async event void UartStream.sendDone(uint8_t* buf, uint16_t len, error_t error) {

		// Release the resource after the send operation is completed
		call Resource.release();

	}


	/**
	 * @async_event UartStream.receivedByte
	 * @desc Asynchronous event triggered when a byte is received via UartStream.
	 */
	async event void UartStream.receivedByte(uint8_t byte) {

		// Release the resource after receiving a byte
		call Resource.release();

	}


	/**
	 * @async_event UartStream.receiveDone
	 * @desc Asynchronous event triggered when the UartStream receive operation is completed.
	 */
	async event void UartStream.receiveDone(uint8_t* buf, uint16_t len, error_t error) {

		// Release the resource after the receive operation is completed
		call Resource.release();

	}


}
