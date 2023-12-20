#include "BluetoothSerial.h"

#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif

BluetoothSerial bluetoothSerial;

// HC-05 MAC address
// 98:D3:41:F6:DC:F6
uint8_t hc05_mac_address[] = {0x98,0xD3,0x41,0xF6,0xDC,0xF6};

void setup() {
  Serial.begin(115200);
  delay(1000);  // Wait for Serial to initialize - adjust as needed

  bluetoothSerial.begin("ESP32", true); // Bluetooth device name
}

void loop() {
  if (!bluetoothSerial.connected()) {
    // Try to connect to the HC-05 module
    Serial.println("Connecting to HC-05...");
    if (bluetoothSerial.connect(hc05_mac_address)) {
      Serial.println("Connected to HC-05");
    } else {
      Serial.println("Failed to connect to HC-05");
      delay(5000); // Wait before trying to reconnect
      return;
    }
  }

  // Example: Reading a byte from BluetoothSerial
  if (bluetoothSerial.available()) {
    int temperature = bluetoothSerial.read();
    Serial.println("Received temperature: " + String(temperature));
  }

  delay(2000);
}