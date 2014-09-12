/*
 * FileName: MGenStepParamSet.java
 * 
 * Description: create MGenStepParamSet class
 * 
 * History:
 * 1.0 SHUYUFANG 2013-09-29 Create
 */


package ARH.common.generate;

import java.util.*;
import java.util.regex.*;

import ARH.common.config.*;
import ARH.framework.basic.MBasicApi;
import ARH.framework.exception.MException;
import ARH.framework.logger.*;
import ARH.framework.xml.*;

import org.dom4j.*;

/**
 * this class is used to generate test step parameter set
 * @author SHUYUFANG
 * @version 1.0
 */
public class MGenStepParamSet
{
    /**
     * generate step parameter set
     * @param mbtXmlFilePath MBT XML file path
     * @param testStepNumber the step number
     * @return parameter set
     */
    @SuppressWarnings("unchecked")
    public static HashSet<String> genStepParamSet(String mbtXmlFilePath, int testStepNumber)
    {
        HashSet<String> stepParamSet = new HashSet<String>();
        try
        {
            MXmlParse xmlParser = new MXmlParse(mbtXmlFilePath);
            String actionsStr = "";

            // get root element
            Element modelElem = xmlParser.getRoot();
            // get sub element called "TESTSTEPS"
            Element testStepsElem = xmlParser.getSubFirstElement(modelElem, 
                    MCommonConfig.MBT_XML_TEST_STEPS);
            // get the list of sub element called "TESTSTEP"
            List<Element> testStepElemList = xmlParser.getSubElement(testStepsElem, 
                    MCommonConfig.MBT_XML_TEST_STEP);
            for (Element testStepElem : testStepElemList)
            {
                // get sub element called "STEPNUMBER"
                Element stepNumberElem = xmlParser.getSubFirstElement(testStepElem, 
                        MCommonConfig.MBT_XML_STEP_NUMBER);
                String stepNumber = xmlParser.getNodeData(stepNumberElem);
                
                if (String.valueOf(testStepNumber).equals(stepNumber))
                {
                    // get sub element called "ACTIONS"
                    Element actionsElem = xmlParser.getSubFirstElement(testStepElem, 
                            MCommonConfig.MBT_XML_ACTIONS);
                    // get the list of sub element called "ACTION"
                    List<Element> actionElemList = xmlParser.getSubElement(actionsElem, 
                            MCommonConfig.MBT_XML_ACTION);
                    for (Element actionElem : actionElemList)
                    {
                        // get action content
                        String action = xmlParser.getNodeData(actionElem);
                        actionsStr += action;
                    }
                    
                    break;
                }
            }
            
            Pattern pattern = Pattern.compile("MBT_STEP_PARAM\\(.*?\\)");
            Matcher matcher;
            matcher = pattern.matcher(actionsStr);
            while (matcher.find())
            {
                String paramInfo = matcher.group();
                // get parameter name
                String varName = paramInfo.substring(15, paramInfo.length() - 1);
                // add parameter to the set
                stepParamSet.add(varName);
            }
        }
        catch (DocumentException e)
        {
            MLogMag.getInstance().getLogger().error(MBasicApi.getLineInfo() + "\n" 
                    + "Cannot generate step param set from mbt file \"" + mbtXmlFilePath 
                    + "\" in MGenStepParamSet" + "\n" + e.getMessage());            
        }
        catch (MException e)
        {
            MLogMag.getInstance().getLogger().error(MBasicApi.getLineInfo() + "\n" 
                    + "Cannot generate step param set from mbt file \"" + mbtXmlFilePath 
                    + "\" in MGenStepParamSet" + "\n" + e.getMessage());
        }
        
        return stepParamSet;
    }
}