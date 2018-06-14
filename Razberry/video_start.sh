#!/bin/sh
#Record 30 seconds of video

./picam-1.4.6-binary-stretch/picam --alsadev hw:1,0 &
sleep 2

#echo ">> starting picam"
touch hooks/start_record
