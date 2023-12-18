#include "esp_bt_device.h"
#include "BluetoothSerial.h"


void printDeviceAddress() {
  const uint8_t* point = esp_bt_dev_get_address();

  for(int i=0; i<6; i++) {
    char str[3];
    sprintf(str, "%02X", (int)point[i]);
    Serial.print(str);

    if(i < 5) {
      Serial.print(":");
    }
  }
}

BluetoothSerial SerialBT;

void setup() {
  Serial.begin(115200);
  SerialBT.begin("ESP32");
  printDeviceAddress();
}

void loop() {
  SerialBT.println("Hello World");
  delay(1000);
}