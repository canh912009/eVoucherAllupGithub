#!/bin/bash

# Turn off verbose mode
set +x

# Configuration
envFilename='.env.production'
nextFolder='./.next/'

# # Echo the environment variables
# echo "--- Environment variables ---"
# # Use `env` command to directly output environment variables
# env
# echo "--- Environment variables end ---"
# echo -e "\n"

# Declare an associative array (HashMap)
declare -A configMap

# Function to apply path
apply_path() {
  # local lineNumber=0
  # Read all config file
  while IFS= read -r line; do
    # ((lineNumber++))
    # echo "Line $lineNumber: $line"

    # Ignore comments or empty lines
    if [[ "${line:0:1}" == "#" || -z "$line" || "${line}" =~ ^[[:space:]]*$ ]]; then
      continue
    fi
    
    # Split config name and value
    configName="${line%%=*}"
    configValue="${line#*=}"
    configValue="${configValue%"${configValue##*[![:space:]]}"}"  # Trim trailing whitespace
    configValue="${configValue#\"}"  # Remove leading "
    configValue="${configValue%\"}"  # Remove trailing "
    
    # Check if configName is one of the ignored variables
    if [[ "$configName" == "NEXT_PUBLIC_AUTH_TOKEN_KEY" || \
          "$configName" == "NEXT_PUBLIC_DEFAULT_LANGUAGE" || \
          "$configName" == "NEXT_PUBLIC_ENABLE_MULTI_LANG" || \
          "$configName" == "NEXT_PUBLIC_AVAILABLE_LANGUAGES" ]]; then
      continue
    fi

    # Get system env
    envValue=$(env | grep "^$configName=" | cut -d= -f2-)
    envValue="${envValue#\"}"  # Remove leading "
    envValue="${envValue%\"}"  # Remove trailing "

    # Display current line
    echo -e "\e[1;34m=====> Line =====\e[0m"
    echo -e "\e[1m$line\e[0m"

    # Display config name
    echo -e "\e[1;34m**** Config Name ****\e[0m"
    echo -e "\e[1m$configName\e[0m"

    # Display config value
    echo -e "\e[1;34m**** Config Value ****\e[0m"
    echo -e "\e[1m$configValue\e[0m"

    # Display environment value
    echo -e "\e[1;34m**** Environment Value ****\e[0m"
    echo -e "\e[1m$envValue\e[0m"

    # If envValue is empty, skip the loop iteration
    if [[ -z "$envValue" ]]; then
      # Push key-value pair into the hashmap
      configMap["$configName"]=$configValue
      continue
    fi

    # Push key-value pair into the hashmap
    configMap["$configName"]=$envValue
    
    # If both config value and env value exist and are not equal
    if [[ -n "$configValue" && -n "$envValue" && "$configValue" != "$envValue" ]]; then
      # Replace config value with env value in files
      echo "Replacing $configValue with $envValue"
      find "$nextFolder" \( -type d -name .git -prune \) -o -type f -print0 | xargs -0 sed -i "s#$configValue#$envValue#g"
    fi
    echo -e "\n"
  done < "$envFilename"
}

# Execute function
apply_path

# Starting Nextjs
echo "Starting E-Voucher NEXT_PUBLIC_REST_API_ENDPOINT_EV: ${configMap['NEXT_PUBLIC_REST_API_ENDPOINT_EV']}, NEXT_PUBLIC_FILE_API_ENDPOINT: ${configMap['NEXT_PUBLIC_FILE_API_ENDPOINT']}, NEXT_PUBLIC_FILE_API_ENDPOINT_LOCAL: ${configMap['NEXT_PUBLIC_FILE_API_ENDPOINT_LOCAL']}"

# Executing additional commands
exec "$@"
