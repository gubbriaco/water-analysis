#include <SoftwareSerial.h>
#include <AltSoftSerial.h>


#define SOP '<'
#define EOP '>'
#define LENGTH 5

#define UART_TX_PIN 9
#define UART_RX_PIN 8
AltSoftSerial uartSerial;

#define BT_TX_PIN 11
#define BT_RX_PIN 10
SoftwareSerial bluetoothSerial(BT_RX_PIN, BT_TX_PIN); //RX, TX

void setup() {

  Serial.begin(9600);
  while (!Serial) {
  }

  uartSerial.begin(9600);
  bluetoothSerial.begin(9600);
  
}


void loop() {

  delay(2000);


  //flag per inizio e fine
  bool started = false;
  bool ended = false;

  char inData[LENGTH];
  byte index;
  int temperature;


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


  if(started && ended) {
    temperature = word(byte(inData[1]), byte(inData[0]));
    Serial.println(temperature);
  }

  // Reset per i prossimi pacchetti
  started = false;
  ended = false;
  index = 0;
  inData[index] = '\0';
  

  bluetoothSerial.print(temperature);
  bluetoothSerial.print(";");
  
  
  delay(20);

}