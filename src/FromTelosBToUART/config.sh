#!/bin/bash

if [ "$#" -eq 0 ]; then
  echo "Usage: $0 <argument>"
  exit 1
fi

# Access the first argument using $1
arg1="$1"

MAKEFILE_DIR="./"

export TOSDIR=/opt/tinyos-main/support/make/Makerules
export MAKERULES=/opt/tinyos-main/support/make/Makerules

# Create the Makefile in the specified directory
echo "COMPONENT=MainApp" > "$MAKEFILE_DIR/Makefile"
echo -e "\n# Include directories for MSP430 USART and printf library" >> "$MAKEFILE_DIR/Makefile"
echo "CFLAGS += -I\$(TOSDIR)/chips/msp430/usart" >> "$MAKEFILE_DIR/Makefile"
echo "CFLAGS += -I\$(TOSDIR)/lib/printf" >> "$MAKEFILE_DIR/Makefile"

echo -e "\n# Define preprocessor macros" >> "$MAKEFILE_DIR/Makefile"
echo "CFLAGS += -DACK_DIM=2"  >> "$MAKEFILE_DIR/Makefile"
echo "CFLAGS += -DUARTdata_DIM=9"  >> "$MAKEFILE_DIR/Makefile"
echo "CFLAGS += -DNR_QUALITY_PARAMS=3"  >> "$MAKEFILE_DIR/Makefile"
echo "CFLAGS += -DTEMPERATURE_POS=0"  >> "$MAKEFILE_DIR/Makefile"
echo "CFLAGS += -DTDS_POS=1"  >> "$MAKEFILE_DIR/Makefile"
echo "CFLAGS += -DPH_POS=2"  >> "$MAKEFILE_DIR/Makefile"
echo "CFLAGS += -DSOP=60"  >> "$MAKEFILE_DIR/Makefile"
echo "CFLAGS += -DEOP=62"  >> "$MAKEFILE_DIR/Makefile"
echo "CFLAGS += -DSOP_POS=0"  >> "$MAKEFILE_DIR/Makefile"
echo "CFLAGS += -DEOP_POS=7"  >> "$MAKEFILE_DIR/Makefile"
echo "CFLAGS += -DSAMPLING_TIME=1024"  >> "$MAKEFILE_DIR/Makefile"

# Creates the content of the Makefile dynamically based on the read value
case $arg1 in
  "home")
    echo -e "\n# Include directories for project components" >> "$MAKEFILE_DIR/Makefile"
    echo "CFLAGS += -I./main"  >> "$MAKEFILE_DIR/Makefile"
    echo "CFLAGS += -I./analysis"  >> "$MAKEFILE_DIR/Makefile"
    echo "CFLAGS += -I./analysis/water"  >> "$MAKEFILE_DIR/Makefile"
    echo "CFLAGS += -I./analysis/water/quality.param"  >> "$MAKEFILE_DIR/Makefile"
    echo "CFLAGS += -I./analysis/water/quality.param/temperature"  >> "$MAKEFILE_DIR/Makefile"
    echo "CFLAGS += -I./analysis/water/quality.param/tds"  >> "$MAKEFILE_DIR/Makefile"
    echo "CFLAGS += -I./analysis/water/quality.param/tds/adc"  >> "$MAKEFILE_DIR/Makefile"
    echo "CFLAGS += -I./analysis/water/quality.param/ph"  >> "$MAKEFILE_DIR/Makefile"
    echo "CFLAGS += -I./driver"  >> "$MAKEFILE_DIR/Makefile"
    ;;
  "sea")
    echo -e "\n# Include directories for project components" >> "$MAKEFILE_DIR/Makefile"
    echo "CFLAGS += -I./main"  >> "$MAKEFILE_DIR/Makefile"
    echo "CFLAGS += -I./analysis"  >> "$MAKEFILE_DIR/Makefile"
    echo "CFLAGS += -I./analysis/water"  >> "$MAKEFILE_DIR/Makefile"
    echo "CFLAGS += -I./analysis/water/quality.param"  >> "$MAKEFILE_DIR/Makefile"
    echo "CFLAGS += -I./analysis/water/quality.param/temperature"  >> "$MAKEFILE_DIR/Makefile"
    echo "CFLAGS += -I./analysis/water/quality.param/tds"  >> "$MAKEFILE_DIR/Makefile"
    echo "CFLAGS += -I./analysis/water/quality.param/tds/adc"  >> "$MAKEFILE_DIR/Makefile"
    echo "CFLAGS += -I./analysis/water/quality.param/ph"  >> "$MAKEFILE_DIR/Makefile"
    echo "CFLAGS += -I./driver"  >> "$MAKEFILE_DIR/Makefile"
    ;;
  "pool")
    echo -e "\n# Include directories for project components" >> "$MAKEFILE_DIR/Makefile"
    echo "CFLAGS += -I./main"  >> "$MAKEFILE_DIR/Makefile"
    echo "CFLAGS += -I./analysis"  >> "$MAKEFILE_DIR/Makefile"
    echo "CFLAGS += -I./analysis/water"  >> "$MAKEFILE_DIR/Makefile"
    echo "CFLAGS += -I./analysis/water/quality.param"  >> "$MAKEFILE_DIR/Makefile"
    echo "CFLAGS += -I./analysis/water/quality.param/temperature"  >> "$MAKEFILE_DIR/Makefile"
    echo "CFLAGS += -I./analysis/water/quality.param/tds"  >> "$MAKEFILE_DIR/Makefile"
    echo "CFLAGS += -I./analysis/water/quality.param/tds/adc"  >> "$MAKEFILE_DIR/Makefile"
    echo "CFLAGS += -I./analysis/water/quality.param/ph"  >> "$MAKEFILE_DIR/Makefile"
    echo "CFLAGS += -I./driver"  >> "$MAKEFILE_DIR/Makefile"
    ;;
  *)
    echo "Unknown value in argument: $arg1"
    exit 1
    ;;
esac

echo -e "\ninclude \$(MAKERULES)" >> "$MAKEFILE_DIR/Makefile"

