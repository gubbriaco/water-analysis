#!/bin/bash

if [ "$#" -eq 0 ]; then
  echo "Usage: $0 <argument>"
  exit 1
fi

# Access the first argument using $1
arg1="$1"

MAKEFILE_DIR="./Makefile"

export TOSDIR=/opt/tinyos-main/support/make/Makerules
export MAKERULES=/opt/tinyos-main/support/make/Makerules

cat <<EOL > "$MAKEFILE_DIR"
COMPONENT=MainApp

# Include directories for MSP430 USART and printf library
CFLAGS += -I\$(TOSDIR)/chips/msp430/usart
CFLAGS += -I\$(TOSDIR)/lib/printf

# Define preprocessor macros
CFLAGS += -DACK_DIM=2
CFLAGS += -DSOP=60
CFLAGS += -DEOP=62
CFLAGS += -DSOP_POS=0
CFLAGS += -DSAMPLING_TIME=1024
EOL


generate_constants() {
    case $arg1 in
        "home")
            cat <<EOL >> "$MAKEFILE_DIR"
CFLAGS += -DUARTdata_DIM=9
CFLAGS += -DNR_QUALITY_PARAMS=3
CFLAGS += -DTEMPERATURE_POS=0
CFLAGS += -DTDS_POS=1
CFLAGS += -DPH_POS=2
CFLAGS += -DEOP_POS=7
EOL
            ;;
        "sea")
            cat <<EOL >> "$MAKEFILE_DIR"
CFLAGS += -DUARTdata_DIM=9
CFLAGS += -DNR_QUALITY_PARAMS=3
CFLAGS += -DTEMPERATURE_POS=0
CFLAGS += -DTDS_POS=1
CFLAGS += -DPH_POS=2
CFLAGS += -DEOP_POS=7
EOL
            ;;
        "pool")
            cat <<EOL >> "$MAKEFILE_DIR"
CFLAGS += -DUARTdata_DIM=7
CFLAGS += -DNR_QUALITY_PARAMS=2
CFLAGS += -DTEMPERATURE_POS=0
CFLAGS += -DPH_POS=1
CFLAGS += -DEOP_POS=5
EOL
            ;;
        *)
            echo "Unknown value in argument: $arg1"
            exit 1
            ;;
    esac
}

generate_constants


cat <<EOL >> "$MAKEFILE_DIR"
# Include directories for project components
CFLAGS += -I./main
CFLAGS += -I./analysis
CFLAGS += -I./analysis/water
CFLAGS += -I./analysis/water/quality.param
EOL


generate_directories() {
    case $arg1 in
        "home")
            cat <<EOL >> "$MAKEFILE_DIR"
CFLAGS += -I./analysis/water/quality.param/temperature
CFLAGS += -I./analysis/water/quality.param/temperature/adc
CFLAGS += -I./analysis/water/quality.param/tds
CFLAGS += -I./analysis/water/quality.param/tds/adc
CFLAGS += -I./analysis/water/quality.param/ph
CFLAGS += -I./analysis/water/quality.param/ph/adc
CFLAGS += -I./driver
EOL
            ;;
        "sea")
            cat <<EOL >> "$MAKEFILE_DIR"
CFLAGS += -I./analysis/water/quality.param/temperature
CFLAGS += -I./analysis/water/quality.param/temperature/adc
CFLAGS += -I./analysis/water/quality.param/tds
CFLAGS += -I./analysis/water/quality.param/tds/adc
CFLAGS += -I./analysis/water/quality.param/ph
CFLAGS += -I./analysis/water/quality.param/ph/adc
CFLAGS += -I./driver
EOL
            ;;
        "pool")
            cat <<EOL >> "$MAKEFILE_DIR"
CFLAGS += -I./analysis/water/quality.param/temperature
CFLAGS += -I./analysis/water/quality.param/temperature/adc
CFLAGS += -I./analysis/water/quality.param/ph
CFLAGS += -I./analysis/water/quality.param/ph/adc
CFLAGS += -I./driver
EOL
            ;;
        *)
            echo "Unknown value in argument: $arg1"
            exit 1
            ;;
    esac
}

generate_directories

echo -e "\ninclude \$(MAKERULES)" >> "$MAKEFILE_DIR"

