#define SOP '<'
#define EOP '>'
#define LENGTH 5
#define Tens_Pin A3

#include <SoftwareSerial.h>
SoftwareSerial mySerial(10, 11); //RX, TX


void setup() {

  Serial.begin(9600);
  mySerial.begin(9600);

}


void loop() {

  //flag per inizio e fine
  bool started = false;
  bool ended = false;

  char inData[LENGTH];
  byte index;
  int Tensione, Temperatura;
  byte Transmit_Data[2];


  while(mySerial.available() > 0) {
    char inChar = mySerial.read();
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
    Temperatura = word(byte(inData[1]), byte(inData[0]));

    Serial.println(Temperatura);

    started = false;
    ended = false;
    index = 0;
    inData[index] = '\0';
  }

  Tensione = analogRead(Tens_Pin);
  Transmit_Data[0] = Tensione & 0xff;
  Transmit_Data[1] = Tensione >> 8;
  
  mySerial.write(Transmit_Data, 2);
  delay(1000);

}
