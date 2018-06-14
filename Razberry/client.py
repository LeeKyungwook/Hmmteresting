#-*-coding:utf-8
#import the necessary packages
from picamera.array import PiRGBArray
from picamera import PiCamera
from threading import Timer

import sys
sys.path.append('/usr/local/lib/python2.7/site-packages')
import time
import cv2
import json
import requests
import os
import time
import subprocess
import sys

reload(sys)
sys.setdefaultencoding('utf-8')

from os import walk

base_url = 'http://112.151.162.170:7000/init'
raz_url = 'http://112.151.162.170:7000/raz_client'
pwd = '/home/pi/Hmmteresting/Razberry/test_image.jpg'
cascade = cv2.CascadeClassifier("/home/pi/opencv-3.3.0/data/haarcascades/haarcascade_frontalface_alt.xml")

class RaspberryModule():
    

    def start_camera(self, camera):
        #camera = PiCamera()
        camera.resolution = (612, 816)
        camera.framerate = 32
        #rawCapture = PiRGBArray(camera, size=(612, 816))
        #cascade = cv2.CascadeClassifier("/home/pi/opencv-3.3.0/data/haarcascades/haarcascade_frontalface_alt.xml")
    
        time.sleep(0.1)

    def detect(self, img, cascade):
        rects = cascade.detectMultiScale(img, scaleFactor=1.3, minNeighbors=4, minSize=(30,30),
                            flags=cv2.CASCADE_SCALE_IMAGE)
        if len(rects) == 0:
            return []

        rects[:,2:] += rects[:,:2]
        return rects

    def draw_rects(self, img, rects, color):
        for x1, y1, x2, y2 in rects:
            cv2.imwrite("test_image.jpg", img)
            rect_image = cv2.rectangle(img, (x1-40, y1-40), (x2+40, y2+40), color, 2)
            print(x1, y1, x2, y2)   #rectangle coordinate
            return 1

    def start_record_vid(self):
        #start video recording
        os.system('./video_start.sh')

    def stop_record_vid(self):
        #stop video recording
        os.system('./video_stop.sh')
        vid_name = subprocess.check_output('./getname_test.sh', shell = True)
        return vid_name

    def record_aud(self):
        #audio recording and return file name
        aud_name = subprocess.check_output('./audio_message.sh', shell = True)
        return aud_name

if __name__ == '__main__':
    
    raz_module = RaspberryModule()
    camera = PiCamera()

    raz_module.start_camera(camera)
    rawCapture = PiRGBArray(camera, size=(612, 816))

    while True:
        os.system('python init.py &')
        time.sleep(3)
        os.system('pkill -9 -ef schedule.py')

	#capture frames from the camera
        for frame in camera.capture_continuous(rawCapture, format="bgr", use_video_port=True):
            #grab the raw Numpy array representing the image, then initialize the timestamp
            #and occupied/unoccupied text
            img = frame.array

            gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
            gray = cv2.equalizeHist(gray)

            rects = raz_module.detect(gray, cascade)
            vis = img.copy()
        
            if raz_module.draw_rects(vis, rects, (0, 255, 0)) == 1:
                
                files = {'media' : open(pwd, 'rb') }
                image_url = base_url
                res = requests.post(image_url, files = files)
	        
                if res.text == 'cannot find face':
                    print res.text
                elif res.text == 'who are you?':
                    print res.text
                elif res.text == 'Too Many Faces':
                    print res.text
                else :
                    print("detected!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                    print res.text
                    
                    #make json file named test.json
                    #json_data = ast.literal_eval(res.text)
                    json_data = json.loads(res.text)
                    with open('test.json', 'w') as make_file:
                        json.dump(json_data, make_file, ensure_ascii=False)

                    break
                    
            #show the frame
            #cv2.imshow("Frame", vis)
            rawCapture.truncate(0)

        rawCapture.truncate(0)
        camera.close()
        
        #change the color of monitor when server responsed
        os.system('python schedule.py &')
        time.sleep(4)
        subprocess.call(["mplayer", "-volume", "150", "../../tts/hello.mp3"])
        os.system('pkill -9 -ef init.py')

        now = time.localtime()
        now_min = now.tm_min
        now_sec = now.tm_sec
        now_time = now_min * 60 + now_sec
        future = now_time + 180

        '''
        t = Timer(60.0, raz_module.stop_UI)
        t.start()
        '''

        while True:

            now = time.localtime()
            if (now.tm_min * 60 + now.tm_sec) > future:
                print('natural exit')

                #Camera setting
                camera = PiCamera()
                raz_module.start_camera(camera)
                rawCapture = PiRGBArray(camera, size=(612, 816))

                time.sleep(0.1)
                break

            try: 
                print ("Sending.............................") 
                res = requests.post(raz_url)
            #get request and wait for returing value
            except requests.exceptions.ConnectionError:
                print("ConnectionError.............")
                time.sleep(5)
                continue
            
            input_data = json.loads(res.text)
            with open('test.json', 'w') as make_file:
                json.dump(input_data, make_file, ensure_ascii=False)

            client_Param = input_data["client_Param"]
            print client_Param
            
            if (client_Param == "1"):
                #record and send video name
                subprocess.call(["mplayer", "-volume", "150", "../../tts/start_record_video.mp3"])

                raz_module.start_record_vid()
                #time.sleep(5)
                while True:
                    try: 
                        res = requests.post(raz_url + '/ILuvU')
                        #get request and wait for returing value
                    except requests.exceptions.ConnectionError:
                        print("ConnectionError.............")
                        time.sleep(5)
                        continue
                    
                    input_data = json.loads(res.text)
                    heart_signal = input_data["luvU"]
                    if (heart_signal == 1):
                        video_name = raz_module.stop_record_vid()
                        camera.close()
                        print('recording stop...................')
                        break
                    else:
                        continue
                
                '''
                now = time.localtime()
                now_min = now.tm_min
                now_sec = now.tm_sec
                now_time = now_min * 60 + now_sec
                
                future = now_time + 60

                while True:
                    if (now.tm_min * 60 + now.tm_sec) > future:
                        video_name = raz_module.stop_record_vid()
                        break
                '''
                subprocess.call(["mplayer", "-volume", "150", "../../tts/stop_record_video.mp3"])        
                video_save_url = base_url + '/sendMessage'
                data = {"title" : video_name}
                res = requests.post(video_save_url, data = data)
                continue
            #elif (client_Param == 2):
            #    #record and send audio name
            #    #turn on the video
            #    
            #    audio_name = raz_module.record_aud()
            #    audio_save_url = base_url + '/sendAudioMessage'
            #    res = requests.post(audio_save_url, data = audio_name) 
            elif (client_Param == "2"):
                #turn on the video
                messageTitle = input_data["messageTitle"]

                os.system('python schedule.py &')
                subprocess.call(["mplayer", "-volume", "150", "../../tts/showvideo.mp3"])

                print('video starting..........')

                os.system('omxplayer -o local -b --vol 1500 ./rec/archive/' + messageTitle)
                os.system('find -name "' + messageTitle + '" -delete')
                continue
            elif (client_Param == "3"):
                #break inner loop and go to outer loop
                subprocess.call(["mplayer", "-volume", "150", "../../tts/goodbye.mp3"])


                #Camera setting
                camera = PiCamera()
                raz_module.start_camera(camera)
                rawCapture = PiRGBArray(camera, size=(612, 816))
                break
            elif (client_Param == "5"):
                subprocess.call(["mplayer", "-volume", "150", "../../tts/nofound.mp3"])
                continue
            else :
                #innser loop
                print("normal condition")
                time.sleep(3)
                continue
