import subprocess

aud_name = subprocess.check_output('./audio_message.sh', shell = True)

print("test::: " + aud_name)
