import sys
import cv2
import numpy as np
from sklearn import svm

sys.path.insert(0, '/home/kyungwook/kyungwook/caffe/python')
import caffe

input_image_file = '/home/kyungwook/kyungwook/test_image/aligned1.jpg'
output_file = '/home/kyungwook/kyungwook/test_image/feature.txt'

model_file = '/home/kyungwook/kyungwook/caffe/python/models/model-r50-am.caffemodel'
deploy_prototxt = '/home/kyungwook/kyungwook/caffe/python/models/model.prototxt'

net = caffe.Net(deploy_prototxt, model_file, caffe.TEST)

layer = 'stage4_unit3_bn3'
if layer not in net.blobs:
    raise TypeError("Invalid layer name: " + layer)

# imagemean_file = '/home/kyungwook/kyungwook/caffe/python/caffe/imagenet/ilsvrc_2012_mean.npy'

# transformer = caffe.io.Transformer({'data': net.blobs['data'].data.shape})
# transformer.set_mean('data', np.load(imagemean_file).mean(1).mean(1))
# transformer.set_transpose('data', (2,0,1))
# transformer.set_raw_scale('data', 255.0)

#net.blobs['data'].reshape(1,3,112,112)

# img = caffe.io.load_image(input_image_file)
# net.blobs['data'].data[...] = transformer.preprocess('data', img)
# output = net.forward()

# with open(output_file, 'w') as f:
#     np.savetxt(f, net.blobs[layer].data[0], fmt='%.4f', delimiter='\n')

# features = net.blobs[layer].data[4][:,0, 0]
# category=svm.predict(features)
# print get_category_name(category)