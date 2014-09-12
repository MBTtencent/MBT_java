# Get .png file according to the .dot file
# AUTHOR: shuyufang

import sys
import urllib
import urllib2

if len(sys.argv) < 7:
    #print("USAGE: python genpng.py src_dot_path src_png_path src_ip src_user dest_ip dest_user")
    print("FALSE")
    sys.exit()

TRANSFER_FILE_INTERFACE = "http://10.6.207.98/interface/deployment.php?act=1&srcip=%s&dstip=%s&srcpath=%s&dstpath=%s&srcuser=%s&dstuser=%s"
EXECUTE_CMD_INTERFACE = "http://10.6.207.98/interface/deployment.php?act=2&dstip=%s&cmd=%s&dstuser=%s&timeout=%s"

sSrcDotPath = urllib2.quote(sys.argv[1])
sSrcPngPath = urllib2.quote(sys.argv[2])
sSrcIp = urllib2.quote(sys.argv[3])
sSrcUser = urllib2.quote(sys.argv[4])
sDestIp = urllib2.quote(sys.argv[5])
sDestUser = urllib2.quote(sys.argv[6])

sDestDotPath = urllib2.quote("/data/shuyufang/mygraph.dot")
sDestPngPath = urllib2.quote("/data/shuyufang/mygraph.png")

sDestUploadCmd = urllib2.quote("python /data/shuyufang/downloadpng.py " + sDestPngPath + " " + sSrcPngPath + " " + sDestIp + " " + sDestUser + " " + sSrcIp + " " + sSrcUser)
sGenPngCmd = urllib2.quote("/usr/bin/dot " + sDestDotPath + " -Tpng -o " + sDestPngPath)
sDelDotAndPngCmd = urllib2.quote("/bin/rm /data/shuyufang/mygraph.dot /data/shuyufang/mygraph.png")
sTimeToWait = urllib2.quote("30")

sDtoolsUploadDot = TRANSFER_FILE_INTERFACE % (sSrcIp, sDestIp, sSrcDotPath, sDestDotPath, sSrcUser, sDestUser)

sDtoolsGenPng = EXECUTE_CMD_INTERFACE % (sDestIp, sGenPngCmd, sDestUser, sTimeToWait)

sDtoolsDelDotAndPng = EXECUTE_CMD_INTERFACE % (sDestIp, sDelDotAndPngCmd, sDestUser, sTimeToWait)

sDtoolsDownloadPng = EXECUTE_CMD_INTERFACE % (sDestIp, sDestUploadCmd, sDestUser, sTimeToWait)

try:
    oResult = urllib.urlopen(sDtoolsUploadDot)
    oResult = urllib.urlopen(sDtoolsGenPng)
    oResult = urllib.urlopen(sDtoolsDownloadPng)
    oResult = urllib.urlopen(sDtoolsDelDotAndPng)
    print("TRUE")
except Exception:
    print("FALSE")
