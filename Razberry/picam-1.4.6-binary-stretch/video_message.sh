#!/bin/sh
#Record 30 seconds of video

echo ">> starting picam"
./picam --alsadev hw:1,0 &
sleep 3

latestfile=$(ls -1t rec/archive/*.ts | head -n1)
echo ">> $latestfile"

echo ">> begin recording"
#echo "filename=test4.ts" > hooks/start_record
touch hooks/start_record
sleep 5

echo ">> finish recording"
touch hooks/stop_record
sleep 1

latestfile=$(ls -1t rec/archive/*.ts | head -n1)
echo ">> $latestfile"
killall picam

echo ">> done"
