#include <SoftwareSerial.h>
#include <AltSoftSerial.h>

#include <OneWire.h>
#include <DallasTemperature.h>

#include "ArduinoDefinitions.h"


AltSoftSerial uartSerial;

SoftwareSerial bluetoothSerial(BT_RX_PIN, BT_TX_PIN);

OneWire oneWireWaterTemperature(ONE_WIRE_BUS_WATER_TEMPERATURE);
DallasTemperature waterTemperatureSensor(&oneWireWaterTemperature);


int TEMPERATURE_POS;
int TDS_POS;
int PH_POS;
int dataArrayLength;


/**
  * Setup function to initialize serial communication.
*/
void setup() {
  Serial.begin(BAUD_RATE);
  while (!Serial) {
  }

  uartSerial.begin(BAUD_RATE);
  bluetoothSerial.begin(BAUD_RATE);

  waterTemperatureSensor.begin();

  // Determine the environment and process sensor data
  if (ENVIRONMENT == HOME) {
    TEMPERATURE_POS=0;
    TDS_POS=1;
    PH_POS=2;
    dataArrayLength = 3;
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


void loop() {

    // An array containing sensor readings.
    int data[dataArrayLength];
    
    
    Serial.println("");
    Serial.println("################################");

    // Read sensor data multiple times to calculate averages
    for(int i=0; i<READS_BLUETOOTH; i++) {
      readFromSensors(data);
    }
    
    // Calculate average temperature
    temperature = getTemperatureAverage(temperature, countMeasures);
    // Convert TDS readings to compensated values based on temperature
    tds = getTotalDissolvedMetalsAverage(tds, countMeasures, temperature);
    // Calculate average pH
    ph = getpHAverage(ph, countMeasures);

    
    // Transmit sensor data via Bluetooth based on the current environment
    if (ENVIRONMENT == HOME) {
      serialEnvironmentBased(temperature, tds, ph);
      successfullyBluetooth();

    } else if (ENVIRONMENT == POOL) {
      serialEnvironmentBased(temperature, -1, ph);
      successfullyBluetooth();

    } else if (ENVIRONMENT == SEA) {
      serialEnvironmentBased(temperature, tds, ph);
      successfullyBluetooth();

    } else {
      // If the environment is not recognized, indicate a Bluetooth failure
      failureBluetooth();
    }

    delay(20);
      
    

    // Reset variables for the next set of measurements
    temperature = 0;
    tds = 0;
    ph = 0;
    countMeasures = 0;

}


/**
 * Prints environmental data to the Serial monitor and transmits it via Bluetooth.
 *
 * @param temperature The temperature data to be printed and transmitted.
 * @param tds The Total Dissolved Solids (TDS) data to be printed and transmitted.
 * @param ph The pH data to be printed and transmitted.
 */
void serialEnvironmentBased(float temperature, float tds, float ph) {
  // Call serialPrint to print data to the Serial monitor
  serialPrint(temperature, tds, ph);
  
  // Call sendViaBluetooth to transmit data via Bluetooth
  sendViaBluetooth(temperature, tds, ph);
}


/**
 * Prints environmental data to the Serial monitor.
 *
 * @param temperature The temperature data to be printed.
 * @param tds The Total Dissolved Solids (TDS) data to be printed.
 * @param ph The pH data to be printed.
 */
void serialPrint(float temperature, float tds, float ph) {
  if (temperature == -1) {
    Serial.print("Temperature = ");
    Serial.print("NULL");
    Serial.println(" °C");
  } else if (tds == -1) {
    Serial.print("TDS = ");
    Serial.print("NULL");
    Serial.println(" PPM");
  } else if (ph == -1) {
    Serial.print("pH = ");
    Serial.println("NULL");
  } else {
    Serial.print("Temperature = ");
    Serial.print(temperature);
    Serial.println(" °C");
        
    Serial.print("TDS = ");
    Serial.print(tds);
    Serial.println(" PPM");
    
    Serial.print("pH = ");
    Serial.println(ph);
  }
 
}


/**
 * Sends environmental data via Bluetooth.
 *
 * @param temperature The temperature data to be transmitted.
 * @param tds The Total Dissolved Solids (TDS) data to be transmitted.
 * @param ph The pH data to be transmitted.
 */
void sendViaBluetooth(float temperature, float tds, float ph) {
  if (temperature == -1) {
    bluetoothSerial.print("NULL");
  } else {
    bluetoothSerial.print(temperature);
  }
  bluetoothSerial.print(",");
  if (tds == -1) {
    bluetoothSerial.print("NULL");
  } else {
    bluetoothSerial.print(tds);
  }
  bluetoothSerial.print(",");
  if (ph == -1) {
    bluetoothSerial.println("NULL");
  } else {
    bluetoothSerial.print(ph);
  }
  bluetoothSerial.print(";");
}


/**
 * Calculates and returns the average temperature based on the accumulated temperature values
 * and the count of temperature measurements.
 *
 * @param temperature The sum of temperature values.
 * @param countMeasures The count of temperature measurements.
 * @return The average temperature.
 */
float getTemperatureAverage(float temperature, int countMeasures) {
  // Calculate average temperature, TDS, and pH
  float temperatureAverage = temperature = temperature / countMeasures;
  return temperatureAverage;
}


/**
 * Calculates and returns the compensated average Total Dissolved Solids (TDS) based on the
 * accumulated TDS values, the count of TDS measurements, and the corresponding temperature.
 *
 * @param tds The sum of TDS values.
 * @param countMeasures The count of TDS measurements.
 * @param temperature The corresponding temperature for TDS compensation.
 * @return The compensated average TDS.
 */
float getTotalDissolvedMetalsAverage(float tds, int countMeasures, float temperature) {
  // Convert TDS readings to compensated values based on temperature
  float tdsAverage = tds / countMeasures;
  float averageVoltage = (tdsAverage * V_REF) / RESOLUTION;
  float compensationCoefficient = 1.0 + 0.02 * (temperature - 25.0);
  float compensationVoltage = averageVoltage / compensationCoefficient;
  float tdscompensated = (133.42 * compensationVoltage * compensationVoltage * compensationVoltage - 255.86 * compensationVoltage * compensationVoltage + 857.39 * compensationVoltage) * 0.5;
  return tdscompensated;
}


/**
 * Calculates and returns the average pH based on the accumulated pH values
 * and the count of pH measurements.
 *
 * @param ph The sum of pH values.
 * @param countMeasures The count of pH measurements.
 * @return The average pH.
 */
float getpHAverage(float ph, int countMeasures) {
  float phAverage = ph / countMeasures;
  return phAverage;
}


/**
 * Reads data from sensors using UART communication and updates environmental parameters.
 *
 * @param data An array to store the sensor readings.
 */
void readFromSensors(int data[]) {
  // Read data from UART communication
  readFromUART(data);

  // Check the acknowledgment status (ack_sent) for successful data transmission
  if (ack_sent == 0) {
    // If acknowledgment indicates failure, subtract the last measurements from cumulative values
    temperature = temperature - temperatureLast;
    tds = tds - tdsLast;
    ph = ph - phLast;
  } 
  // If acknowledgment indicates success, validate and update the cumulative values
  else if(ack_sent == 1) {
    if (temperatureLast < TEMPERATURE_LOWER_BOUND) {
      temperature = temperature - temperatureLast;
    }
    if (temperatureLast > TEMPERATURE_UPPER_BOUND) {
      temperature = temperature - temperatureLast;
    }
    if (tdsLast < TDS_LOWER_BOUND) {
      tds = tds - tdsLast;
    }
    if (tdsLast > TDS_UPPER_BOUND) {
      tds = tds - tdsLast;
    }
    if (phLast < PH_LOWER_BOUND) {
      ph = ph - phLast;
    }
    if (phLast > PH_UPPER_BOUND) {
      ph = ph - phLast;
    }
    // Increment the count of successful measurements
    countMeasures = countMeasures + 1;
  }
}


/**
 * Reads data from UART communication and processes it to update environmental parameters.
 * This function expects data packets with a Start of Packet (SOP) and End of Packet (EOP) markers.
 * The received data is parsed and used to update temperature, TDS, and pH values.
 * @param data An array containing sensor readings.
 */
void readFromUART(int data[]) {
  delay(SAMPLING_TIME_UART);

  // Flags for start and end
  bool started = false;
  bool ended = false;

  // Buffer to store incoming data
  char inData[LENGTH];
  byte index;
  byte dataTransmitted[2];
  int ack;

  // Variables to store parsed data
  float temperatureValue;
  int tdsValue;
  float phValue;

  // Read data from UART communication
  while (uartSerial.available() > 0) {
    char inChar = uartSerial.read();
    if (inChar == SOP) {
      // Start of Packet marker detected
      index = 0;
      inData[index] = '\0';
      started = true;
      ended = false;
    } else if (inChar == EOP) {
      // End of Packet marker detected
      ended = true;
      break;
    } else {
      // Store the incoming character in the buffer
      if (index < LENGTH) {
        inData[index] = inChar;
        index++;
        inData[index] = '\0';
      }
    }
  }

  // Process received data
  if (started && ended) {
    // Parse and update data array
    for (int i = 0; i < NR_SENSORS; i++) {
      data[i] = word(byte(inData[2 * i + 1]), byte(inData[2 * i]));
    }
    // Acknowledge successful data reception
    ack = 1;
    successfullyUART();
  } else {
    // Acknowledge failure in data reception
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

  // Update last acknowledgment sent
  ack_sent = ack;

  // Process quality parameters based on the received data
  processingQualityParams(data, temperatureValue, tdsValue, phValue);

}

 
/**
 * Processes and updates environmental quality parameters based on the provided data and current environment.
 *
 * @param data An array containing sensor readings.
 * @param temperatureValue The current temperature value to be processed and updated.
 * @param tdsValue The current Total Dissolved Solids (TDS) value to be processed and updated.
 * @param phValue The current pH value to be processed and updated.
 */
void processingQualityParams(int data[], float temperatureValue, float tdsValue, float phValue) {
  // Environment-dependent data processing
  if (ENVIRONMENT == HOME) {
    // Update temperature, TDS, and pH values for home environment
    temperatureValue = readWaterTemperature();
    tdsValue = readWaterTDS(data);
    phValue = readWaterPH(data);

  } else if (ENVIRONMENT == POOL) {
    // Update temperature and pH values for pool environment
    temperatureValue = readWaterTemperature();
    phValue = readWaterPH(data);

  } else if (ENVIRONMENT == SEA) {
    // Update temperature, TDS, and pH values for sea environment
    temperatureValue = readWaterTemperature();
    tdsValue = readWaterTDS(data);
    phValue = readWaterPH(data);

  }

  // Update last measured temperature, TDS, and pH values
  temperatureLast = temperatureValue;
  tdsLast = tdsValue;
  phLast = phValue;

  // Add last measured values to the corresponding current sum
  temperature = temperature + temperatureLast;
  tds = tds + tdsLast;
  ph = ph + phLast;
}



/**
 * Reads the water temperature using the temperature sensor.
 *
 * @return The current water temperature in degrees Celsius.
 */
float readWaterTemperature() {
  // Requests temperature reading from the sensor
  waterTemperatureSensor.requestTemperatures();
  // Get value from sensor
  float temperature_curr = waterTemperatureSensor.getTempCByIndex(0);
  return temperature_curr;
}


/**
 * Reads the pH level of water based on the provided analog sensor data.
 *
 * @param data An array containing analog sensor readings, with pH data at index PH_POS.
 * @return The current pH level of the water.
 */
float readWaterPH(int data[]) {
  // float adc_pH7 = 2.50;

  // Extracts the pH analog reading from the provided data array
  float adc_pH = data[PH_POS];
  
  // Converts the analog pH reading to actual pH using the calibration formula
  float ph_curr = (SLOPE_PH * adc_pH) + INTERCEPT_PH;
  return ph_curr;
}


/**
 * Reads the Total Dissolved Solids (TDS) level of water based on the provided sensor data.
 *
 * @param data An array containing sensor readings, with TDS data at index TDS_POS.
 * @return The current TDS level of the water.
 */
float readWaterTDS(int data[]) {
  // Extracts the TDS reading from the provided data array
  float tds_curr = data[TDS_POS];
  
  return tds_curr;
}


/**
  * Indicate successful UART communication.
*/
void successfullyUART() {
  digitalWrite(SUCCESSFULLY_UART_PIN, HIGH);
  delay(LED_DELAY);
  digitalWrite(SUCCESSFULLY_UART_PIN, LOW);
}


/**
  * Indicate failure in UART communication.
*/
void failureUART() {
  digitalWrite(FAILURE_UART_PIN, HIGH);
  delay(LED_DELAY);
  digitalWrite(FAILURE_UART_PIN, LOW);
}


/**
  * Indicate successful Bluetooth communication.
*/
void successfullyBluetooth() {
  digitalWrite(SUCCESSFULLY_BLUETOOTH_PIN, HIGH);
  delay(LED_DELAY);
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
