#!/bin/sh

timestamp=$(date +%Y%m%d%H%M)
arecord -D plughw:1,0 -d 5 $timestamp.wav
echo "$timestamp"

latestfile=$(ls -1t *.wav | head -n1)
echo "$latestfile"
