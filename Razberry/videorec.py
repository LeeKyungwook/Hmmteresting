import os
import sys
import picamera
from time import sleep

camera = picamera.PiCamera()
camera.start_recording('video.h264')
camera.start_preview()
sleep(10)
camera.stop_preview()
camera.stop_recording()
sleep(0.5)
os.system('MP4Box -add video.h264 video.mpeg')
