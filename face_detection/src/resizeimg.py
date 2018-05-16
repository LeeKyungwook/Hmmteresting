import os 
import cv2

def search(dirname):
    filenames = os.listdir(dirname)
    for filename in filenames:
        full_filename = os.path.join(dirname, filename)
        img = cv2.imread(full_filename)
        result = cv2.resize(img, (150,250), interpolation = cv2.INTER_AREA)
        result_filename = "/home/kyungwook/kyungwook/aligned_img/" + filename
        cv2.imwrite(result_filename, result)

search("/home/kyungwook/kyungwook/raz_img")
