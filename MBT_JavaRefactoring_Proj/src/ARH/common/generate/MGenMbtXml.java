/*
 * FileName: MGenXml.java
 * 
 * Description: create MGenXml class
 * 
 * History:
 * 1.0 SHUYUFANG 2013-08-21 Create
 */


package ARH.common.generate;

import java.lang.reflect.*;
import java.util.*;

import ARH.common.config.*;
import ARH.framework.annotation.*;
import ARH.framework.basic.MBasicApi;
import ARH.framework.dynamic.*;
import ARH.framework.exception.MException;
import ARH.framework.file.*;
import ARH.framework.logger.MLogMag;
import ARH.framework.xml.*;

import org.dom4j.*;

/**
 * this class is used to generate XML file of MBT written by user
 * @author SHUYUFANG
 * @version 1.0
 */
public class MGenMbtXml
{
    /**
     * constructor
     */
    private MGenMbtXml()
    {
        
    }
    
    /**
     * generate XML file
     * @param mbtClassName the name of generated MBT class
     * @param stepLogPath the path of log file
     * @param mbtXmlPath the MBT XML file path
     * @return XML file generated or not
     */
    public static boolean genMbtXmlFile(String mbtClassName, String stepLogPath, String mbtXmlPath)
    {
        // generate step log file first
        if (!MGenMbtXml.genStepLogFile(mbtClassName, stepLogPath))
        {
            return false;
        }
        try
        {
            // XML creator 
            MXmlCreate xmlCreator = new MXmlCreate(mbtXmlPath);
            // set XML file type and encoding
            xmlCreator.setFormat(MXmlConfig.MXML_FORMAT_PRETTY_PRINT, 
                    MXmlConfig.MXML_FORMAT_ENCODING_UTF8_TYPE);
            // MODEL element
            Element modelElem = xmlCreator.addElement(null, MCommonConfig.MBT_XML_MODEL);
            // add CLASS element to MODEL element
            Element classElem = xmlCreator.addElement(modelElem, MCommonConfig.MBT_XML_CLASS);
            String tmpClassName = mbtClassName.substring(mbtClassName.lastIndexOf(".") + 1);
            xmlCreator.addElementData(classElem, tmpClassName);
            // add SCENARIOS element to MODEL element
            Element scenariosElem = xmlCreator.addElement(modelElem, MCommonConfig.MBT_XML_SCENARIOS);
            
            // get SCENARIO method list
            ArrayList<Method> scenMethodList = MClass.getMethodsWithAnnotation(mbtClassName,
                    MScenario.class);
            for (Method method : scenMethodList)
            {
                // add SCENARIO element to SCENARIOS element
                Element scenarioElem = xmlCreator.addElement(scenariosElem, MCommonConfig.MBT_XML_SCENARIO);
                
                // add NAME element to SCENARIO element
                Element nameElem = xmlCreator.addElement(scenarioElem, MCommonConfig.MBT_XML_NAME);
                xmlCreator.addElementData(nameElem, method.getName());
                
                // add OPTION element to SCENARIO element
                Element optionElem = xmlCreator.addElement(scenarioElem, MCommonConfig.MBT_XML_OPTION);
                xmlCreator.addElementData(optionElem, MAnnotation.getScenOption(method));
                
                // add PARAMETER element to SCENARIO element
                Element paramElem = xmlCreator.addElement(scenarioElem, MCommonConfig.MBT_XML_PARAM);
                xmlCreator.addElementData(paramElem, MAnnotation.getScenParam(method));
                
                // add ROUTE element to SCENARIO element
                Element pathElem = xmlCreator.addElement(scenarioElem, MCommonConfig.MBT_XML_ROUTE);
                xmlCreator.addElementData(pathElem, MAnnotation.getScenPath(method));
            }
            
            // add TESTSTEPS element to MODEL element
            Element testStepsElem = xmlCreator.addElement(modelElem, MCommonConfig.MBT_XML_TEST_STEPS);
            
            // get content lines of the step log file
            ArrayList<String> stepLogLines = MFile.readFileLines(stepLogPath);
            int lineIdx = 0;
            while (lineIdx < stepLogLines.size())
            {
                // begin of a step
                if (stepLogLines.get(lineIdx).equals("STEP BEGIN"))
                {
                    // add TESTSTEP element to TESTSTEPS element
                    Element testStepElem = xmlCreator.addElement(testStepsElem, 
                            MCommonConfig.MBT_XML_TEST_STEP);
                    
                    // add name element to TESTSTEP element
                    String name = stepLogLines.get(++lineIdx).substring(7);
                    Element nameElem = xmlCreator.addElement(testStepElem, MCommonConfig.MBT_XML_NAME);
                    xmlCreator.addElementData(nameElem, name);
                    
                    // add STEPNUMBER element to TESTSTEP element
                    String stepNumber = stepLogLines.get(++lineIdx).substring(13);
                    Element stepNumberElem = xmlCreator.addElement(testStepElem, 
                            MCommonConfig.MBT_XML_STEP_NUMBER);
                    xmlCreator.addElementData(stepNumberElem, stepNumber);
                    
                    // add BEGINSTATE element to TESTSTEP element
                    String beginState = stepLogLines.get(++lineIdx).substring(13);
                    Element beginStateElem = xmlCreator.addElement(testStepElem, 
                            MCommonConfig.MBT_XML_BEGIN_STATE);
                    xmlCreator.addElementData(beginStateElem, beginState);
                    
                    // add ENDSTATE element to TESTSTEP element
                    String endState = stepLogLines.get(++lineIdx).substring(11);
                    Element endStateElem = xmlCreator.addElement(testStepElem, 
                            MCommonConfig.MBT_XML_END_STATE);
                    xmlCreator.addElementData(endStateElem, endState);
                    
                    // add REPEATTIME element to TESTSTEP element
                    String repeatTime = stepLogLines.get(++lineIdx).substring(13);
                    Element repeatTimeElem = xmlCreator.addElement(testStepElem, 
                            MCommonConfig.MBT_XML_REPEAT_TIME);
                    xmlCreator.addElementData(repeatTimeElem, repeatTime);

                    // add PARAM element to TESTSTEP element
                    String param = stepLogLines.get(++lineIdx).substring(8);
                    Element paramElem = xmlCreator.addElement(testStepElem, MCommonConfig.MBT_XML_PARAM);
                    xmlCreator.addElementData(paramElem, param);
                    
                    // add DATA element to TESTSTEP element
                    String data = stepLogLines.get(++lineIdx).substring(7);
                    Element dataElem = xmlCreator.addElement(testStepElem, MCommonConfig.MBT_XML_DATA);
                    xmlCreator.addElementData(dataElem, data);
                    
                    ++lineIdx;
                    
                    // add ACTIONS element to TESTSTEP element
                    Element actionsElem = xmlCreator.addElement(testStepElem, 
                            MCommonConfig.MBT_XML_ACTIONS);
                    
                    // lines before step end are about actions
                    while (!stepLogLines.get(lineIdx).equals("STEP END"))
                    {
                        // add action element to ACTIONS element
                        String action = stepLogLines.get(lineIdx);
                        Element actionElem = xmlCreator.addElement(actionsElem, 
                                MCommonConfig.MBT_XML_ACTION);
                        xmlCreator.addElementData(actionElem, action);
                        
                        ++lineIdx;
                    }
                }
                ++lineIdx;
            }
            // generate XML file
            xmlCreator.createXml();
            // delete step log file
            MFile.delFile(stepLogPath);
        }
        catch (MException e)
        {
            MLogMag.getInstance().getLogger().error(MBasicApi.getLineInfo() + "\n" 
                    + "Cannot generate mbt xml file in MGenMbtXml + \"" + mbtXmlPath + "\".\n" 
                    + e.getMessage());
            return false;
        }
        return true;
    }
    
