#include <SoftwareSerial.h>
#include <AltSoftSerial.h>

// Define Start of Packet (SOP) and End of Packet (EOP) characters
#define SOP '<'
#define EOP '>'
#define LENGTH 9

// Define UART pin configuration
#define UART_TX_PIN 9
#define UART_RX_PIN 8
AltSoftSerial uartSerial;

// Define Bluetooth pin configuration
#define BT_TX_PIN 11
#define BT_RX_PIN 10
SoftwareSerial bluetoothSerial(BT_RX_PIN, BT_TX_PIN); // RX, TX

/**
  * ENVIRONMENT = 0 -> HOME
  * ENVIRONMENT = 1 -> POOL
  * ENVIRONMENT = 2 -> SEA
*/
#define ENVIRONMENT 1
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



/**
  * Setup function to initialize serial communication.
*/
void setup() {
  Serial.begin(9600);
  while (!Serial) {
  }

  uartSerial.begin(9600);
  bluetoothSerial.begin(9600);
}



void loop() {

  delay(2000);
  Serial.println("----------------------------------------------------------");

  // Flag per inizio e fine
  bool started = false;
  bool ended = false;

  char inData[LENGTH];
  byte index;
  int data[3];
  byte dataTransmitted[2];
  int ack;

  int temperatureValue;
  int tdsValue;
  int phValue;

  
  int TEMPERATURE_POS;
  int TDS_POS;
  int PH_POS;


  // Read data from UART communication
  while (uartSerial.available() > 0) {
    char inChar = uartSerial.read();
    if (inChar == SOP) {
      index = 0;
      inData[index] = '\0';
      started = true;
      ended = false;
    } else if (inChar == EOP) {
      ended = true;
      break;
    } else {
      if (index < LENGTH) {
        inData[index] = inChar;
        index++;
        inData[index] = '\0';
      }
    }
  }

  // Process received data
  if (started && ended) {
    for (int i = 0; i < 3; i++) {
      data[i] = word(byte(inData[2 * i + 1]), byte(inData[2 * i]));
    }
    ack = 1;
    successfullyUART();
  } else {
    ack = 0;
    failureUART();
  }

  // Reset flags for the next packet
  started = false;
  ended = false;
  index = 0;
  inData[index] = '\0';

  // Transmit acknowledgement to the UART device
  dataTransmitted[0] = ack & 0xff;
  dataTransmitted[1] = ack >> 8;
  uartSerial.write(dataTransmitted, 2);
  Serial.print("ack_sent = ");
  Serial.println(ack);


  // Determine the environment and process sensor data
  if (ENVIRONMENT == HOME) {
    TDS_POS=0;
    PH_POS=1;
  } else if (ENVIRONMENT == POOL) {
    TEMPERATURE_POS=0;
    PH_POS=1;
  } else if (ENVIRONMENT == SEA) {
    TEMPERATURE_POS=0;
    TDS_POS=1;
    PH_POS=2;
  }

  
  // Transmit sensor data via Bluetooth
  if (ENVIRONMENT == HOME) {
    tdsValue = data[TDS_POS];
    Serial.print("TDS = ");
    Serial.print(tdsValue);
    Serial.println(" PPM");
    bluetoothSerial.print(tdsValue);
    bluetoothSerial.print(",");

    phValue = data[PH_POS];
    Serial.print("pH = ");
    Serial.println(phValue);
    bluetoothSerial.print(phValue);
    bluetoothSerial.print(";");

    successfullyBluetooth();

  } else if (ENVIRONMENT == POOL) {
    temperatureValue = data[TEMPERATURE_POS];
    Serial.print("Temperature = ");
    Serial.print(temperatureValue);
    Serial.println(" Celsius");
    bluetoothSerial.print(temperatureValue);
    bluetoothSerial.print(",");

    phValue = data[PH_POS];
    Serial.print("pH = ");
    Serial.println(phValue);
    bluetoothSerial.print(phValue);
    bluetoothSerial.print(";");

    successfullyBluetooth();

  } else if (ENVIRONMENT == SEA) {
    temperatureValue = data[TEMPERATURE_POS];
    Serial.print("Temperature = ");
    Serial.print(temperatureValue);
    Serial.println(" Celsius");
    bluetoothSerial.print(temperatureValue);
    bluetoothSerial.print(",");
    
    tdsValue = data[TDS_POS];
    Serial.print("TDS = ");
    Serial.print(tdsValue);
    Serial.println(" PPM");
    bluetoothSerial.print(tdsValue);
    bluetoothSerial.print(",");

    phValue = data[PH_POS];
    Serial.print("pH = ");
    Serial.println(phValue);
    bluetoothSerial.print(phValue);
    bluetoothSerial.print(";");

    successfullyBluetooth();

  } else {
    failureBluetooth();

  }

  delay(20);
}



/**
  * Indicate successful UART communication.
*/
void successfullyUART() {
  digitalWrite(SUCCESSFULLY_UART_PIN, HIGH);
  delay(750);
  digitalWrite(SUCCESSFULLY_UART_PIN, LOW);
}


/**
  * Indicate failure in UART communication.
*/
void failureUART() {
  digitalWrite(FAILURE_UART_PIN, HIGH);
  delay(750);
  digitalWrite(FAILURE_UART_PIN, LOW);
}


/**
  * Indicate successful Bluetooth communication.
*/
void successfullyBluetooth() {
  digitalWrite(SUCCESSFULLY_BLUETOOTH_PIN, HIGH);
  delay(750);
  digitalWrite(SUCCESSFULLY_BLUETOOTH_PIN, LOW);
}


/**
  * Indicate failure in Bluetooth communication.
*/
void failureBluetooth() {
  digitalWrite(FAILURE_BLUETOOTH_PIN, HIGH);
  delay(750);
  digitalWrite(FAILURE_BLUETOOTH_PIN, LOW);
}
