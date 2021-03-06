import numpy as np
import matplotlib.pyplot as plt
import os 
import cv2
import json
from collections import Counter


DESCDIR = "E:/\school/tensorflowprc/moleDataSet/Data/Descriptions/"
DESCIMG = "E:/\school/tensorflowprc/moleDataSet/Data/Images/"

def open_json_file(file_name):
    with open(file_name) as f:
        data = json.load(f)
        #print(data)
        return data


labels = []
for file in os.listdir(DESCDIR):
    data = open_json_file(os.path.join(DESCDIR,file))
    if 'meta' in data & 'clinical' in data['meta']:
        if 'benign_malignant' in data['meta']['clinical']:
            if data['meta']['clinical']['benign_malignant'] == 'benign':
                labels.append(0)
            else:
                labels.append(1)


        else:
            os.remove(os.path.join(DESCDIR,file))
            os.remove(os.path.join(DESCIMG,file + '.jpeg'))
            continue

print(Counter(labels).keys())
print(Counter(labels).values())
print(len(labels))
