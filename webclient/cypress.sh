while ! netstat -tna | grep 'LISTEN\>' | grep -q ':8080\>'; do
  sleep 10
done
echo "Webclient is up"
npm run cypress