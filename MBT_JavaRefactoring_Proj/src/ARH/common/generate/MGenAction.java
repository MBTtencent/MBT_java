/*
 * FileName: MGenAction.java
 * 
 * Description: create MGenAction class
 * 
 * History:
 * 1.0 SHUYUFANG 2013-08-19 Create
 */


package ARH.common.generate;

import java.lang.reflect.*;
import java.util.*;

import ARH.framework.annotation.*;
import ARH.framework.basic.MBasicApi;
import ARH.framework.dynamic.*;
import ARH.framework.exception.MException;
import ARH.framework.file.*;
import ARH.framework.logger.MLogMag;

/**
 * this class is used to generate system action file
 * according to user action file
 * @author SHUYUFANG
 * @version 1.0
 */
public class MGenAction
{
    /**
     * constructor
     */
    private MGenAction()
    {
        
    }
    
    /**
     * generate system action file
     * @param userClass the action class name defined by users
     * @param sysPackage the package of action class defined by users
     * @param sysClass the action class name to be generated
     * @param sysActionPath the path of action java source file
     * @param stepLogPath the path of log file
     * @return system action file generated or not
     */
    public static boolean genAction(String userClass, String sysPackage, 
            String sysClass, String sysActionPath, String stepLogPath)
    {
        boolean res = false;
        
        // get method list of the action class written by user
        ArrayList<Method> methods = null;
        try
        {
            methods = MClass.getMethodsWithAnnotation(userClass, MAction.class);
        }
        catch (MException e)
        {
            MLogMag.getInstance().getLogger().error(MBasicApi.getLineInfo() + "\n" + 
                    "Cannot get methods with annotation in MGenAction." + "\n" + e.getMessage());
            return res;
        }
        
        String contents = "";
        contents += "package " + sysPackage.trim() + ";\n\n";
        contents += "import ARH.framework.file.*;\n";
        contents += "import ARH.framework.exception.*;\n";
        contents += "import ARH.framework.logger.*;\n\n";
        contents += "public class " + sysClass.trim() + "\n{\n";
        
        for (Method method : methods)
        {
            contents += "\n    public static void " + method.getName() + "(";
            String paramStr = "";
            String paramInfo = "";
            int paramCount = method.getParameterTypes().length;
            
            for (int i = 0; i < paramCount; ++i)
            {
                if (i > 0)
                {
                    paramStr += ", ";
                    paramInfo += " + \", \" + ";
                }
                paramStr += "String param" + String.valueOf(i + 1);
                paramInfo += "\"\\\"\" + " + "param" + String.valueOf(i + 1) + " + \"\\\"\"";
            }
            
            contents += paramStr + ")\n";
            contents += "    {\n";            
            contents += "        String funcInfo = \"" + method.getName() + "\";\n";            
            if (paramInfo.equals(""))
            {
                paramInfo = "\"\"";
            }
            contents += "        String paramInfo = " + paramInfo + ";\n";            
            contents += "        String mergeInfo = funcInfo + \"(\" + paramInfo + \")\";\n";    
            contents += "        try\n";
            contents += "        {\n";
            contents += "            MFile.appendFile(\"" + stepLogPath + "\", mergeInfo + \";\\n\");\n";  
            contents += "        }\n";
            contents += "        catch (MException e)\n";
            contents += "        {\n";
            contents += "            MLogMag.getInstance().getLogger().error(e.getMessage());\n";
            contents += "        }\n";
            contents += "    }\n";
        }
        
        contents += "}\n";
        
        try
        {
            // write to the specified new action file
            res = MFile.writeFile(sysActionPath, contents);
        }
        catch (MException e)
        {
            MLogMag.getInstance().getLogger().error(MBasicApi.getLineInfo() + "\n" + 
                    "Cannot generate system action file in MGenAction." + "\n" + e.getMessage());
        }
        
        return res;
    }
}