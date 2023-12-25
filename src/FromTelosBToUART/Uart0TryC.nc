#include "msp430usart.h"
#include "printf.h"

#define SOP 60
#define EOP 62


module Uart0TryC {

    uses {
        interface Boot;
        interface Leds;
        interface Timer<TMilli> as Timer;
        interface Read<uint16_t> as Temperature;
        interface Resource;
        interface UartStream;
    }

	provides {
		interface Msp430UartConfigure;
	}

}


implementation {

	// Dichiarazione di variabili
	uint8_t DataUart[9];
	uint8_t receivedData[2];
	uint16_t ArduinoData;
	uint16_t Celsius[3] = {0, 0, 0};

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

	// Evento scatenato quando il sistema è avviato
	event void Boot.booted() {

		// Avvia un timer periodico con un periodo di 1024 millisecondi
		call Timer.startPeriodic(1024);

	}

	// Evento scatenato quando il timer scatta
	event void Timer.fired() {

		// Leggi la temperatura, richiedi la risorsa e stampa i dati
		call Temperature.read();
		call Resource.request();

		// Stampa i dati della temperatura inviati
		printf("Dati inviati = %d, %d, %d\n", Celsius[0], Celsius[1], Celsius[2]);

		// Combina e stampa i dati ricevuti da Arduino
		ArduinoData = receivedData[1] << 8 | receivedData[0];
		printf("Dati ricevuti = %d\n", ArduinoData);

		// Svuota il buffer di stampa
		printfflush();

	}

	// Evento scatenato quando la lettura della temperatura è completata
	event void Temperature.readDone(error_t code, uint16_t data) {

		if(code == SUCCESS) {
			// Converti i dati grezzi della temperatura in Celsius
			Celsius[0] = Celsius[1] = Celsius[2] = (-39 + 0.01 * data);

			// Memorizza i dati della temperatura nell'array DataUart
			DataUart[2] = Celsius[0] >> 8; // MSB
			DataUart[1] = Celsius[0] & 0xff; // LSB
			DataUart[4] = Celsius[1] >> 8; // MSB
			DataUart[3] = Celsius[1] & 0xff; // LSB
			DataUart[6] = Celsius[2] >> 8; // MSB
			DataUart[5] = Celsius[2] & 0xff; // LSB

		}
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
