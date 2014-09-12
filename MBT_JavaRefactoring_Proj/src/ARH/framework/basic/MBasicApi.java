/*
 * FileName: MBasicApi.java
 * 
 * Description: create MBasicApi class
 * 
 * History: 
 * 1.0 MICKCHEN 2013-08-20 Create
 */


package ARH.framework.basic;

import java.text.SimpleDateFormat;
import java.util.*;

import ARH.framework.file.*;

/**
 * The MBasicApi class define some common API
 * @author MICKCHEN
 * @version 1.0
 */
public class MBasicApi
{       
    /**
     * Check the String is a list or not, example :[1,2,3]
     * @param s the string would to be checked
     * @return true if is like [1,2,3],otherwise false
     */
    public static boolean isList(String s)
    {
        s = s.trim();
        if (s.startsWith("[") && s.endsWith("]"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * Get the sub value of the list,which like "[1,2,3]"
     * @param s the string to split
     * @return value list
     */
    public static String[] getSubList(String s)
    {
        s = s.trim();
        if (s.startsWith("[") && s.endsWith("]"))
        {
            String sub = s.substring(1, s.length() - 1);
            String[] valueList= sub.split(",");
            
            return valueList;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Get current project path
     * @return project path
     */
    public static String getProjectPath()
    {
        return System.getProperty("user.dir").replace("\\", "/");
    }
    
    /**
     * Get local time
     * @return time with long type
     */
    public static String getLocalTime()
    {
        Date d = new Date();
        long longtime = d.getTime();
        return String.valueOf(longtime);
    }
    /**
     * Get local time
     * @return time with long type
     */
    public static String getCurrentTime()
    {
        Date curTime = new Date();
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return time.format(curTime);
    }
    
    /**
     * Print string
     * @param s string will be print
     */
    public static void print(String s)
    {
        System.out.print(s);
    }
    
    /**
     * Print string
     * @param s string will be print
     */
    public static void println(String s)
    {
        System.out.println(s);
    }
    
    /**
     * Get code information
     * @return code information
     */
    public static String getLineInfo()
    {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        return "[FILE:" + ste.getFileName() + " METHOD:" + ste.getMethodName() + " LINE:" + ste.getLineNumber() + "]";
    }
    
    /**
     * Check the XML file is valid or not
     * @param xmlFile MXL file Path
     * @return true if valid, otherwise false
     */
    public static boolean isXMLFileValid(String xmlFile)
    {
        if (xmlFile.endsWith(".xml") && MFile.isExisted(xmlFile))
        {
            return true;
        }
        
        return false;
        
    }
}