#include "BluetoothSerial.h"

#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif

BluetoothSerial bluetoothSerial;

// HC-05 MAC address
// 98:D3:41:F6:DC:F6
uint8_t hc05_mac_address[] = {0x98,0xD3,0x41,0xF6,0xDC,0xF6};

// Bluetooth Device Name to connect
String deviceNameToConnect = "HC05";
char *pin = "1234";
bool connected;

void setup() {
  Serial.begin(115200);
  delay(1000);  // Wait for Serial to initialize - adjust as needed
  bluetoothSerial.begin("ESP32", true);
  
  connected = bluetoothSerial.connect(hc05_mac_address);
  if(connected) {
    Serial.println("Connected Succesfully!");
  } else {
    while(!bluetoothSerial.connected(10000)) {
      Serial.println("Failed to connect. Make sure remote device is available and in range, then restart app."); 
    }
  }
  // disconnect() may take upto 10 secs max
  if (bluetoothSerial.disconnect()) {
    Serial.println("Disconnected Succesfully!");
  }
  // this would reconnect to the name(will use address, if resolved) or address used with connect(name/address).
  bluetoothSerial.connect();
}

void loop() {

  if (Serial.available()) {
    bluetoothSerial.write(Serial.read());
  }
  if (bluetoothSerial.available()) {
    Serial.write(bluetoothSerial.read());
  }
  delay(20);

}