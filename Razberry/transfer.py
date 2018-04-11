import requests
import os
import time
import picamera

with picamera.PiCamera() as camera:
    camera.start_preview()
    time.sleep(5)
    camera.captue('/home/pi/image4.jpg')
    camera.stop_preview()

url = 'http://112.151.162.170:7000'
pwd = '/home/pi/image4.jpg'
files = {'media' : open(pwd, 'rb') }

res = requests.post(url, files = files)

print res.text
