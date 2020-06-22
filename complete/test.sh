#!/bin/bash

set -e
EXIT_STATUS=0

start=`date +%s`

./gradlew clean test --console=plain || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

echo "Starting services"

./gradlew run -parallel --console=plain & PID1=$!

echo "Waiting 5 seconds for gateway to start"

./gradlew :acceptancetest:test --rerun-tasks --console=plain || EXIT_STATUS=$?

killall -9 java

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

end=`date +%s`
runtime=$((end-start))
echo "execution took $runtime seconds"

exit $EXIT_STATUS
