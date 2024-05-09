#!/bin/bash

# Loop through all directories in the specified path
for dir in "."/*; do
    echo path
    echo $path
    echo dir
    echo $dir
    # Check if it's a directory
    if [ -d "$dir" ]; then
        # Enter the directory
        cd "$dir" || continue

        # Run your command here
        # Replace "your_command_here" with the command you want to run
        ./mvnw clean

        # Exit the directory
        cd - >/dev/null 2>&1
    fi
done

