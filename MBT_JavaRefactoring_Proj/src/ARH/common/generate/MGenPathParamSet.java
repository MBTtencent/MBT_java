/*
 * FileName: MGenPathParamSet.java
 * 
 * Description: create MGenPathParamSet class
 * 
 * History:
 * 1.0 SHUYUFANG 2013-08-27 Create
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
 * this class is used to generate parameter set of a case path
 * @author SHUYUFANG
 * @version 1.0
 */
public class MGenPathParamSet
{
    /**
     * generate parameter set of a case path
     * @param mbtXmlFilePath MBT XML file path
     * @param stepPath the step path from which get parameter info
     * @return parameter set
     */
    @SuppressWarnings("unchecked")
    public static HashSet<String> genPathParamSet(String mbtXmlFilePath, ArrayList<Integer> stepPath)
    {
        HashSet<String> paramSet = new HashSet<String>();
        MXmlParse xmlParser;
        try
        {
            if (null == stepPath)
            {
                return paramSet;
            }
            xmlParser = new MXmlParse(mbtXmlFilePath);
            
            // the step map, key: step number, value: actions of the step
            HashMap<Integer, String> testStepElemMap = new HashMap<Integer, String>();
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
                // get sub element called "ACTIONS"
                Element actionsElem = xmlParser.getSubFirstElement(testStepElem, 
                        MCommonConfig.MBT_XML_ACTIONS);
                // get the list of sub element called "ACTION"
                List<Element> actionElemList = xmlParser.getSubElement(actionsElem, 
                        MCommonConfig.MBT_XML_ACTION);
                String actionsStr = "";
                for (Element actionElem : actionElemList)
                {
                    // get action content
                    String action = xmlParser.getNodeData(actionElem);
                    actionsStr += action;
                }
                // add step pair to the step map
                testStepElemMap.put(Integer.valueOf(stepNumber), actionsStr);
            }

            Pattern pattern = Pattern.compile("MBT_SCEN_PARAM\\(.*?\\)");
            Matcher matcher;
            for (int i = 0; i < stepPath.size(); ++i)
            {
                int stepNumber = stepPath.get(i);
                // the step is used before
                if (testStepElemMap.get(stepNumber).equals(""))
                {
                    continue;
                }
                String actionsStr = testStepElemMap.get(stepNumber);
                // mark used step
                testStepElemMap.put(stepNumber, "");
                
                matcher = pattern.matcher(actionsStr);
                while (matcher.find())
                {
                    String paramInfo = matcher.group();
                    // get parameter name
                    String varName = paramInfo.substring(15, paramInfo.length() - 1);
                    // add parameter to the set
                    paramSet.add(varName);
                }
            }
        }
        catch (DocumentException e)
        {
            MLogMag.getInstance().getLogger().error(MBasicApi.getLineInfo() + "\n" 
                    + "Cannot generate path param set from mbt file \"" + mbtXmlFilePath 
                    + "\" in MGenParamSet" + "\n" + e.getMessage());            
        }
        catch (MException e)
        {
            MLogMag.getInstance().getLogger().error(MBasicApi.getLineInfo() + "\n" 
                    + "Cannot generate path param set from mbt file \"" + mbtXmlFilePath 
                    + "\" in MGenParamSet" + "\n" + e.getMessage());
        }
        
        return paramSet;
    }
}