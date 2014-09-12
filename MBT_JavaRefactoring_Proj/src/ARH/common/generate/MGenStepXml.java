/*
 * FileName: MGenStepXml.java
 * 
 * Description: create MGenStepXml class
 * 
 * History:
 * 1.0 SHUYUFANG 2013-09-29 Create
 */


package ARH.common.generate;

import java.util.*;
import java.util.regex.*;

import ARH.common.config.*;
import ARH.common.data.*;
import ARH.framework.basic.*;
import ARH.framework.exception.MException;
import ARH.framework.json.*;
import ARH.framework.logger.*;
import ARH.framework.xml.*;

import org.dom4j.*;

/**
 * This class is used to generate step variables and variable combinations XML file
 * @author SHUYUFANG
 * @version 1.0
 */
public class MGenStepXml
{
    /**
     * generate step variables combinations XML
     * @param stepVarCombXml the path of step variable combinations XML file
     * @param stepVarXml the path of step variables XML file
     * @param mbtXmlPath the path of MBT XML file
     * @return XML file generated or not
     */
    @SuppressWarnings("unchecked")
    public static boolean genStepXml(String stepVarCombXml, String stepVarXml, String mbtXmlPath)
    {
        try
        {      
            // XML creator to create step variables combination XML file
            MXmlCreate varCombXmlCreator = new MXmlCreate(stepVarCombXml);
            
            // XML creator to create step variables XML file
            MXmlCreate stepVarXmlCreator = new MXmlCreate(stepVarXml);
            
            // create test steps element
            Element varCombTestStepsElem = varCombXmlCreator.addElement(null, 
                    MCommonConfig.MBT_XML_TEST_STEPS);
            Element stepVarTestStepsElem = stepVarXmlCreator.addElement(null, 
                    MCommonConfig.MBT_XML_TEST_STEPS);
            
            // MBT XML parser
            MXmlParse xmlParser = new MXmlParse(mbtXmlPath);
            // get model element
            Element modelElem = xmlParser.getRoot();
            // get test steps element
            Element testStepsElem = xmlParser.getSubFirstElement(modelElem, 
                    MCommonConfig.MBT_XML_TEST_STEPS);
            // get list of test step element
            List<Element> testStepElemList = xmlParser.getSubElement(testStepsElem,
                    MCommonConfig.MBT_XML_TEST_STEP);
            for (Element testStepElem : testStepElemList)
            {               
                // get name element
                Element nameElem = xmlParser.getSubFirstElement(testStepElem, 
                        MCommonConfig.MBT_XML_NAME);
                String name = xmlParser.getNodeData(nameElem);
                
                // get step number element
                Element stepNumberElem = xmlParser.getSubFirstElement(testStepElem,
                        MCommonConfig.MBT_XML_STEP_NUMBER);
                String stepNumber = xmlParser.getNodeData(stepNumberElem);
                // get actions element
                Element actionsElem = xmlParser.getSubFirstElement(testStepElem,
                        MCommonConfig.MBT_XML_ACTIONS);
                List<Element> actionElemList = xmlParser.getSubElement(actionsElem, 
                        MCommonConfig.MBT_XML_ACTION);
                
                String actionsStr = "";
                for (Element actionElem : actionElemList)
                {
                    String action = xmlParser.getNodeData(actionElem);
                    actionsStr += action;
                }
                
                // generate parameter set
                HashSet<String> paramSet = MGenStepXml.genStepParamSet(actionsStr);
                // get parameter element
                Element paramElem = xmlParser.getSubFirstElement(testStepElem,
                        MCommonConfig.MBT_XML_PARAM);
                String param = xmlParser.getNodeData(paramElem);
                ArrayList<HashMap<String, String>> paramList = MJsonHandler.getJsonParamList(param);
                // get data element
                Element dataElem = xmlParser.getSubFirstElement(testStepElem,
                        MCommonConfig.MBT_XML_DATA);
                String data = xmlParser.getNodeData(dataElem);
                
                // generate data
                HashMap<Integer, HashMap<String, String>> paramComMap = 
                        MDataAlgoMag.getInstance().getAlgorithm(data).genData(paramList, paramSet);
                if (null == paramComMap)
                {
                    paramComMap = new HashMap<Integer, HashMap<String, String>>();
                }                
                
                // create test step element
                Element varCombTestStepElem = varCombXmlCreator.addElement(varCombTestStepsElem, 
                        MCommonConfig.MBT_XML_TEST_STEP);
                Element stepVarTestStepElem = stepVarXmlCreator.addElement(stepVarTestStepsElem, 
                        MCommonConfig.MBT_XML_TEST_STEP);
                
                // add name attribute
                varCombXmlCreator.addAttr(varCombTestStepElem, MCommonConfig.MBT_XML_NAME, name);
                stepVarXmlCreator.addAttr(stepVarTestStepElem, MCommonConfig.MBT_XML_NAME, name);
                
                // add data attribute
                stepVarXmlCreator.addAttr(stepVarTestStepElem, MCommonConfig.MBT_XML_DATA, data);
                
                // add step number attribute
                varCombXmlCreator.addAttr(varCombTestStepElem, MCommonConfig.MBT_XML_STEP_NUMBER, 
                        stepNumber);
                
                // create combinations element
                Element varCombCombinationsElem = varCombXmlCreator.addElement(varCombTestStepElem, 
                        MCommonConfig.MBT_XML_COMBINATIONS);
                for (int idx : paramComMap.keySet())
                {
                    // create combination element
                    Element varCombCombinationElem = varCombXmlCreator.addElement(varCombCombinationsElem, 
                            MCommonConfig.MBT_XML_COMBINATION);
                    for (HashMap<String, String> paramInfoMap : paramList)
                    {
                        String varCombNameAttr = paramInfoMap.get(MCommonConfig.MBT_XML_NAME);
                        if (!paramComMap.get(idx).containsKey(varCombNameAttr))
                        {
                            continue;
                        }                        
                        Element varCombVarElem = varCombXmlCreator.addElement(varCombCombinationElem, 
                                MCommonConfig.MBT_XML_VAR);
                        String varCombValueAttr = paramComMap.get(idx).get(varCombNameAttr);
                        String varCombTypeAttr = paramInfoMap.get(MCommonConfig.MBT_XML_TYPE);
                        // add name, value, type attribute
                        varCombXmlCreator.addAttr(varCombVarElem, MCommonConfig.MBT_XML_NAME, 
                                varCombNameAttr);
                        varCombXmlCreator.addAttr(varCombVarElem, MCommonConfig.MBT_XML_VALUE, 
                                varCombValueAttr);
                        varCombXmlCreator.addAttr(varCombVarElem, MCommonConfig.MBT_XML_TYPE, 
                                varCombTypeAttr);
                    }
                }
                
                // create VAR elements
                for (int i = 0; null != paramList && i < paramList.size(); ++i)
                {
                    Element stepVarVarElem = stepVarXmlCreator.addElement(stepVarTestStepElem, 
                            MCommonConfig.MBT_XML_VAR);
                    stepVarXmlCreator.addAttr(stepVarVarElem, MCommonConfig.MBT_XML_NAME,
                            paramList.get(i).get(MCommonConfig.MBT_XML_NAME));
                    stepVarXmlCreator.addAttr(stepVarVarElem, MCommonConfig.MBT_XML_VALUE,
                            paramList.get(i).get(MCommonConfig.MBT_XML_VALUE));
                    stepVarXmlCreator.addAttr(stepVarVarElem, MCommonConfig.MBT_XML_TYPE,
                            paramList.get(i).get(MCommonConfig.MBT_XML_TYPE));
                }
            }
            
            boolean res = varCombXmlCreator.createXml();
            res = stepVarXmlCreator.createXml() && res;
                        
            return res;
        }
        catch (MException e)
        {
            MLogMag.getInstance().getLogger().warning(MBasicApi.getLineInfo() + "\n" + "Cannot generate " 
                    + "step variables combination xml in MGenStepXml" + "\n" + e.getMessage());
            return false;
        }
        catch (DocumentException e)
        {
            MLogMag.getInstance().getLogger().warning(MBasicApi.getLineInfo() + "\n" + "Cannot generate " 
                    + "step variables combination xml in MGenStepXml" + "\n" + e.getMessage());
            return false;
        }
    }
    
    /**
     * generate step parameters set
     * @param actionsStr the string of all actions
     * @return the set of step parameters
     */
    private static HashSet<String> genStepParamSet(String actionsStr)
    {        
        HashSet<String> paramSet = new HashSet<String>();
        
        Pattern pattern = Pattern.compile("MBT_STEP_PARAM\\(.*?\\)");
        Matcher matcher = null;
        matcher = pattern.matcher(actionsStr);
        while (matcher.find())
        {
            String paramInfo = matcher.group();
            // get parameter name
            String varName = paramInfo.substring(15, paramInfo.length() - 1);
            // add parameter to the set
            paramSet.add(varName);
        }
        
        return paramSet;
    }
}