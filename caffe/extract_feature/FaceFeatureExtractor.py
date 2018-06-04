import os
os.environ['GLOG_minloglevel'] = '2' # suprress Caffe verbose prints
import cv2
import numpy as np
import caffe
import sys
from os import walk

class FaceFeatureExtractor():
    def __init__(self, in_shape = (112, 112, 3), mean = 127.5, raw_scale = 128.0):
        prepath    = os.path.abspath(__file__)
        prepath    = os.path.dirname(prepath)
        prototxt   = prepath + '/model/model.prototxt'
        caffemodel = prepath + '/model/model-r50-am.caffemodel'
	caffe.set_mode_gpu()
        caffe.set_device(0)

        self.blob_name = 'pre_fc1'
        self.in_w      = in_shape[0]
        self.in_h      = in_shape[1]
        self.in_c      = in_shape[2]
        self.net       = caffe.Net(prototxt, caffemodel, caffe.TEST)
        self.mean      = mean
        self.mul       = 1.0/raw_scale

        self.net.blobs['data'].reshape(1, self.in_c, self.in_h, self.in_w) #reshap input blob to match img

    def input_norm(self, blob):
        blob = cv2.resize(blob, (self.in_w, self.in_h))
        blob = cv2.cvtColor(blob, cv2.COLOR_BGR2RGB)
        blob = blob.transpose(2, 0, 1)
        blob = blob.astype(np.float32)
        blob -= self.mean
        blob *= self.mul

        return blob

    def forward(self, blob):
        self.net.blobs['data'].data[0,:,:,:] = blob
        out = self.net.forward()
        out_shape  = out[self.blob_name].shape
        len_out    = 1
        for i in out_shape:
            len_out *= i

        return out[self.blob_name].reshape(len_out)

    def output_norm(self, blob):
        dist = 0
        for i in blob:
            dist += i**2
        dist = np.sqrt(dist)
        out_norm =  blob / dist
        return out_norm

    def extract_feature(self, img):
        blob = self.input_norm(img)
        out  = self.forward(blob)
        out  = self.output_norm(out)
        return out
    
    def get_cos_dist(self, p, q):
        p = np.asarray(p).flatten()
        q = np.asarray(q).flatten()
        return np.dot(p.T,q) / (np.sqrt(np.dot(p,p.T)*np.dot(q,q.T)))
 
       
if __name__ == '__main__':
    
    #parser = argparse.ArgumentParser()
    #parser.add_argument('-img1', required=True, help='img path')
    args = sys.argv[1]

    ###################################### Make npy ########################################
    # ftr_extor = FaceFeatureExtractor()
    
    # dirname = '/home/kyungwook/kyungwook/gallery'

    # filenames = os.listdir(dirname)
    # for filename in filenames:
    #     full_filename = os.path.join(dirname, filename)
    #     npy_filename_tuple = os.path.splitext(filename)
    #     npy_filename = npy_filename_tuple[0] + '.npy' 
    #     npy_dir = dirname + '/' + npy_filename
    #     print npy_filename
    #     print full_filename
    #     print dirname
    #     img = cv2.imread(full_filename)
    #     out = ftr_extor.extract_feature(img)
    #     np.save(npy_dir, out)
    ###########################################################################################

    ###################################### Cal Dis ############################################
    ftr_extor = FaceFeatureExtractor()
    
    input_img = cv2.imread(args)
    input_img_feat = ftr_extor.extract_feature(input_img)
    
    dirname = '/home/kyungwook/kyungwook/gallery'

    current_dist = 0
    current_filename = 'None'
    filenames = os.listdir(dirname)
    for filename in filenames:
        full_filename = os.path.join(dirname, filename)
        gal_img = cv2.imread(full_filename)
        gal_img_feat = ftr_extor.extract_feature(gal_img)
        dist = ftr_extor.get_cos_dist(gal_img_feat, input_img_feat) 

        if (current_dist == 0):
            current_dist = dist
            current_filename = filename

        elif (dist > current_dist):
            current_dist = dist
            current_filename = filename

        # print dist

    # #print '--------------------------'
    if (current_dist > 0.55):
        # print current_dist
        print current_filename
    else :
        # print current_dist
        print "None Detected "
    
    #print current_dist
    #print current_filename




    

  

    
