import subprocess
import os

print("test!!!")

os.system('./video_message.sh')
vid_name = subprocess.check_output('./getname_test.sh', shell = True)

print("test:: " + vid_name)
