/*
 * FileName: MGenReplacedStep.java
 * 
 * Description: create MGenReplacedStep class
 * 
 * History:
 * 1.0 SHUYUFANG 2013-09-30 Create
 */


package ARH.common.generate;

import java.util.*;

import ARH.common.config.*;
import ARH.framework.basic.MBasicApi;
import ARH.framework.exception.MException;
import ARH.framework.logger.MLogMag;
import ARH.framework.xml.*;

import org.dom4j.*;

/**
 * this class is used to generate replaced step list
 * @author SHUYUFANG
 * @version 1.0
 */
public class MGenReplacedStep
{    
    /** step variables combination map */
    private HashMap<Integer, ArrayList<HashMap<String, String>>> varCombsMap;
    
    /** step variable combinations map */
    private HashMap<Integer, ArrayList<String>> varCombsNameMap;
    
    /** step actions map */
    private HashMap<Integer, ArrayList<String>> stepActionMap;
    
    /** step name map */
    private HashMap<Integer, String> stepNameMap;
    
    /** replaced step actions map */
    private HashMap<Integer, ArrayList<String>> replacedStepMap;
    
    /** step path */
    private ArrayList<Integer> stepPath;
    
    /** replaced step result list */
    private ArrayList<HashMap<Integer, ArrayList<String>>> replacedStepList;
    
    /** the list of replaced name */
    private ArrayList<String> replacedNameList;
    
    /** temporary replaced name */
    private String curReplacedName;
    
    /**
     * constructor
     * @param mbtXml the MBT XML file path
     * @param stepVarCombXml the step variables combination map
     */
    public MGenReplacedStep(String mbtXml, String stepVarCombXml)
    {
        varCombsMap = new HashMap<Integer, ArrayList<HashMap<String, String>>>();
        varCombsNameMap = new HashMap<Integer, ArrayList<String>>();
        stepNameMap = new HashMap<Integer, String>();
        parseCombXml(stepVarCombXml);
        
        stepActionMap = new HashMap<Integer, ArrayList<String>>();        
        parseMbtXml(mbtXml);
    }
    
    /**
     * generate replaced steps according to the step path
     * @param stepPath the specified step path
     * @return the list of replaced step result
     */
    public void genReplacedStep(ArrayList<Integer> stepPath)
    {
        replacedStepList = new ArrayList<HashMap<Integer, ArrayList<String>>>();
        replacedNameList = new ArrayList<String>();
        replacedStepMap = new HashMap<Integer, ArrayList<String>>();
        curReplacedName = "";
        this.stepPath = stepPath;

        dfs(1);
    }
    
    /**
     * get the list of replaced step
     * @return the list of replaced step
     */
    public ArrayList<HashMap<Integer, ArrayList<String>>> getReplacedStepList()
    {
        return replacedStepList;
    }
    
    /**
     * get the list of replaced name
     * @return the list of replaced name
     */
    public ArrayList<String> getReplacedNameList()
    {
        return replacedNameList;
    }
    
    /**
     * depth first search
     * @param curPos current position of step path
     */
    private void dfs(int curPos)
    {
    	if (curPos == stepPath.size() + 1)
    	{
    		replacedStepList.add(new HashMap<Integer, ArrayList<String>>(replacedStepMap));
    		replacedNameList.add(new String(curReplacedName));
    		return;
    	}
    	int curStepNumber = stepPath.get(curPos - 1);
    	ArrayList<HashMap<String, String>> varCombsList = varCombsMap.get(curStepNumber);
    	ArrayList<String> varCombsNameList = varCombsNameMap.get(curStepNumber);
    	
    	ArrayList<String> curActionList = stepActionMap.get(curStepNumber);
    	
    	// no variables combination existed
    	if (0 == varCombsList.size())
    	{
    		varCombsList.add(new HashMap<String, String>());
    		varCombsNameList.add("");
    	}
    	
    	// replace variable combinations one by one
    	for (int i = 0; i < varCombsList.size(); ++i)
    	{
    		HashMap<String, String> varCombs = varCombsList.get(i);
    		ArrayList<String> replacedActionList = replaceVar(varCombs, curActionList);
    		replacedStepMap.put(curStepNumber, replacedActionList);
    		
    		String bakReplacedName = curReplacedName;
    		if (!varCombsNameList.get(i).equals(""))
    		{
    		    curReplacedName += "{(" + stepNameMap.get(curStepNumber) + ")" + varCombsNameList.get(i) 
    		            + "}";
    		}
    		
    		dfs(curPos + 1);
    		
    		curReplacedName = bakReplacedName;
    	}
    }
    
    /**
     * replace variables using the combination
     * @param varCombs variables key value map
     * @param actionList the actions of one step
     * @return the replaced action list
     */
    private ArrayList<String> replaceVar(HashMap<String, String> varCombs, ArrayList<String> actionList)
    {
    	ArrayList<String> replacedActionList = new ArrayList<String>();
    	
    	for (String action : actionList)
    	{
    		String replacedAction = new String(action);
    		
	        // substitute parameters one by one
	        for (String varName : varCombs.keySet())
	        {    	
	            // the REGEX of parameters before substituted
	            String originStr = "\\{MBT_STEP_PARAM\\(" + varName + "\\)\\}";
	            
	            String goalStr = varCombs.get(varName);
	            replacedAction = replacedAction.replaceAll(originStr, goalStr);
	        }
	        
	        replacedActionList.add(replacedAction);
    	}
    	
    	return replacedActionList;
    }

