#include <SoftwareSerial.h>
#include <AltSoftSerial.h>

#include <OneWire.h>
#include <DallasTemperature.h>

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
OneWire oneWireWaterTemperature(ONE_WIRE_BUS_WATER_TEMPERATURE);
DallasTemperature waterTemperatureSensor(&oneWireWaterTemperature);
float readWaterTemperature();

float readWaterPH();

float readWaterTDS();


void readFromSensors(int data[]);
void readQualityParams(int data[]);

int error;
int count;

int TEMPERATURE_POS;
int TDS_POS;
int PH_POS;
int dataArrayLength;

/**
  * Setup function to initialize serial communication.
*/
void setup() {
  Serial.begin(9600);
  while (!Serial) {
  }

  uartSerial.begin(9600);
  error = 1;
  count = 0;
  bluetoothSerial.begin(9600);

  waterTemperatureSensor.begin();

  // Determine the environment and process sensor data
  if (ENVIRONMENT == HOME) {
    TDS_POS=0;
    PH_POS=1;
    dataArrayLength = 2;
  } else if (ENVIRONMENT == POOL) {
    TEMPERATURE_POS=0;
    PH_POS=1;
    dataArrayLength = 2;
  } else if (ENVIRONMENT == SEA) {
    TEMPERATURE_POS=0;
    TDS_POS=1;
    PH_POS=2;
    dataArrayLength = 3;
  }

}

float temperature = 0;
float tds = 0;
float ph = 0;

float temperatureLast = 0;
float tdsLast = 0;
float phLast = 0;

int ack_sent = -1;

int countMeasures = 0;

#define READS 10

void loop() {

    int data[dataArrayLength];
    
    if (error==1) {
      Serial.println("Sensor initialising.");
      delay(1024);
      count = count + 1;
      if (count == READS) {
        error = 0;
      }
    } else if (error == 0) {
        
      Serial.println("----------------------------------------------------------");

      for(int i=0; i<READS; i++) {
        readFromSensors(data);
      }
      
      temperature = temperature / countMeasures;
      tds = tds / countMeasures;
      float V_REF = 2.5;
      float RESOLUTION = 4095;
      float averageVoltage = (tds * V_REF) / RESOLUTION;
      float compensationCoefficient = 1.0 + 0.02 * (temperature - 25.0);
      float compensationVoltage = averageVoltage / compensationCoefficient;
      float tdscompensated = (133.42 * compensationVoltage * compensationVoltage * compensationVoltage - 255.86 * compensationVoltage * compensationVoltage + 857.39 * compensationVoltage) * 0.5;
      ph = ph / countMeasures;

      
      // Transmit sensor data via Bluetooth
      if (ENVIRONMENT == HOME) {
        Serial.print("TDS = ");
        Serial.print(tdscompensated);
        Serial.println(" PPM");
        bluetoothSerial.print(tdscompensated);
        bluetoothSerial.print(",");

        Serial.print("pH = ");
        Serial.println(ph);
        bluetoothSerial.print(ph);
        bluetoothSerial.print(";");

        successfullyBluetooth();

      } else if (ENVIRONMENT == POOL) {
        Serial.print("Temperature = ");
        Serial.print(temperature);
        Serial.println(" °C");
        bluetoothSerial.print(temperature);
        bluetoothSerial.print(",");

        Serial.print("pH = ");
        Serial.println(ph);
        bluetoothSerial.print(ph);
        bluetoothSerial.print(";");

        successfullyBluetooth();

      } else if (ENVIRONMENT == SEA) {
        Serial.print("Temperature = ");
        Serial.print(temperature);
        Serial.println(" °C");
        bluetoothSerial.print(temperature);
        bluetoothSerial.print(",");
        
        Serial.print("TDS = ");
        Serial.print(tdscompensated);
        Serial.println(" PPM");
        bluetoothSerial.print(tdscompensated);
        bluetoothSerial.print(",");

        Serial.print("pH = ");
        Serial.println(ph);
        bluetoothSerial.print(ph);
        bluetoothSerial.print(";");

        successfullyBluetooth();

      } else {
        failureBluetooth();

      }

      delay(20);
      
    }

    temperature = 0;
    tds = 0;
    ph = 0;
    countMeasures = 0;

}

void readFromSensors(int data[]) {
  readQualityParams(data);
  if (ack_sent == 0) {
    temperature = temperature - temperatureLast;
    tds = tds - tdsLast;
    ph = ph - phLast;
  } else if(ack_sent == 1) {
    if (temperatureLast < -55) {
      temperature = temperature - temperatureLast;
    }
    if (temperatureLast > 125) {
      temperature = temperature - temperatureLast;
    }
    if (tdsLast < 0) {
      tds = tds - tdsLast;
    }
    if (tdsLast > 1000) {
      tds = tds - tdsLast;
    }
    if (phLast < 0) {
      ph = ph - phLast;
    }
    if (phLast > 14) {
      ph = ph - phLast;
    }
    countMeasures = countMeasures + 1;
  }
}


void readQualityParams(int data[]) {
  delay(1024);

  // Flag per inizio e fine
  bool started = false;
  bool ended = false;

  char inData[LENGTH];
  byte index;
  byte dataTransmitted[2];
  int ack;

  float temperatureValue;
  int tdsValue;
  float phValue;

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

  
  // Transmit sensor data via Bluetooth
  if (ENVIRONMENT == HOME) {
    tdsValue = readWaterTDS(data);
    phValue = readWaterPH(data);

  } else if (ENVIRONMENT == POOL) {
    temperatureValue = readWaterTemperature();
    phValue = readWaterPH(data);


  } else if (ENVIRONMENT == SEA) {
    temperatureValue = readWaterTemperature();
    tdsValue = readWaterTDS(data);
    phValue = readWaterPH(data);

  }

  temperatureLast = temperatureValue;
  tdsLast = tdsValue;
  phLast = phValue;

  temperature = temperature + temperatureLast;
  tds = tds + tdsLast;
  ph = ph + phValue;

  ack_sent = ack;
}


float readWaterTemperature() {
  waterTemperatureSensor.requestTemperatures();
  float temperature_curr = waterTemperatureSensor.getTempCByIndex(0);
  return temperature_curr;
}


float readWaterPH(int data[]) {
  // float adc_pH7 = 2.50;
  float adc_pH = data[PH_POS];
  float ph_curr = (-0.003875598 * adc_pH) + 16.3265548;
  return ph_curr;
}


float readWaterTDS(int data[]) {
  float tds_curr = data[TDS_POS];
  return tds_curr;
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
