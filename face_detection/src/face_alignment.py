import sys
import cv2
import dlib
import os

if len(sys.argv) != 3:
    print(
        "Call this program like this:\n"
        "   ./face_alignment.py shape_predictor_5_face_landmarks.dat ../examples/faces/bald_guys.jpg\n"
        "You can download a trained facial shape predictor from:\n"
        "    http://dlib.net/files/shape_predictor_5_face_landmarks.dat.bz2\n")
    exit()

predictor_path = sys.argv[1]
face_file_path = sys.argv[2]
path = '/home/kyungwook/kyungwook/aligned_img'

# Load all the models we need: a detector to find the faces, a shape predictor
# to find face landmarks so we can precisely localize the face
detector = dlib.get_frontal_face_detector()
sp = dlib.shape_predictor(predictor_path)

# Load the image using Dlib
img = dlib.load_rgb_image(face_file_path)

# Ask the detector to find the bounding boxes of each face. The 1 in the
# second argument indicates that we should upsample the image 1 time. This
# will make everything bigger and allow us to detect more faces.
dets = detector(img, 1)

num_faces = len(dets)
if num_faces == 0:
    print("Sorry, there were no faces found in '{}'".format(face_file_path))
    exit()

# Find the 5 face landmarks we need to do the alignment.
faces = dlib.full_object_detections()
for detection in dets:
    faces.append(sp(img, detection))

window = dlib.image_window()

# Get the aligned face images
# Optionally: 
images = dlib.get_face_chips(img, faces, size=112, padding=0.12)
for image in images:
    #window.set_image(image)
    #dlib.hit_enter_to_continue()
    #cv2.imwrite("saved.jpg", image)
    bgr_img = cv2.cvtColor(image, cv2.COLOR_RGB2BGR)
    cv2.imwrite(os.path.join(path , 'input12.jpg'), bgr_img)
    print("Alignment Completed")

# It is also possible to get a single chip
# image = dlib.get_face_chip(img, faces[0])
# window.set_image(image)
# dlib.hit_enter_to_continue()
