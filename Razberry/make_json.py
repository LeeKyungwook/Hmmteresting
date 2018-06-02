#-*- coding:utf-8 -*-
import json
from collections import OrderedDict

'''
file_data = OrderedDict()

file_data["Date"] = "2018.6.12"
file_data["Time"] = "12h 14m"
file_data["Name"] = "jh"
'''
dict_string = {"Date":"Monday, June 13 2018", "Time":"2018.06.13 4:30 PM", "Name":"LeeJunHo"}

#test = json.loads(dict_string)
#print(test)
print(json.dumps(dict_string, ensure_ascii=False))

with open('test.json', 'w') as make_file:
    json.dump(dict_string, make_file, ensure_ascii=False)