    /**
     * parse the MBT XML file to get step actions map
     * @param mbtXml the MBT XML file path
     */
    @SuppressWarnings("unchecked")
    private void parseMbtXml(String mbtXml)
    {
        try
        {
            MXmlParse xmlParser = new MXmlParse(mbtXml);
        
            // get root element called "MODEL"
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
                int stepNumber = Integer.valueOf(xmlParser.getNodeData(stepNumberElem));
                
                // get sub element called "ACTIONS"
                Element actionsElem = xmlParser.getSubFirstElement(testStepElem, 
                        MCommonConfig.MBT_XML_ACTIONS);
                
                // the action list of a test step
                ArrayList<String> actionList = new ArrayList<String>();
                
                // get the list of sub element called "ACTION"
                List<Element> actionElemList = xmlParser.getSubElement(actionsElem, 
                        MCommonConfig.MBT_XML_ACTION);
                for (Element actionElem : actionElemList)
                {
                    // get action content and add it to the action list
                    String action = xmlParser.getNodeData(actionElem);
                    actionList.add(action);
                }
                
                stepActionMap.put(Integer.valueOf(stepNumber), actionList);
            }
        }
        catch (DocumentException e)
        {
            MLogMag.getInstance().getLogger().error(MBasicApi.getLineInfo() + "\n" 
                    + "Cannot parse mbt xml in MGenReplacedStep\n" + e.getMessage());
        }
        catch (MException e)
        {
            MLogMag.getInstance().getLogger().error(MBasicApi.getLineInfo() + "\n" 
                    + "Cannot parse mbt xml in MGenReplacedStep\n" + e.getMessage());
        }
    }
    
    /**
     * parse the step variable combinations XML file to get 
     * step variables combination map and step name map
     * @param stepVarCombXml the step variable combinations XML file path
     */
    @SuppressWarnings("unchecked")
    private void parseCombXml(String stepVarCombXml)
    {
        try
        {            
            MXmlParse xmlParser = new MXmlParse(stepVarCombXml);
            
            // get root element called "TESTSTEPS"
            Element testStepsElem = xmlParser.getRoot();
            
            // get the list of sub element called "TESTSTEP"
            List<Element> testStepElemList = xmlParser.getSubElement(testStepsElem, 
                    MCommonConfig.MBT_XML_TEST_STEP);
            
            for (Element testStepElem : testStepElemList)
            {
                // get "STEPNUMBER" attribute
                String stepNumber = xmlParser.getAttrValue(testStepElem, 
                        MCommonConfig.MBT_XML_STEP_NUMBER);
                
                // get "STEPNAME" attribute
                String stepName = xmlParser.getAttrValue(testStepElem, 
                		MCommonConfig.MBT_XML_NAME);
                
                stepNameMap.put(Integer.valueOf(stepNumber), stepName);

                ArrayList<HashMap<String, String>> varCombsList = 
                        new ArrayList<HashMap<String, String>>();
                ArrayList<String> varCombsNameList = new ArrayList<String>();
                
                // get sub element called "COMBINATIONS"
                Element combsElem = xmlParser.getSubFirstElement(testStepElem, 
                        MCommonConfig.MBT_XML_COMBINATIONS);
                
                // get the list of sub element called "COMBINATION"
                List<Element> combElemList = xmlParser.getSubElement(combsElem, 
                        MCommonConfig.MBT_XML_COMBINATION);
                
                for (Element combElem : combElemList)
                {
                    // get the list of sub element called "VAR"
                    List<Element> varElemList = xmlParser.getSubElement(combElem, 
                            MCommonConfig.MBT_XML_VAR);
                    HashMap<String, String> varMap = new HashMap<String, String>();
                    String varCombsName = "";
                    
                    for (Element varElem : varElemList)
                    {
                        // get "NAME" attribute
                        String name = xmlParser.getAttrValue(varElem, MCommonConfig.MBT_XML_NAME);
                        
                        // get "VALUE" attribute
                        String value = xmlParser.getAttrValue(varElem, MCommonConfig.MBT_XML_VALUE);
                        
                        varCombsName += "," + name + "=" + value;
                        
                        varMap.put(name, value);
                    }
                    varCombsList.add(varMap);
                    
                    if (!varCombsName.equals(""))
                    {
                        varCombsName = varCombsName.substring(1);
                    }
                    varCombsNameList.add(varCombsName);
                }
                
                varCombsMap.put(Integer.valueOf(stepNumber), varCombsList);
                varCombsNameMap.put(Integer.valueOf(stepNumber), varCombsNameList);
            }
        }
        catch (DocumentException e)
        {
            MLogMag.getInstance().getLogger().error(MBasicApi.getLineInfo() + "\n" 
                    + "Cannot parse combination xml \"" + stepVarCombXml + "\" in MGenReplacedStep.\n" 
                    + e.getMessage());
        }
        catch (MException e)
        {
            MLogMag.getInstance().getLogger().error(MBasicApi.getLineInfo() + "\n" 
                    + "Cannot parse combination xml \"" + stepVarCombXml + "\" in MGenReplacedStep.\n" 
                    + e.getMessage());
        }
    }
    
    /**
     * get step names map
     * @return the step names map
     */
    public HashMap<Integer, String> getStepNameMap()
    {
    	return stepNameMap;
    }
}