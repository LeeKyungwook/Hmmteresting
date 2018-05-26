#!/bin/sh

#Get latest name of recording file

latestfile=$(ls -1t rec/archive/*.ts | head -n1)
echo "$latestfile"

