#include <SoftwareSerial.h>
#include <AltSoftSerial.h>

#define SOP '<'
#define EOP '>'
#define LENGTH 9

#define UART_TX_PIN 9
#define UART_RX_PIN 8
AltSoftSerial uartSerial;

#define BT_TX_PIN 11
#define BT_RX_PIN 10
SoftwareSerial bluetoothSerial(BT_RX_PIN, BT_TX_PIN); // RX, TX

#define TEMPERATURE_POS 0
#define TDS_POS 1
#define PH_POS 2

/**
  * ENVIRONMENT = 0 -> HOME
  * ENVIRONMENT = 1 -> POOL
  * ENVIRONMENT = 2 -> SEA
*/
#define ENVIRONMENT 1
#define HOME 0
#define POOL 1
#define SEA 2



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

  if (started && ended) {
    for (int i = 0; i < 3; i++) {
      data[i] = word(byte(inData[2 * i + 1]), byte(inData[2 * i]));
    }
    ack = 1;
  } else {
    ack = 0;
  }

  // Reset per i prossimi pacchetti
  started = false;
  ended = false;
  index = 0;
  inData[index] = '\0';

  dataTransmitted[0] = ack & 0xff;
  dataTransmitted[1] = ack >> 8;
  uartSerial.write(dataTransmitted, 2);
  Serial.print("ack_sent = ");
  Serial.println(ack);

  
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
  }

  delay(20);
}
