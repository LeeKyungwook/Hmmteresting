#!/bin/sh
#Record 30 seconds of video

#echo ">> finish recording"
touch hooks/stop_record
sleep 1

killall picam

#echo ">> done"

