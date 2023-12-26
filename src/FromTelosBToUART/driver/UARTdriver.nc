#include "msp430usart.h"
#include "printf.h"


module UARTdriver {

	provides {
	  	interface Driver;
	  	interface Msp430UartConfigure;
	}
  
	uses {
        	interface Boot;
        	interface Leds;
        	interface Resource;
        	interface UartStream;
    	}

}



implementation {

	uint8_t DataUart[9];
	uint8_t receivedData[2];
	uint16_t ArduinoData;

	// Configurazione per la comunicazione UART MSP430
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

	// Comando asincrono per ottenere la configurazione UART MSP430
	async command msp430_uart_union_config_t* Msp430UartConfigure.getConfig() {
		return &msp430_uart_9600_config;
	}

	
	event void Boot.booted() {
	}
	
	
	command void Driver.send(uint16_t QualityParameters[NR_QUALITY_PARAMS]) {
		
		DataUart[2] = QualityParameters[TEMPERATURE_POS] >> 8; // MSB
		DataUart[1] = QualityParameters[TEMPERATURE_POS] & 0xff; // LSB
		DataUart[4] = QualityParameters[TDS_POS] >> 8; // MSB
		DataUart[3] = QualityParameters[TDS_POS] & 0xff; // LSB
		DataUart[6] = QualityParameters[PH_POS] >> 8; // MSB
		DataUart[5] = QualityParameters[PH_POS] & 0xff; // LSB
		
		call Resource.request();
		
		printf("data_to_arduino = %d, %d, %d\n", QualityParameters[0], QualityParameters[1], QualityParameters[2]);
		
		ArduinoData = receivedData[1] << 8 | receivedData[0];
		printf("ack = %d\n", ArduinoData);

		
		printfflush();
	
	}


	// Evento scatenato quando la risorsa è concessa
	event void Resource.granted() {

		// Trasmissione: invia l'array DataUart tramite UartStream e cambia lo stato del LED
		DataUart[0] = SOP;
		DataUart[7] = EOP;
		if(call UartStream.send(DataUart, 9) == SUCCESS) {
			call Leds.led0Toggle();
		}

		// Ricezione: ricevi i dati nell'array receivedData tramite UartStream e cambia lo stato del LED
		if(call UartStream.receive(receivedData, 2) == SUCCESS) {
			call Leds.led2Toggle();
		}

	}

	// Eventi asincroni per invio, ricezione e ricezione byte in UartStream
	async event void UartStream.sendDone(uint8_t* buf, uint16_t len, error_t error) {

		// Rilascia la risorsa dopo che l'invio è completato
		call Resource.release();

	}

	async event void UartStream.receivedByte(uint8_t byte) {

		// Rilascia la risorsa dopo la ricezione di un byte
		call Resource.release();

	}

	async event void UartStream.receiveDone(uint8_t* buf, uint16_t len, error_t error) {

		// Rilascia la risorsa dopo che la ricezione è completata
		call Resource.release();

	}

}
