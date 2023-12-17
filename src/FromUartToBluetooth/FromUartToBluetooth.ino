#define SOP '<'
#define EOP '>'
#define LENGTH 5

#include <SoftwareSerial.h>
#define UART_TX_PIN 11
#define UART_RX_PIN 10
SoftwareSerial uartSerial(UART_RX_PIN, UART_TX_PIN); //RX, TX

#define BT_TX_PIN 9
#define BT_RX_PIN 8
SoftwareSerial bluetoothSerial(BT_RX_PIN, BT_TX_PIN);


void setup() {

  pinMode(BT_RX_PIN, INPUT);
  pinMode(BT_TX_PIN, OUTPUT);

  Serial.begin(9600);
  uartSerial.begin(9600);
  //bluetoothSerial.begin(9600);
  
}


void loop() {

  //flag per inizio e fine
  bool started = false;
  bool ended = false;

  char inData[LENGTH];
  byte index;
  int temperature;
  byte dataTransmitted[2];
  int ack;


  while(uartSerial.available() > 0) {
    char inChar = uartSerial.read();
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
  
  dataTransmitted[0] = ack & 0xff;
  dataTransmitted[1] = ack >> 8;
  uartSerial.write(dataTransmitted, 2);

  delay(1000);

}