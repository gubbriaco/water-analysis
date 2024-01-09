#!/bin/bash
set +x

if [ "$#" -eq 0 ]; then
  echo "Usage: $0 <argument>"
  exit 1
fi

# Access the first argument using $1
arg1="$1"

source ./configMakefile.sh "$arg1"
echo "./configMakefile.sh arg=$arg1"
sleep 0.1

source ./configMainApp.sh "$arg1"
echo "./configMainApp.sh arg=$arg1"
sleep 0.1

source ./configMain.sh "$arg1"
echo "./configMain.sh arg=$arg1"
sleep 0.1

source ./configUARTdriver.sh "$arg1"
echo "./configUARTdriver.sh arg=$arg1"
sleep 0.1

source ./configWaterAnalysis.sh "$arg1"
echo "./configWaterAnalysis.sh arg=$arg1"
sleep 0.1

source ./configQualityParamC.sh "$arg1"
echo "./configQualityParamC.sh arg=$arg1"
sleep 0.1

