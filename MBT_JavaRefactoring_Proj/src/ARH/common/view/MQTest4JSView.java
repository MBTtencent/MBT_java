/*
 * FileName: MQTestViewBase.java
 * 
 * Description: create MQTestViewBase class
 * 
 * Version: 1.0
 * 
 * History:
 * 1.0 MICKCHEN 2013-09-06 Create
 */


package ARH.common.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.dom4j.DocumentException;

import ARH.common.view.qtest.MQTestActionHandler;
import ARH.common.view.qtest4js.MQTest4JSActionHandler;
import ARH.framework.basic.MBasicApi;
import ARH.framework.exception.MException;
import ARH.framework.logger.MLogMag;

/**
 * This class is used to set QTest case format, 
 * @author MICKCHEN
 * @version 1.0
 */
public class MQTest4JSView extends MViewBase
{
    private MQTest4JSActionHandler qTest4JSActionHandler = new MQTest4JSActionHandler();
    
    public static ArrayList<String> dataFileList = new ArrayList<String>();
    
    /**
     * Set case name
     * @param caseName original case name
     * @return case name after updated
     */
    public String setCaseName(String caseName)
    {
        return caseName + ".js";
    }
    
    /**
     * Set case comment
     * @param caseName original case name
     * @return case comment
     */
    public String setCaseComment(String caseName)
    {
        return "";
    }
    
    /**
     * Set case start log
     * @param caseName case name
     * @return case start log
     */
    public String setCaseStartLog(String caseName)
    {
        return "";
    }
    
    /**
     * Set case end log
     * @param caseName case name
     * @return case end log
     */
    public String setCaseEndLog(String caseName)
    {
        return "";
    }
    
    /**
     * Set step comment
     * @param stepNum step number
     * @param stepName step name
     * @return step comment
     */
    public String setStepComment(int stepNum, String stepName)
    {
        return "";
    }
    
    /**
     * Set step log
     * @param stepNum step number
     * @param stepName step name
     * @return log content
     */
    public String setStepLog(int stepNum, String stepName)
    {
        return "";
    }
    
    /**
     * Set step start log
     * @param stepNum step number 
     * @param stepName step name
     * @return step start log
     */
    public String setStepStartLog(int stepNum, String stepName)
    {
        return "\n";
    }
    
    /**
     * Set step end log
     * @param stepNum step number 
     * @param stepName step name
     * @return step end log
     */
    public String setStepEndLog(int stepNum, String stepName)
    {
        return "\n}\n";
    }
    
    /**
     * Set action
     * @param actionName action name
     * @return action content
     */
    public String setAction(String actionStr)
    {
        try 
        {
            return qTest4JSActionHandler.handleAction(actionStr);// + "\n";
        } 
        catch (Exception e) {
            e.printStackTrace();
            MLogMag.getInstance().getLogger().error(MBasicApi.getLineInfo() + 
                    "Set action error...");
            
            return "";
        }
    }
    
    /**
     * Set content
     * @param content script content
     * @return result 
     */
    public String setContent(String content)
    {
        return qTest4JSActionHandler.setContent(content);
    }
    
    /**
     * Add view
     */
    public void addView()
    {
        MViewMag.getInsctance().addView("MQTest4JSView");
    }
    
    /**
     * Handle the variables in the data file
     * @param content
     * @param sVarPath
     * @param caseName
     * @param dir
     * @param MBT XML path
     * @return true if success, otherwise false
     * @throws DocumentException 
     * @throws MException 
     */
    @Override
    public boolean handleVars(String content, String varPath, String caseName, 
            HashMap<Integer,String> nameMap, String dir, String mbtXmlPath)
            throws MException, DocumentException
    {
        return this.genCase(content, dir + "/" + this.setCaseName(caseName));
    }
    
}
