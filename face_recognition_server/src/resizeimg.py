import os 
import cv2

def search(dirname):
    filenames = os.listdir(dirname)
    for filename in filenames:
        full_filename = os.path.join(dirname, filename)
        img = cv2.imread(full_filename)
        result = cv2.resize(img, (200,200), interpolation = cv2.INTER_AREA)
        result_filename = "/home/kyungwook/kyungwook/gallery/" + filename
        cv2.imwrite(result_filename, result)

search("/home/kyungwook/kyungwook/android_img")

# img = cv2.imread("/home/kyungwook/kyungwook/gallery/yn2.jpg")

# result = cv2.resize(img, (200,200), interpolation = cv2.INTER_AREA)
# cv2.imwrite("/home/kyungwook/kyungwook/gallery/yn2_resize.jpg", result)