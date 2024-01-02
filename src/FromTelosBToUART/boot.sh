if [ "$#" -eq 0 ]; then
  echo "Usage: $0 <argument>"
  exit 1
fi

# Access the first argument using $1
arg1="$1"

source ./config.sh "$arg1"

