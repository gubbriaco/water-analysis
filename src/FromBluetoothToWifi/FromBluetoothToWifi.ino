#include "BluetoothSerial.h"

#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif

#define SOP '<'
#define EOP '>'
#define LENGTH 5

BluetoothSerial SerialBT;


void setup() {
  Serial.begin(9600);
  SerialBT.begin("ESP32");
}


void loop() {

  //flag per inizio e fine
  bool started = false;
  bool ended = false;

  char inData[LENGTH];
  byte index;
  int temperature;
  int ack;


  while(SerialBT.available() > 0) {
    char inChar = SerialBT.read();
    if(inChar == SOP) {
      index = 0;
      inData[index] = '\0';
      started = true;
      ended = false;
    }
    else if(inChar == EOP) {
      ended = true;
      break;
    }
    else {
      if(index < LENGTH) {
        inData[index] = inChar;
        index++;
        inData[index] = '\0';
      }
    }
  }


  // Reset per i prossimi pacchetti
  if(started && ended) {
    temperature = word(byte(inData[1]), byte(inData[0]));
    Serial.println(temperature);
    ack = 1;

    started = false;
    ended = false;
    index = 0;
    inData[index] = '\0';
  }
  else {
    ack = 0;
    
    started = false;
    ended = false;
    index = 0;
    inData[index] = '\0';
  }

  delay(1000);
}