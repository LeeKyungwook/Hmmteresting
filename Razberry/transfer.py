import requests
import os
import time
from picamera import PiCamera

camera = PiCamera()

camera.resolution = (612, 816)
camera.brightness = 60

camera.start_preview()
time.sleep(3)
camera.capture('/home/pi/test_image.jpg')
camera.stop_preview()

'''
url = 'http://112.151.162.170:7000/init'
pwd = '/home/pi/Hmmteresting/Razberry/crop_image.jpg'
files = {'media' : open(pwd, 'rb') }

res = requests.post(url, files = files)
print res.text
'''
