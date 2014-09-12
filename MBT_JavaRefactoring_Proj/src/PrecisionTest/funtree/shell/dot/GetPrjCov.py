# Get coverage result of the project
# AUTHOR: shuyufang

import sys
import os
import commands
import urllib2

#
# Create project directory if not existed
#
def createDir(sPrjPath):
	if os.path.exists(sPrjPath):
		return
	os.makedirs(sPrjPath)

#	
# Change current directory
#
def changeDir(sPrjPath):
	os.chdir(sPrjPath)

#
# Checkout project source code from svn
#
def execSvnCheckout(sSvnUrl, sPrjPath):
	sSvnCheckOutCmd = "/opt/CollabNet_Subversion/bin/svn checkout " + str(sSvnUrl).encode("utf8") \
			+ " " + sPrjPath + " --username shuyufang --password shuyu@FANG768 --non-interactive --trust-server-cert";
	oCmdRst = commands.getstatusoutput(sSvnCheckOutCmd)
	if oCmdRst[0] != 0:
		print("FALSE")
		print(oCmdRst[1])
		sys.exit()

#	
# Run makefile to get .gcno files
#
def execMake():
	sMakeCmd = "make"
	oCmdRst = commands.getstatusoutput(sMakeCmd)
	if oCmdRst[0] != 0:
		print("FALSE")
		sys.exit()
		
#
# Run case to get .gcda files
#
def execRunCase():
	sRunCaseCmd = "./main"
	oCmdRst = commands.getstatusoutput(sRunCaseCmd)
	if oCmdRst[0] != 0:
		print("FALSE")
		sys.exit()

#
# Get .gcov files
#
def execLcov():
	sLcovCmd = "/usr/bin/lcov -c -d ./ -o res.info"
	oCmdRst = commands.getstatusoutput(sLcovCmd)
	if oCmdRst[0] != 0:
		print("FALSE1")
		print(oCmdRst[1])
		sys.exit()
	
	sGenHtmlCmd = "/usr/bin/genhtml res.info -o result" 
	oCmdRst = commands.getstatusoutput(sGenHtmlCmd)
	if oCmdRst[0] != 0:
		print("FALSE2")
		print(oCmdRst[1])
		sys.exit()

#
# Compress C++ coverage result
#
def compressResult():
	sCompressCmd = "/bin/tar -zcvf result.tar.gz result"
	oCmdRst = commands.getstatusoutput(sLcovCmd)
	if oCmdRst[0] != 0:
		print("FALSE")
		print(oCmdRst[1])
		sys.exit()

#
# Transfer result
#
def transferResult(sPrjPath):
	sTransferCmd = "/usr/local/services/itil_dt_server-0.1/bin/itil_dt_transfer f " + sPrjPath + "/result/ 10.6.223.157 /data/shuyufang/testprj/ user_00"
	oCmdRst = commands.getstatusoutput(sTransferCmd)
	if oCmdRst[0] != 0:
		print("FALSE")
		print(oCmdRst[1])
		sys.exit()

#
# Get result url
#
def getResultUrl():
	sResultUrl = "http://10.6.223.157/shuyufang/testprj/index.html"
	return sResultUrl

def main():
	if len(sys.argv) != 3:
		#print("USAGE: python cppcov.py svn_url project_path")
		print("FALSE")
		sys.exit()
		
	sSvnUrl = sys.argv[1]
	sPrjPath = sys.argv[2]

	if sSvnUrl[-1] == "/":
		sSvnUrl = sSvnUrl[: -1]

	if sPrjPath[-1] == "/":
		sPrjPath = sPrjPath[: -1]
	
	createDir(sPrjPath)
	changeDir(sPrjPath)
	execSvnCheckout(sSvnUrl, sPrjPath)
	execMake()
	execRunCase()
	execLcov()
	transferResult(sPrjPath)
	sResultUrl = getResultUrl()
	print(sResultUrl)

if __name__ == "__main__":
	main()
