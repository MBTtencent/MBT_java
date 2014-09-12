/*
 * FileName: MViewBase.java
 * 
 * Description: create MViewBase class
 * 
 * Version: 1.0
 * 
 * History:
 * 1.0 MICKCHEN 2013-08-27 Create
 */


package ARH.common.view;

import java.util.HashMap;

import org.dom4j.DocumentException;

import ARH.framework.exception.MException;
import ARH.framework.file.MFile;

/**
 * This class is the basic class of all view
 * @author MICKCHEN
 * @version 1.0
 */
public abstract class MViewBase
{
    /**
     * Define case name type
     */
    public static final int MCASE_NAME_TYPE_NUM         = 0;
    public static final int MCASE_NAME_TYPE_DATAINFO    = 1;
    
    /**
     * Set step name
     * @param stepName original step name
     * return step name after updated
     */
    public String setStepName(String stepName)
    {
        return stepName;
    }
    
    /**
     * Set step log
     * @param stepNum step number
     * @param stepName step name
     * @return log content
     */
    public String setStepLog(int stepNum, String stepName)
    {
        return String.valueOf(stepNum) + " " + stepName;
    }
    
    /**
     * Set case name
     * @param caseName original case name
     * @return case name after updated
     */
    public String setCaseName(String caseName)
    {
        return caseName;
    }
    
    /**
     * Set case comment
     * @param caseName original case name
     * @return case comment
     */
    public String setCaseComment(String caseName)
    {
        return caseName;
    }
    
    /**
     * Set step comment
     * @param stepNum step number
     * @param stepName step name
     * @return step comment
     */
    public String setStepComment(int stepNum, String stepName)
    {
        return "# " + String.valueOf(stepNum) + " " + stepName;
    }
    
    /**
     * Set action
     * @param actionName action name
     * @return action content
     */
    public String setAction(String actionStr)
    {
        return actionStr;
    }
    
    /**
     * Set case start log
     * @param caseName case name
     * @return case start log
     */
    public String setCaseStartLog(String caseName)
    {
        return caseName;
    }
    
    /**
     * Set case end log
     * @param caseName case name
     * @return case end log
     */
    public String setCaseEndLog(String caseName)
    {
        return caseName;
    }
    
    /**
     * Set step start log
     * @param stepNum step number 
     * @param stepName step name
     * @return step start log
     */
    public String setStepStartLog(int stepNum, String stepName)
    {
        return String.valueOf(stepNum) + " " + stepName;
    }
    
    /**
     * Set step end log
     * @param stepNum step number 
     * @param stepName step name
     * @return step end log
     */
    public String setStepEndLog(int stepNum, String stepName)
    {
        return String.valueOf(stepNum) + " " + stepName;
    }

    /**
     * Set previous data
     * @return result
     */
    public String setPreData()
    {
        return "";
    }
    
    /**
     * Set content
     * @param content script content
     * @return result 
     */
    public String setContent(String content)
    {
        return content;
    }
    
    /**
     * Generate case
     * @param caseContent case content
     * @param caseName case name
     * @return true if success, otherwise false
     */
    public boolean genCase(String caseContent, String path) throws MException
    {
        return MFile.writeFile(path, caseContent);
    }
    
    /**
     * Add view,should be realized in derived class 
     */
    abstract public void addView();

    /**
     * Handle the variables in the data file
     * @param content
     * @param sVarPath
     * @param caseName
     * @param dir
     * @param MBT XML file
     * @return true if success, otherwise false
     * @throws DocumentException 
     * @throws MException 
     */
    abstract public boolean handleVars(String content, String varPath, String caseName, 
            HashMap<Integer,String> nameMap, String dir, String mbtXmlPath)
            throws MException, DocumentException;
}
