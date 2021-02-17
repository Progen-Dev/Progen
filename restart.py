import os
import platform
import time
import sys

time.sleep(3)

if platform.system() == 'Windows':
    # Command on Windows
    os.system('java -jar target/Progen-1.2-beta.jar')
else:
    # Command on UNIX (Linux, MacOS)
    os.system('java -jar target/Progen-1.2-beta.jar')

sys.exit(0)