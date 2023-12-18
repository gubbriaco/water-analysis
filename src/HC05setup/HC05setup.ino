#include <SoftwareSerial.h>

#define BT_TX_PIN 9
#define BT_RX_PIN 8
SoftwareSerial bluetoothSerial(BT_RX_PIN, BT_TX_PIN);


char c = ' ';
void setup() {
  Serial.begin(9600);
  Serial.println("ready");
  bluetoothSerial.begin(38400);

}

void loop() {
  
  if(bluetoothSerial.available()) {
    c = bluetoothSerial.read();
    Serial.write(c);
  }

  if(Serial.available()) {
    c = Serial.read();
    bluetoothSerial.write(c);
  }

}