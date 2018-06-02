import os
import time

os.system('python init.py &')
time.sleep(3)
os.system('pkill -9 -ef schedule.py')
print('this is a  foreground file')
print('open schedule UI file')
os.system('python schedule.py &')
time.sleep(3)
os.system('pkill -9 -ef init.py')
print('terminate scheduleUI file')
time.sleep(3)
os.system('python init.py &')
time.sleep(3)
os.system('pkill -9 -ef schedule.py')
time.sleep(3)
os.system('pkill -9 -ef init.py')
