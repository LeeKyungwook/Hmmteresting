import requests
import os
import time
import picamera

'''
with picamera.PiCamera() as camera:
    camera.start_preview()
    time.sleep(5)
    camera.capture('/home/pi/test_image.jpg')
    camera.stop_preview()
'''

url = 'http://112.151.162.170:7000/file'
pwd = '/home/pi/test.wav'
files = {'media' : open(pwd, 'rb') }

res = requests.post(url, files = files)
print res.text
