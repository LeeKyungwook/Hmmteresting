import json
import sys

reload(sys)
sys.setdefaultencoding('utf-8')

with open('test.json') as data_file:
    data = json.load(data_file)

user_id = data["user"]
temperature_num = data["weather"]["temperature"]
message_number = data["messageNum"]

print(user_id)
print(temperature_num)
print(message_number)

for index, requiredItem in enumerate(data["requiredItem"]):
    print(requiredItem)

print('------------')
for index, schedule in enumerate(data["schedule"]):
    print(schedule["title"])
    print(schedule["startDate"])
    print(schedule["startTime"])
    print(schedule["endDate"])
    print(schedule["endTime"])
    print('---------------')
