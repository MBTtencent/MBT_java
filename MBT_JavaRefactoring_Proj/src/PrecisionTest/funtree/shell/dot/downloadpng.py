import sys
import urllib
import urllib2

if len(sys.argv) < 7:
    #print("USAGE: python downloadpng.py src_png_path dest_png_path src_ip src_user dest_ip dest_user")
    print("FALSE")
    sys.exit()

TRANSFER_FILE_INTERFACE = "http://10.6.207.98/interface/deployment.php?act=1&srcip=%s&dstip=%s&srcpath=%s&dstpath=%s&srcuser=%s&dstuser=%s"

sSrcPngPath = urllib2.quote(sys.argv[1])
sDestPngPath = urllib2.quote(sys.argv[2])
sSrcIp = urllib2.quote(sys.argv[3])
sSrcUser = urllib2.quote(sys.argv[4])
sDestIp = urllib2.quote(sys.argv[5])
sDestUser = urllib2.quote(sys.argv[6])

sDtoolsUploadDot = TRANSFER_FILE_INTERFACE % (sSrcIp, sDestIp, sSrcPngPath, sDestPngPath, sSrcUser, sDestUser)

try:
    oResult = urllib.urlopen(sDtoolsUploadDot)
    print("TRUE")
except Exception:
    print("FALSE")