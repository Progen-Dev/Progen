import os
import platform
import time
import sys

time.sleep(3)

if platform.system() == 'Windows':
    os.system('echo It works on Windows')
else:
    os.system('echo It works on UNIX')

sys.exit(0)
