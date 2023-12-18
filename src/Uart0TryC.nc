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


	// Declaration of variables
	uint8_t DataUart[5];
	uint8_t receivedData[2];
	uint16_t ArduinoData;
	uint16_t Celsius = 0;
	

	// Configuration for MSP430 UART communication
	msp430_uart_union_config_t msp430_uart_9600_config = {{
			ubr : UBR_1MHZ_9600,  // Baud rate (use enum msp430_uart_rate_t in msp430usart.h for predefined rates)
			umctl : UMCTL_1MHZ_9600, // Modulation (use enum msp430_uart_rate_t in msp430usart.h for predefined rates)
			ssel : 0X02, // Clock source (00=UCLKI; 01=ACLK; 10=SMCLK; 11=SMCLK)
			pena : 0, // Parity enable (0=disabled; 1=enabled)
			pev : 0, // Parity select (0=odd; 1=even)
			spb : 1, // Stop bits (0=one stop bit; 1=two stop bits)
			clen : 1, // Character length (0=7-bit data; 1=8-bit data)
			listen : 0, // Listen enable (0=disabled; 1=enabled, feed tx back to receiver)
			mm : 0, // Multiprocessor mode (0=idle-line protocol; 1=address-bit protocol)
			ckpl : 0, // Clock polarity (0=normal; 1=inverted)
			urxse : 0, // Receive start-edge detection (0=disabled; 1=enabled)
			urxeie : 1, // Erroneous-character receive (0=rejected; 1=recieved and URXIFGx set)
			urxwie : 0, // Wake-up interrupt-enable (0=all characters set URXIFGx; 1=only address sets URXIFGx)
			utxe : 1, // 1:enable tx module
			urxe : 1, // 1:enable rx module
	}};   
	

	// Asynchronous command to get the MSP430 UART configuration
	async command msp430_uart_union_config_t* Msp430UartConfigure.getConfig() {
		return &msp430_uart_9600_config;
	}
	

	// Event triggered when the system is booted
	event void Boot.booted() {

		// Start a periodic timer with a period of 1024 milliseconds
		call Timer.startPeriodic(1024);

	}
	

	// Event triggered when the timer fires
	event void Timer.fired() {

		// Read temperature, request resource, and print data
		call Temperature.read();
		call Resource.request();

		// Print temperature data sent
		printf("Dati inviati = %d\n", Celsius);

		// Flush the print buffer
		printfflush();

	}
	

	// Event triggered when temperature reading is done
	event void Temperature.readDone(error_t code, uint16_t data) {

		if(code == SUCCESS) {
			// Convert raw temperature data to Celsius
			Celsius = (-39 + 0.01 * data);
            
			// Store temperature data in DataUart array
			DataUart[2] = Celsius >> 8; //MSB
			DataUart[1] = Celsius & 0xff; //LSB

		}
	}
	

	// Event triggered when resource is granted
	event void Resource.granted() {

		// Transmission: Send DataUart array via UartStream and toggle LED
		DataUart[0] = SOP;
		DataUart[3] = EOP;
		if(call UartStream.send(DataUart, 5) == SUCCESS) {
			call Leds.led0Toggle();
		}


	}
	

	// Asynchronous events for send, receive, and byte reception in UartStream
	async event void UartStream.sendDone(uint8_t* buf, uint16_t len, error_t error) {

		// Release the resource after sending is done
		call Resource.release();

	}
	

	async event void UartStream.receivedByte(uint8_t byte) {

		// Release the resource after receiving a byte
		call Resource.release();

	}
	

	async event void UartStream.receiveDone(uint8_t* buf, uint16_t len, error_t error) {

		// Release the resource after receiving is done
		call Resource.release();

	}


}
