COMPONENT=MainApp

# Include directories for MSP430 USART and printf library
CFLAGS += -I$(TOSDIR)/chips/msp430/usart
CFLAGS += -I$(TOSDIR)/lib/printf

# Define preprocessor macros
CFLAGS += -DACK_DIM=2
CFLAGS += -DSOP=60
CFLAGS += -DEOP=62
CFLAGS += -DSOP_POS=0
CFLAGS += -DSAMPLING_TIME=1024
CFLAGS += -DUARTdata_DIM=9
CFLAGS += -DNR_QUALITY_PARAMS=3
CFLAGS += -DTEMPERATURE_POS=0
CFLAGS += -DTDS_POS=1
CFLAGS += -DPH_POS=2
CFLAGS += -DEOP_POS=7
# Include directories for project components
CFLAGS += -I./main
CFLAGS += -I./analysis
CFLAGS += -I./analysis/water
CFLAGS += -I./analysis/water/quality.param
CFLAGS += -I./analysis/water/quality.param/temperature
CFLAGS += -I./analysis/water/quality.param/temperature/adc
CFLAGS += -I./analysis/water/quality.param/tds
CFLAGS += -I./analysis/water/quality.param/tds/adc
CFLAGS += -I./analysis/water/quality.param/ph
CFLAGS += -I./analysis/water/quality.param/ph/adc
CFLAGS += -I./driver

include $(MAKERULES)
