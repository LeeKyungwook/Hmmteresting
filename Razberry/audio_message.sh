#!/bin/sh

arecord -D plughw:1,0 -d 5 test.wav

latestfile=$(ls -1t *.wav | head -n1)
echo "$latestfile"
