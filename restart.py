import os
import platform
import time
import sys

time.sleep(3)

if platform.system() == "Linux":
    os.system("./start.sh")
    else:
    os.system("java -jar Progen.jar")

    sys.exit(0)