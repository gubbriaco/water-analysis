// mioHeader.h
#ifndef MIOHEADER_H




// Number of sensors
#define NR_SENSORS 3


// Baud rate
#define BAUD_RATE 9600


// Sampling time for sensors
#define SAMPLING_TIME_SENSORS_INITIALISATION 1024

// Sampling time for sensors
#define SAMPLING_TIME_UART 1024

// Sampling time for bluetooth
#define SAMPLING_TIME_BLUETOOTH 1024


// Number of reads for each average
#define READS 10


// Define Start of Packet (SOP) and End of Packet (EOP) characters
#define SOP '<'
#define EOP '>'
#define LENGTH 9


// Define UART pin configuration
#define UART_TX_PIN 9
#define UART_RX_PIN 8


// Define Bluetooth pin configuration
#define BT_TX_PIN 11
#define BT_RX_PIN 10


/**
  * ENVIRONMENT = 0 -> HOME
  * ENVIRONMENT = 1 -> POOL
  * ENVIRONMENT = 2 -> SEA
*/
#define ENVIRONMENT 2
#define HOME 0
#define POOL 1
#define SEA 2


// Define pins for successfully UART communication indicators
#define SUCCESSFULLY_UART_PIN 7
/**
  * Indicate successful UART communication.
*/
void successfullyUART();

// Define pins for failure in UART communication indicators
#define FAILURE_UART_PIN 6
/**
  * Indicate failure in UART communication.
*/
void failureUART();

// Define pins for successfully Bluetooth communication indicators
#define SUCCESSFULLY_BLUETOOTH_PIN 5
/**
  * Indicate successful Bluetooth communication.
*/
void successfullyBluetooth();

// Define pins for failure in Bluetooth communication indicators
#define FAILURE_BLUETOOTH_PIN 4
/**
  * Indicate failure in Bluetooth communication.
*/
void failureBluetooth();

#define ONE_WIRE_BUS_WATER_TEMPERATURE 3


/**
 * Reads the water temperature using the temperature sensor.
 *
 * @return The current water temperature in degrees Celsius.
 */
float readWaterTemperature();

/**
 * Reads the pH level of water based on the provided analog sensor data.
 *
 * @param data An array containing analog sensor readings, with pH data at index PH_POS.
 * @return The current pH level of the water.
 */
float readWaterPH(int data[]);

/**
 * Reads the Total Dissolved Solids (TDS) level of water based on the provided sensor data.
 *
 * @param data An array containing sensor readings, with TDS data at index TDS_POS.
 * @return The current TDS level of the water.
 */
float readWaterTDS(int data[]);


/**
 * Reads data from sensors using UART communication and updates environmental parameters.
 *
 * @param data An array to store the sensor readings.
 */
void readFromSensors(int data[]);

/**
 * Reads data from UART communication and processes it to update environmental parameters.
 * This function expects data packets with a Start of Packet (SOP) and End of Packet (EOP) markers.
 * The received data is parsed and used to update temperature, TDS, and pH values.
 * @param data An array containing sensor readings.
 */
void readQualityParams(int data[]);


// Temperature lower bound and upper bound
#define TEMPERATURE_LOWER_BOUND -55
#define TEMPERATURE_UPPER_BOUND 125

// TDS lower bound and upper bound
#define TDS_LOWER_BOUND 0
#define TDS_UPPER_BOUND 1000

// pH lower bound and upper bound
#define PH_LOWER_BOUND 0
#define PH_UPPER_BOUND 14


// ADC TelosB Voltage reference
#define V_REF 2.5

// ADC TelosB resolution
#define RESOLUTION 4095


// Slope of the pH sensor characteristic
#define SLOPE_PH -0.003875598

// Intercept of pH sensor characteristic
#define INTERCEPT_PH 16.3265548


// LEDs delay
#define LED_DELAY 750



#endif



