#-*- coding:utf-8 -*-
import json
from collections import OrderedDict

'''
file_data = OrderedDict()

file_data["Date"] = "2018.6.12"
file_data["Time"] = "12h 14m"
file_data["Name"] = "jh"
'''
dict_string = {"name":"이준호","schedule":[{"title":"점심약속","user":"123","startDate":"2018-06-04","startTime":"12:00","endDate":"2018-06-04","endTime":"13:00","reqItmes":"","isBroadcast":"1"},{"title":"캡스톤 수업","user":"123","startDate":"2018-06-04","startTime":"16:30","endDate":"2018-06-04","endTime":"21:00","reqItmes":"노트북","isBroadcast":"1"}],"requiredItem":["노트북","과자"],"messageNum":1}

#test = json.loads(dict_string)
#print(test)
print(json.dumps(dict_string, ensure_ascii=False))

with open('test.json', 'w') as make_file:
    json.dump(dict_string, make_file, ensure_ascii=False)