    /**
     * generate step log file
     * @param mbtClassName the name of generated MBT class
     * @param stepLogPath the path of log file
     * @return step log file generated or not
     */
    private static boolean genStepLogFile(String mbtClassName, String stepLogPath)
    {
        // get an instance of the generated MBT class
        Object obj = null;
        try
        {
            obj = MClass.getInstance(mbtClassName);
        }
        catch (MException e)
        {
            MLogMag.getInstance().getLogger().error(MBasicApi.getLineInfo() + "\n" 
                    + "Cannot get instance of class \"" + mbtClassName + "\" in MGenMbtXml.\n" 
                    + e.getMessage());
            return false;
        }
        if (null == obj)
        {
            return false;
        }
        
        // get TestStep method list of the MBT class
        ArrayList<Method> methodList = null;
        try
        {
            methodList = MClass.getMethodsWithAnnotation(mbtClassName, 
                    MTestStep.class);
        }
        catch (MException e)
        {
            MLogMag.getInstance().getLogger().error(MBasicApi.getLineInfo() + "\n" 
                    + "Cannot get methods with annocation of class \"" + mbtClassName 
                    + "\" in MGenMbtXml.\n" + e.getMessage());
            return false;            
        }
        
        // the step number list of TestStep methods
        ArrayList<Integer> methodNumber = new ArrayList<Integer>();
        
        // the sorted TestStep method list by step number
        ArrayList<Method> tmpMethodList = new ArrayList<Method>();
        
        // get step number of all methods
        for (Method method : methodList)
        {
            methodNumber.add(MAnnotation.getTestStepValue(method));
        }
        
        //sort test steps by the value of step number annotation
        for (int i = 0; i < methodNumber.size(); ++i)
        {
            for (int j = 0; j < methodNumber.size(); ++j)
            {
                if (methodNumber.get(j) == i + 1)
                {
                    tmpMethodList.add(methodList.get(j));
                    break;
                }
            }
        }
        
        // invoke methods order by step numbers
        for (Method method : tmpMethodList)
        {
            // symbol of the begin of a TestStep method
            String stepStr = "STEP BEGIN\n";
            
            // basic information of a TestStep method
            stepStr += "name = " + method.getName() + "\n";
            stepStr += "stepNumber = " + MAnnotation.getTestStepValue(method) + "\n";
            stepStr += "beginState = " + MAnnotation.getTestStepBeginState(method) + "\n";
            stepStr += "endState = " + MAnnotation.getTestStepEndState(method) + "\n";
            stepStr += "repeatTime = " + MAnnotation.getTestStepRepeatTime(method) + "\n";
            stepStr += "param = " + MAnnotation.getTestStepParam(method) + "\n";
            stepStr += "data = " + MAnnotation.getTestStepData(method) + "\n";
            
            try
            {
                // write begin symbol and basic information of a TestStep method to the log file
                MFile.appendFile(stepLogPath, stepStr);
                
                // invoke the method which has no parameter
                MClass.invokeMethod(method, obj, new Object[]{});
                
                // write symbol of the end of a TestStep method to the log file
                MFile.appendFile(stepLogPath, "STEP END\n\n");
            }
            catch (MException e)
            {
                MLogMag.getInstance().getLogger().error(MBasicApi.getLineInfo() + "\n" 
                        + "Cannot generate step log file  in MGenMbtXml \"" + stepLogPath 
                        + "\".\n" + e.getMessage());
                return false;
            }
        }
        
        return true;
    }
}