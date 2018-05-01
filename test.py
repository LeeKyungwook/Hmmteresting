import requests
import os
import time
import picamera

with picamera.PiCamera() as camera:
    camera.start_preview()
    time.sleep(5)
    camera.capture('/home/pi/image4.jpg') #capture a picture with the name  'image4'
    camera.stop_preview()

url = 'http://112.151.162.170:7000'

pwd = '/home/pi/image4.jpg'

files = {'media' : open(pwd, 'rb')}

res = requests.post(url, files = files) #send the binary-converted image as post request 

print res.text
