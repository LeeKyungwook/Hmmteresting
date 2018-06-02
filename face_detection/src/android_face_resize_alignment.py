#-*-coding:utf-8 -*-
import sys
import cv2
import dlib
import os

# if len(sys.argv) != 2:
    
#     print("Error1 : More input need")
#     exit()

predictor_path = '/home/kyungwook/kyungwook/Hmmteresting/face_detection/src/face_alignment.dat'
path = '/home/kyungwook/kyungwook/gallery'
face_file_path = '/home/kyungwook/kyungwook/Hmmteresting/WebServer/android_img'
demo_path = '/home/kyungwook/kyungwook/Hmmteresting/WebServer/image'
filenames = os.listdir(face_file_path)

for filename in filenames:
    full_filename = os.path.join(face_file_path, filename)
    name = os.path.basename(filename)

    # Load all the models we need: a detector to find the faces, a shape predictor
    # to find face landmarks so we can precisely localize the face
    detector = dlib.get_frontal_face_detector()
    sp = dlib.shape_predictor(predictor_path)

    # Load the image using Dlib
    img = dlib.load_rgb_image(full_filename)
    # resize_img = cv2.resize(img, (200,200), interpolation = cv2.INTER_AREA)

    # Ask the detector to find the bounding boxes of each face. The 1 in the
    # second argument indicates that we should upsample the image 1 time. This
    # will make everything bigger and allow us to detect more faces.
    #dets = detector(resize_img, 1)
    dets = detector(img, 1)

    num_faces = len(dets)
    if num_faces == 0:
        print("Error2 : No Face Found")
        #print("Sorry, there were no faces found in '{}'".format(face_file_path))
        exit()

    # Find the 5 face landmarks we need to do the alignment.
    faces = dlib.full_object_detections()
    for detection in dets:
        #faces.append(sp(resize_img, detection))
        faces.append(sp(img, detection))

    window = dlib.image_window()

    # Get the aligned face images
    # Optionally: 
    # images = dlib.get_face_chips(resize_img, faces, size=112, padding=0.12)
    images = dlib.get_face_chips(img, faces, size=112, padding=0.12)
    for image in images:   
        bgr_img = cv2.cvtColor(image, cv2.COLOR_RGB2BGR)
        cv2.imwrite(os.path.join(path , filename), bgr_img)
        cv2.imwrite(os.path.join(demo_path , filename), bgr_img)
        
print("Alignment & Save Completed")
