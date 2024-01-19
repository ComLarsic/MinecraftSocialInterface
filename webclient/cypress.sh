# Create the fabric/run directory if it doesn't exist
mkdir -p fabric/run
# Add the eula file
echo "eula=true" > ../fabric/run/eula.txt
# Start the server
./gradlew fabric:runServer > LOG.txt &
# Wait for the server to start
echo "Waiting for server"
while true 
do
    sleep 1
    # Print the new line added to the log file
    tail -n 1 LOG.txt

    # Check if the server is done by accessing the log
    if grep -q "Done!" LOG.txt
    then
        break
    fi
done
# Start the cypress tests
echo "Webclient is up"
cd webclient
npm run cypress