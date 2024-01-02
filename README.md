# Water Analysis

## Overview

The Water Analysis project is dedicated to monitoring water quality parameters through the integration of a TelosB device, DS18B20 temperature sensor, TDS sensor, Arduino, and a platform (either a small computer or a specialized board) hosting a Bluetooth server. The TelosB is responsible for data collection from the sensors, transmitting it to the Arduino via UART. Subsequently, the Arduino, equipped with an HC-05 module, communicates the data via Bluetooth to a Java-based Bluetooth server using the BlueCove library.

## Project Components

- **TelosB**: Programmed in nesC language within a TinyOS environment on Linux Ubuntu.

- **Sensors**: Utilizes DS18B20 for temperature measurements and a TDS sensor for water quality analysis.

- **Arduino**: Acts as an intermediary, receiving data from the TelosB via UART and transmitting it via Bluetooth using an HC-05 module.

- **Bluetooth Server (Java)**: Implemented in the `FromBluetoothToServer` directory. It accepts data from HC-05 modules concurrently, facilitating an HTTP POST request to a configurable server with customizable host and endpoints.

## Setup Instructions

1. **TelosB Configuration**: Program the TelosB using nesC in a Linux Ubuntu environment with TinyOS.

2. **HC-05 Configuration**: Configure the HC-05 module using the provided setup file in the `HC05setup` folder and the specified AT-Commands.

3. **Arduino Configuration**: Utilize the code in `FromUartToBluetooth` to configure the Arduino to accept data from the TelosB via UART and transmit it to the Bluetooth server via HC-05.

4. **Bluetooth Server Setup**: In the `FromBluetoothToServer` directory, find the Java source code for the Bluetooth server. Customize the code for host and endpoint settings.

5. **Additional Configuration**: Execute the AT command `AT+BIND=bluetooth_address`, replacing `bluetooth_address` with the Bluetooth address of the device running the Java Bluetooth server.

For detailed information, please contact [giorgioubbriaco@protonmail.com](mailto:giorgioubbriaco@protonmail.com).

## Documentation

Explore the `docs/` folder for datasheets related to sensors and boards employed in the project.

## Source Code

- `src/FromBluetoothToServer/`: Java source code for the Bluetooth server.
  
- `src/FromTelosBToUART/`: nesC source code for TelosB, collecting measurements and sending data via UART to Arduino.
  
- `src/FromUartToBluetooth/`: Arduino source code receiving data via UART and transmitting it to the Bluetooth server using HC-05.

- `src/HC05setup/`: Arduino source code for the initial configuration of the HC-05 using AT commands.

## Repository Link

[Water Analysis Repository](https://github.com/gubbriaco/water-analysis.git)