#import the necessary packages
from picamera.array import PiRGBArray
from picamera import PiCamera
from PIL import Image
import sys
sys.path.append('/usr/local/lib/python2.7/site-packages')
import time
import cv2
import json
import requests
import os
import time
import subprocess

from os import walk

base_url = 'http://112.151.162.170:7000/'
pwd = '/home/pi/crop_image.jpg'

class RaspberryModule():
    '''
    def iniaialize_picam(self):
        camera = PiCamera()
        camera.resolution = (640, 480)
        camera.framerate = 32
        rawCapture = PiRGBArray(camera, size=(640, 480))

        cascade = cv2.CascadeClassifier("/home/pi/opencv-3.3.0/data/haarcascades/haarcascade_frontalface_alt.xml")
       
       #allow the camera to warmup
        time.sleep(0.1)

        
        #capture frames from the camera
        for frame in camera.capture_continuous(rawCapture, format="bgr", use_video_port=True):
            #grab the raw Numpy array representing the image, then initialize the timestamp
            #and occupied/unoccupied text
            img = frame.array

            gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
            gray = cv2.equalizeHist(gray)

            return gray
        
    '''    
    def detect(self, img, cascade):
        rects = cascade.detectMultiScale(img, scaleFactor=1.3, minNeighbors=4, minSize=(30,30),
                            flags=cv2.CASCADE_SCALE_IMAGE)
        if len(rects) == 0:
            return []
            #return 1
        rects[:,2:] += rects[:,:2]
        return rects

    def draw_rects(self, img, rects, color):
        for x1, y1, x2, y2 in rects:
            rect_image = cv2.rectangle(img, (x1-40, y1-40), (x2+40, y2+40), color, 2)
            print(x1, y1, x2, y2)   #rectangle coordinate
            cv2.imwrite("rect_image.jpg", rect_image)   #save a rectangle-drawn picture
            crop_image = Image.open('rect_image.jpg')
            crop_image = crop_image.crop((x1-40, y1-40, x2+40, y2+40))  #crop the image inside the rectangle
            crop_image.save('crop_image.jpg')   #save crop image
            return 1
    def record_vid():
        # video 찍고url로 비디오 이름 보냄
        os.system('./video_message.sh')
        vid_name = subprocess.check_output('./getname_test.sh', shell = True)
        return vid_name
    def record_aud():
        #audio 찍고 url로 비디오 이름 보냄
        aud_name = subprocess.check_output('./audio_message.sh', shell = True)
        return aud_name

class RaspberryUI():

    def initalize_UI():

    def arrange_UI(self, json):

    def play_vid_on_UI():


if __name__ == '__main__':

    raz_module = RaspberryModule()
    raz_ui = RaspberryUI()

    #Camera setting
    camera = PiCamera()
    camera.resolution = (640, 480)
    camera.framerate = 32
    rawCapture = PiRGBArray(camera, size=(640, 480))
    cascade = cv2.CascadeClassifier("/home/pi/opencv-3.3.0/data/haarcascades/haarcascade_frontalface_alt.xml")
    
    time.sleep(0.1)

    while True:

        raz_ui.initalize_UI()
        
	#capture frames from the camera
        for frame in camera.capture_continuous(rawCapture, format="bgr", use_video_port=True):
            #grab the raw Numpy array representing the image, then initialize the timestamp
            #and occupied/unoccupied text
            img = frame.array

            gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
            gray = cv2.equalizeHist(gray)

        #if (raz_module.detect(img, cascade) != 1):
            rects = raz_module.detect(gray, cascade)
            vis = img.copy()
        
            if raz_module.draw_rects(vis, rects, (0, 255, 0)) == 1:
                break
            
            #show the frame
            cv2.imshow("Frame", vis)
            rawCapture.truncate(0)

        files = {'media' : open(pwd, 'rb') }             #저장된 이미지 불러와야함
        image_url = base_url + 'file'                    #보내려는 url 주소
        res = requests.post(image_url, files = files)

        raz_ui.arrange_UI(res.json)

        while True:

            video_url = base_url + 'video'
            res = requests.get(url = video_url)         
            #get 방식으로 서버에 계속하여 요청을 보내고 응답을 기다림

            ###### reponse 값은 임의 값으로 함.
            if (res.text == 1):                         
                #response가 1이 오면 비디오 찍고 비디오 이름을 서버에 request로 보냄
                video_name = raz_module.record_vid()    
                #record_vid 실행 시, 영상을 저장하고 해당 파일의 전체 경로(video_name)를 보내줘야함
                video_save_url = video_url + '/vsave'
                res = requests.post(video_save_url, data = video_name)
            elif (res.text == 2):
                #audio 녹음 실행 후 녹음 파일 이름 전송
                audio_name = raz_module.record_aud()
                audio_save_url = audio_url + '/asave'
                res = requests.post(audio_save_url, data = audio_name)
            elif(res.text == '영상파일이름'):            
                #영상 파일 이름에 대한 response가 오면 video 실행.
                    
            elif(res.text == 3):                        
                #response가 3이 오면 내부 while 문을 빠져나가 외부 while문으로 감(스마트 미러 종료시 해당 response)
                break()

            else:                                        
                #다른 response가 올 경우 내부 while continue
                time.sleep(2)
                continue
        '''
        else:
            time.sleep(2)
            continue
        '''
