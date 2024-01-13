# Water Analysis

## Overview

The Water Analysis project is dedicated to monitoring water quality parameters through the integration of a TelosB device, DS18B20 temperature sensor, TDS sensor, Arduino, and a platform (either a small computer or a specialized board) hosting a Bluetooth server. The TelosB is responsible for data collection from the sensors, transmitting it to the Arduino via UART. Subsequently, the Arduino, equipped with an HC-05 module, communicates the data via Bluetooth to a Java-based Bluetooth Base Station using the BlueCove library. Finally, HTTP requests will be made to a server considering the data collected from the Base Station.
![Design Concept](https://github.com/gubbriaco/water-analysis/assets/101352023/5ae3e3ae-728e-4988-86c5-d189b166aefc)

## Project Components

- **TelosB**: Programmed in nesC language within a TinyOS environment on Linux Ubuntu.

- **Sensors**: Utilizes DS18B20 for temperature measurements and a TDS sensor for water quality analysis.

- **Arduino**: Acts as an intermediary, receiving data from the TelosB via UART and transmitting it via Bluetooth using an HC-05 module.

- **Bluetooth Base Station (Java)**: Implemented in the `FromBluetoothToServer` directory. It accepts data from HC-05 modules concurrently, facilitating an HTTP request to a configurable server.

- **Spring BootServer (Java)**: Implemented in the `server` directory. Handles HTTP requests from the Bluetooth Base Station.

- **Frontend**: Implemented in the `frontend` directory. Make HTTP requests for recovery device data and misuration. These data is shown via cards.

![Hardware Design](https://github.com/gubbriaco/water-analysis/assets/101352023/75458248-455f-4796-90b0-874903a72a2a)
![Software Design](https://github.com/gubbriaco/water-analysis/assets/101352023/be7cc825-eddc-4ece-a8ce-060e8421c4a8)
![Web App UI](https://github.com/gubbriaco/water-analysis/assets/101352023/e574baed-cef4-480e-b041-66497250731d)


## Setup Instructions

1. **TelosB Configuration**: Program the TelosB using nesC in a Linux Ubuntu environment with TinyOS.

2. **HC-05 Configuration**: Configure the HC-05 module using the provided setup file in the `HC05setup` folder and the specified AT-Commands.

3. **Arduino Configuration**: Utilize the code in `FromUartToBluetooth` to configure the Arduino to accept data from the TelosB via UART and transmit it to the Bluetooth server via HC-05.

4. **Bluetooth Base Station Setup**: In the `FromBluetoothToServer` directory, find the Java source code for the Bluetooth Base Station. 

5. **Spring Boot Server Setup**: In the `server` directory, find the Java source code for the server.

6. **Frontend Setup**: Implemented in the `frontend` directory.

7. **Additional Configuration**: Execute the AT command `AT+BIND=bluetooth_address`, replacing `bluetooth_address` with the Bluetooth address of the device running the Java Bluetooth Base Station.

For detailed information, please contact [giorgioubbriaco@protonmail.com](mailto:giorgioubbriaco@protonmail.com).

## Documentation

Explore the `docs/` folder for datasheets related to sensors and boards employed in the project.

## Source Code

- `src/server/`: Java source code for the Spring Boot server.

- `src/frontend/`: Source code for the frontend.

- `src/FromBluetoothToServer/`: Java source code for the Bluetooth Base Station.
  
- `src/TelosBFirmware/`: nesC source code for TelosB, collecting measurements and sending data via UART to Arduino.
  
- `src/ArduinoFirmware/`: Arduino source code receiving data via UART and transmitting it to the Bluetooth server using HC-05.

- `src/HC05setup/`: Arduino source code for the initial configuration of the HC-05 using AT commands.

## Repository Link

[Water Analysis Repository](https://github.com/gubbriaco/water-analysis.git)
