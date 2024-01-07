#!/bin/bash

if [ "$#" -eq 0 ]; then
  echo "Usage: $0 <argument>"
  exit 1
fi

# Access the first argument using $1
arg1="$1"

MAINAPP_DIR="./MainApp.nc"

cat <<EOL > "$MAINAPP_DIR"
/**
 * @file MainApp.nc
 * @brief TinyOS configuration for the MainApp.
 * @author [gubbriaco, fnicoletti, agrandinetti]
 */

/**
 * @configuration MainApp
 * @desc TinyOS configuration for the MainApp.
 */
configuration MainApp {
}



/**
 * @implementation
 */
implementation {
	
	components MainC;
	components Main;
	components new TimerMilliC() as Timer;
	components LedsC;
	components PrintfC, SerialStartC;
	components UARTdriver;
	components new Msp430Uart0C(); 
	components WaterAnalysis;
	components QualityParamC;
EOL


generate_components() {
    case $arg1 in
        "home")
            cat <<EOL >> "$MAINAPP_DIR"
	components TDSC;
	components new Msp430SparkfunTDSC() as TDSsensorDriver;
	components PHC;
	components new Msp430SparkfunPHC() as PHsensorDriver;
EOL
            ;;
        "sea")
            cat <<EOL >> "$MAINAPP_DIR"
	components TemperatureC;
	components TDSC;
	components new Msp430SparkfunTDSC() as TDSsensorDriver;
	components PHC;
	components new Msp430SparkfunPHC() as PHsensorDriver;
EOL
            ;;
        "pool")
            cat <<EOL >> "$MAINAPP_DIR"
	components TemperatureC;
	components PHC;
	components new Msp430SparkfunPHC() as PHsensorDriver;
EOL
            ;;
        *)
            echo "Unknown value in argument: $arg1"
            exit 1
            ;;
    esac
}

generate_components

cat <<EOL >> "$MAINAPP_DIR"

	Main.Boot -> MainC;
	Main.Leds -> LedsC;
	Main.TimerSampling -> Timer;
	Main.Driver -> UARTdriver;
	Main.Analysis -> WaterAnalysis;
	Main.TimerDriver -> Timer;
	WaterAnalysis.Leds -> LedsC;
	WaterAnalysis.QualityParam -> QualityParamC;
	UARTdriver.Resource -> Msp430Uart0C;
	UARTdriver.UartStream -> Msp430Uart0C;
	UARTdriver.Msp430UartConfigure <- Msp430Uart0C;
	UARTdriver.Leds -> LedsC;
EOL


generate_wirings() {
    case $arg1 in
        "home")
            cat <<EOL >> "$MAINAPP_DIR"
	QualityParamC.TDS -> TDSC;
	TDSC.TDSmeasure -> TDSsensorDriver.TDSreadInterface;
	QualityParamC.PH -> PHC;
	PHC.PHmeasure -> PHsensorDriver.PHreadInterface;
EOL
            ;;
        "sea")
            cat <<EOL >> "$MAINAPP_DIR"
	QualityParamC.Temperature -> TemperatureC;
	QualityParamC.TDS -> TDSC;
	TDSC.TDSmeasure -> TDSsensorDriver.TDSreadInterface;
	QualityParamC.PH -> PHC;
	PHC.PHmeasure -> PHsensorDriver.PHreadInterface;
EOL
            ;;
        "pool")
            cat <<EOL >> "$MAINAPP_DIR"
	QualityParamC.Temperature -> TemperatureC;
	QualityParamC.PH -> PHC;
	PHC.PHmeasure -> PHsensorDriver.PHreadInterface;
EOL
            ;;
        *)
            echo "Unknown value in argument: $arg1"
            exit 1
            ;;
    esac
}

generate_wirings


echo -e "\n}" >> "$MAINAPP_DIR"

