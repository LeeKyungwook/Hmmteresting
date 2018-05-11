#!/bin/sh
#Record 30 seconds of video

echo ">> starting picam"
./picam --alsadev hw:1,0 &
sleep 3

echo ">> begin recording"
touch hooks/start_record
sleep 30

echo ">> finish recording"
touch hooks/stop_record
sleep 1
killall picam

echo ">> done"
