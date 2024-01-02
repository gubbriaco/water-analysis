#!/bin/bash

if [ "$#" -eq 0 ]; then
  echo "Usage: $0 <argument>"
  exit 1
fi

# Access the first argument using $1
arg1="$1"

source ./configMakefile.sh "$arg1"

source ./configMainApp.sh "$arg1"

source ./configMain.sh "$arg1"

source ./configUARTdriver.sh "$arg1"

source ./configWaterAnalysis.sh "$arg1"

source ./configQualityParamC.sh "$arg1"

