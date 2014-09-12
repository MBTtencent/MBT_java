/*
 * FileName: MGenScenParser.java
 * 
 * Description: create MGenScenParser class
 * 
 * History:
 * 1.0 SHUYUFANG 2013-09-22 Create
 */


package ARH.common.generate;

import java.util.*;

import ARH.common.config.*;
import ARH.framework.basic.*;
import ARH.framework.exception.MException;
import ARH.framework.json.*;
import ARH.framework.logger.*;
import ARH.framework.xml.*;

import org.dom4j.*;

/**
 * this class is used to generate scenario parser
 * @author SHUYUFANG
 * @version 1.0
 */
public class MGenScenParser
{
	/**
	 * parse XML file to get scenarios information
     * @param mbtXmlFilePath MBT XML file path
	 * @return the list of scenarios information map
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<HashMap<String, Object>> parseScenario(String mbtXmlFile)
	{
		if (null == mbtXmlFile)
		{
			return null;
		}
		
		try
		{
		    MXmlParse xmlParser = new MXmlParse(mbtXmlFile);
			
			ArrayList<HashMap<String, Object>> scenarioInfoList = new ArrayList<HashMap<String, Object>>();
			
	        // get root element called "MODEL"
	        Element modelElem = xmlParser.getRoot();
	        // get sub element called "SCENARIOS"
	        Element scenariosElem = xmlParser.getSubFirstElement(modelElem, 
	                MCommonConfig.MBT_XML_SCENARIOS);
	        // get the list of sub element called "SCENARIO"
	        List<Element> scenarioElemList = xmlParser.getSubElement(scenariosElem, 
	                MCommonConfig.MBT_XML_SCENARIO);
	        for (Element scenarioElem : scenarioElemList)
	        {
	        	HashMap<String, Object> scenarioInfoMap = new HashMap<String, Object>();
	        	
	            // get the name of current scenario
	            Element nameElem = xmlParser.getSubFirstElement(scenarioElem, MCommonConfig.MBT_XML_NAME);
	            String name = xmlParser.getNodeData(nameElem);
	            scenarioInfoMap.put(MCommonConfig.MBT_XML_NAME, name);
	            
	            // get start nodes and path algorithm of current scenario
	            Element optionElem = xmlParser.getSubFirstElement(scenarioElem, 
	                    MCommonConfig.MBT_XML_OPTION);
	            String option = xmlParser.getNodeData(optionElem).trim();
	            
	            String start = MJsonHandler.getJsonValueByKey(option, MCommonConfig.MBT_SCENARIO_START);
	            if (null == start)
	            {
	                start = "";
	            }
	            ArrayList<String> startNodeList = new ArrayList<String>();
	            for (String startNode : start.trim().split(","))
	            {
	            	startNode = startNode.trim();
	            	if (startNode.equals(""))
	            	{
	            		break;
	            	}
	            	startNodeList.add(startNode);
	            }
	            scenarioInfoMap.put(MCommonConfig.MBT_SCENARIO_START, startNodeList);
	            
	            String stop = MJsonHandler.getJsonValueByKey(option, MCommonConfig.MBT_SCENARIO_STOP);
	            if (null == stop)
	            {
	                stop = "";
	            }
	            ArrayList<String> stopNodeList = new ArrayList<String>();
	            for (String stopNode : stop.trim().split(","))
	            {
	            	stopNode = stopNode.trim();
	            	if (stopNode.equals(""))
	            	{
	            		break;
	            	}
	            	stopNodeList.add(stopNode);
	            }
	            scenarioInfoMap.put(MCommonConfig.MBT_SCENARIO_STOP, stopNodeList);
	            
	            String state = MJsonHandler.getJsonValueByKey(option, 
	                    MCommonConfig.MBT_SCENARIO_THROUGH_STATE);
	            if (null == state)
	            {
	                state = "";
	            }
	            ArrayList<String> throughNodeList = new ArrayList<String>();
	            for (String throughNode : state.trim().split(","))
	            {
	            	throughNode = throughNode.trim();
	            	if (throughNode.equals(""))
	            	{
	            		break;
	            	}
	            	throughNodeList.add(throughNode);
	            }
	            scenarioInfoMap.put(MCommonConfig.MBT_SCENARIO_THROUGH_STATE, throughNodeList);
	            
	            String step = MJsonHandler.getJsonValueByKey(option, 
	                    MCommonConfig.MBT_SCENARIO_THROUGH_STEP);
	            if (null == step)
	            {
	                step = "";
	            }
	            ArrayList<String> throughStepList = new ArrayList<String>();
	            for (String throughStep : step.trim().split(","))
	            {
	                throughStep = throughStep.trim();
	                if (throughStep.equals(""))
	                {
	                    break;
	                }
	                throughStepList.add(throughStep);
	            }
                scenarioInfoMap.put(MCommonConfig.MBT_SCENARIO_THROUGH_STEP, throughStepList);
                
                String path = MJsonHandler.getJsonValueByKey(option, MCommonConfig.MBT_SCENARIO_PATH);
                if (null == path)
                {
                    path = "";
                }
                scenarioInfoMap.put(MCommonConfig.MBT_SCENARIO_PATH, path);
                
                String data = MJsonHandler.getJsonValueByKey(option, MCommonConfig.MBT_SCENARIO_DATA);
                if (null == data)
                {
                    data = "";
                }
                scenarioInfoMap.put(MCommonConfig.MBT_SCENARIO_DATA, data);
                
                // get specified routes of current scenario
                Element routeElem = xmlParser.getSubFirstElement(scenarioElem, 
                        MCommonConfig.MBT_XML_ROUTE);
                String route = xmlParser.getNodeData(routeElem).trim();
                ArrayList<HashMap<String, String>> tmpRouteList = MJsonHandler.getJsonPathList(route);
                ArrayList<HashMap<String, ArrayList<String>>> routeList = 
                        new ArrayList<HashMap<String, ArrayList<String>>>();
                for (HashMap<String, String> routeMap : tmpRouteList)
                {
                    ArrayList<String> stateList = new ArrayList<String>();
                    for (String stateStr : routeMap.get(MCommonConfig.MBT_XML_STATE).trim().split(","))
                    {
                        stateStr = stateStr.trim();
                        if (stateStr.equals(""))
                        {
                            break;
                        }
                        stateList.add(stateStr);
                    }
                    
                    if (0 == stateList.size())
                    {
                        continue;
                    }
                    
                    ArrayList<String> stepList = new ArrayList<String>();
                    for (String stepStr : routeMap.get(MCommonConfig.MBT_XML_STEP).trim().split(","))
                    {
                        stepStr = stepStr.trim();
                        if (stepStr.equals(""))
                        {
                            break;
                        }
                        stepList.add(stepStr);
                    }
                    
                    if (0 == stepList.size())
                    {
                        continue;
                    }
                    
                    ArrayList<String> covList = new ArrayList<String>();
                    covList.add(routeMap.get(MCommonConfig.MBT_XML_COV));
                    
                    HashMap<String, ArrayList<String>> tmpRouteMap = new HashMap<String, ArrayList<String>>();
                    tmpRouteMap.put(MCommonConfig.MBT_XML_STATE, stateList);
                    tmpRouteMap.put(MCommonConfig.MBT_XML_STEP, stepList);
                    tmpRouteMap.put(MCommonConfig.MBT_XML_COV, covList);
                    routeList.add(tmpRouteMap);
                }
                scenarioInfoMap.put(MCommonConfig.MBT_XML_ROUTE, routeList);
	            
	            scenarioInfoList.add(scenarioInfoMap);
	        }
	        
	        return scenarioInfoList;
		}
		catch (DocumentException e)
		{
            MLogMag.getInstance().getLogger().error(MBasicApi.getLineInfo() 
                    + "Cannot parse xml \"" + mbtXmlFile + "\" in MGenScenParser.\n" + e.getMessage());
            return null;
		}
		catch (MException e)
		{
            MLogMag.getInstance().getLogger().error(MBasicApi.getLineInfo() 
                    + "Cannot parse xml \"" + mbtXmlFile + "\" in MGenScenParser.\n" + e.getMessage());
            return null;
		}
	}
}