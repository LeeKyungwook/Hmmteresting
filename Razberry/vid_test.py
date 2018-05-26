import subprocess
import os
print("?????")
#vid_name = subprocess.check_output('./picam-1.4.6-binary-stretch/video_message.sh', shell = True)
os.system('./picam-1.4.6-binary-stretch/video_message.sh')
os.system('latestfile=$(ls -1t ./picam-1.4.6-binary-stretch/rec/archive/*.ts | head -n1)')
vid_name = subprocess.check_output('echo "$latestfile"', shell = True)

print("test:: " + vid_name)
