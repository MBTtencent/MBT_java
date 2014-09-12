# Get function names from .cpp file
# AUTHOR: shuyufang

#!/usr/bin/env python

import sys
import os

#
# Get changed function names accorfing to changed lines
#
def getDiffFunc(oDiffLines, oFuncInfo):
    oDiffFuncSet = set()
    
    for sLine in oDiffLines:
        iLine = int(sLine)
        if oFuncInfo.has_key(iLine):
            oDiffFuncSet.add(oFuncInfo[iLine])
            
    return oDiffFuncSet
    

#
# Get lines info for each function
#    
def getFuncInfo(oCppLines):
    oFuncInfo = dict()
    iLineIdx = len(oCppLines) - 1
    sFuncDecl = ""
    sFuncName = ""
    iBodyBeginLine = -1
    iBodyEndLine = -1

    while iLineIdx >= 0:
        sLine = str(oCppLines[iLineIdx])
        
        if sLine.startswith("}"):
            iBodyEndLine = iLineIdx
        
        elif sLine.startswith("{") and iBodyEndLine != -1:
            iBodyBeginLine = iLineIdx
            sFuncDecl = ""
            
        elif iBodyBeginLine != -1:
            if sLine.strip() != "" and sLine.strip() != "*/" \
                    and sLine.strip() != "}":
                sFuncDecl = sLine.strip()+ sFuncDecl
            else:
                iTmpIdx = iBodyBeginLine
                sFuncName = getFuncName(sFuncDecl)
                if sFuncName != "":
                    while iTmpIdx <= iBodyEndLine:
                        oFuncInfo[iTmpIdx + 1] = sFuncName
                        iTmpIdx += 1
                    
                iBodyBeginLine = -1
                iBodyEndLine = -1
                sFuncDecl = ""
        
        iLineIdx -= 1
        
    return oFuncInfo


#
# Get function name according to the definition line
#
def getFuncName(sFuncDecl):
    iEndPos = str(sFuncDecl).find("(")
    if iEndPos == -1:
        return ""
    
    while sFuncDecl[iEndPos - 1] == " ":
        iEndPos -= 1
    iBeginPos = str(sFuncDecl).rfind(" ", 0, iEndPos)
    sFuncName = str(sFuncDecl[iBeginPos + 1 : iEndPos]).strip()
    return sFuncName

#
# Write result to specified file
#
def writeResult(oDiffFuncSet, sOutputFile):
    oFile = open(sOutputFile, "w")
    
    for sFunc in oDiffFuncSet:
        sFunc = str(sFunc)
        if sFunc != "":
            oFile.write(sFunc + "\n")
        
    oFile.flush()
    oFile.close()
 
def main():
    if len(sys.argv) != 4:
        #print("USAGE: python CppDiffFunc: cpp_file diff_file output_file")
        print("FALSE")
        sys.exit()
    
    sCppFile = sys.argv[1]
    sDiffFile = sys.argv[2]
    sOutputFile = sys.argv[3]

    if not os.path.exists(sCppFile) or not os.path.exists(sDiffFile):
        print("FALSE")
        sys.exit()

    oCppFile = open(sCppFile, "r")
    oCppLines = oCppFile.readlines()
    oCppFile.close()
    
    oDiffFile = open(sDiffFile, "r")
    oDiffLines = oDiffFile.readlines()
    oDiffFile.close()
    
    oFuncInfo = getFuncInfo(oCppLines)
    oDiffFuncSet = getDiffFunc(oDiffLines, oFuncInfo)
    
    writeResult(oDiffFuncSet, sOutputFile)
    print("TRUE")
    
if __name__ == "__main__":
    main()