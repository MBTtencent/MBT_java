/*
 * FileName: MGenMbt.java
 * 
 * Description: create MGenMbt class
 * 
 * History:
 * 1.0 SHUYUFANG 2013-08-16 Create
 */


package ARH.common.generate;

import java.util.*;
import java.util.regex.*;

import ARH.framework.basic.MBasicApi;
import ARH.framework.exception.MException;
import ARH.framework.file.*;
import ARH.framework.logger.MLogMag;

/**
 * this class is used to generate system MBT file 
 * according to user MBT file
 * @author SHUYUFANG
 * @version 1.0
 */
public class MGenMbt
{
    /**
     * constructor
     */
    private MGenMbt()
    {
        
    }
    
    /**
     * generate system MBT file
     * @param userMbtPath the path of MBT source file
     * @param userMbtClass the name of MBT class defined by users
     * @param userActionClass the name of action class defined by users
     * @param sysMbtPath the path of generated MBT source file
     * @param sysMbtClass the name of generated MBT class
     * @param sysActionClass the name of generated action class
     * @return system MBT file generated or not
     */
    public static boolean genMbt(String userMbtPath, String userMbtClass, String userActionClass, 
            String sysMbtPath, String sysMbtClass, String sysActionClass)
    {
        boolean res = false;
        
        String sysMbtContent = "";
        String line;
        int stepIdx = 1;
        boolean isScenario = false;
        boolean isTestStep = false;
        
        // get lines of the MBT file written by user
        ArrayList<String> userMbtLines = null;// = MFile.readFileLines(userMbtPath);
        try
        {
            userMbtLines = MFile.readFileLines(userMbtPath);
        }
        catch (MException e)
        {
            MLogMag.getInstance().getLogger().error(MBasicApi.getLineInfo() + "\n" +
                    "Cannot read user mbt file file in MGenMbt." + "\n" + e.getMessage());
        }
        
        // fail to get lines
        if (null == userMbtLines)
        {
            MLogMag.getInstance().getLogger().error(MBasicApi.getLineInfo() + "\n" +
                    "Cannot read user mbt file \"" + userMbtPath + "\" in MGenMbt");
            return false;
        }
        
        // parse each line
        Pattern scenPattern = Pattern.compile("scenParam.get\\(.*?\\)");
        Pattern stepPattern = Pattern.compile("stepParam.get\\(.*?\\)");
        Matcher matcher = null;
        for (int i = 0; i < userMbtLines.size(); ++i)
        {
            line = userMbtLines.get(i);
            if (-1 != line.indexOf("public class " + userMbtClass))
            {
                line = line.replaceFirst("public class " + userMbtClass, 
                        "public class " + sysMbtClass);
            }
            else if (-1 != line.indexOf(userActionClass))
            {
                line = line.replaceAll(userActionClass, sysActionClass);
            }
            else if (0 == line.trim().indexOf("@MTestStep"))
            {
                // add StepNumber annotation to the file
                // representing the number of current TestStep method
                line = "    @MStepNumber(value = " + String.valueOf(stepIdx) 
                        + ")\n" + line;
                ++stepIdx;
                isTestStep = true;
            }
            else if (-1 != line.indexOf("@MScenario"))
            {
                isScenario = true;
            }
            else if (-1 != line.indexOf("public"))
            {
                isScenario = false;
                isTestStep = false;
            }
            if (isScenario || isTestStep)
            {
                line = line.replace("'", "\\\"");
            }
            
            matcher = scenPattern.matcher(line);
            while (matcher.find())
            {
                String paramInfo = matcher.group();
                String varName = paramInfo.substring(15, paramInfo.length() - 2);
                line = line.replace("scenParam.get(\"" + varName + "\")", 
                        "\"{MBT_SCEN_PARAM(" + varName + ")}\"");
            }
            
            matcher = stepPattern.matcher(line);
            while (matcher.find())
            {
                String paramInfo = matcher.group();
                String varName = paramInfo.substring(15, paramInfo.length() - 2);
                line = line.replace("stepParam.get(\"" + varName + "\")", 
                        "\"{MBT_STEP_PARAM(" + varName + ")}\"");
            }
            
            sysMbtContent += line + "\n";
        }
        
        try
        {
            // write content to the new MBT file
            res = MFile.writeFile(sysMbtPath, sysMbtContent);
        }
        catch (MException e)
        {
            MLogMag.getInstance().getLogger().error(MBasicApi.getLineInfo() + "\n" +
                    "Cannot generate system mbt file in MGenMbt." + "\n" + e.getMessage());
        }
        
        return res;
    }
}