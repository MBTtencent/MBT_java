/*
 * FileName: MGenPath.java
 * 
 * Description: create MGenPath class
 * 
 * History:
 * 1.0 SHUYUFANG 2013-08-29 Create
 */


package ARH.common.path;

import java.util.*;

import ARH.common.config.MCommonConfig;
import ARH.common.generate.MGenScenParser;
import ARH.framework.graph.*;
import ARH.framework.logger.*;

/**
 * this class is used to generate paths of scenarios
 * @author SHUYUFANG
 * @version 1.0
 */
public class MGenPath
{
    /**
     * generate paths of scenarios specified by XML file and the graph
     * @param mbtXmlFilePath
     * @param graph
     * @return the result map of paths
     */
    @SuppressWarnings("unchecked")
	public static HashMap<String, HashMap<String, ArrayList<ArrayList<Integer>>>> genPath(
            String mbtXml, MGraph graph)
    {
        if (null == graph)
        {
            MLogMag.getInstance().getLogger().error("Cannot parse graph in MGenPath");
            return null;
        }
        
        ArrayList<HashMap<String, Object>> scenInfoList = MGenScenParser.parseScenario(mbtXml);
        if (null == scenInfoList)
        {
	        MLogMag.getInstance().getLogger().error("Cannot parse scenario of xml file \"" 
	        		+ mbtXml + "\" in MGenPath");
        	return null;
        }
        
        HashMap<String, HashMap<String, ArrayList<ArrayList<Integer>>>> scenariosPathsMap = 
                new HashMap<String, HashMap<String, ArrayList<ArrayList<Integer>>>>();
        
        int scenarioNumber = scenInfoList.size();
        HashMap<String, ArrayList<ArrayList<Integer>>> scenPaths;
        
        for (int i = 0; i < scenarioNumber; ++i)
        {
        	String scenName = (String) scenInfoList.get(i).get(MCommonConfig.MBT_XML_NAME);
        	String pathAlgo = (String) scenInfoList.get(i).get(MCommonConfig.MBT_SCENARIO_PATH);
        	
        	ArrayList<String> startStateNameList = (ArrayList<String>) scenInfoList.get(i)
        	        .get(MCommonConfig.MBT_SCENARIO_START);
        	ArrayList<String> stopStateNameList = (ArrayList<String>) scenInfoList.get(i)
        	        .get(MCommonConfig.MBT_SCENARIO_STOP);
        	ArrayList<String> throughStateNameList = (ArrayList<String>) scenInfoList.get(i)
        	        .get(MCommonConfig.MBT_SCENARIO_THROUGH_STATE);
        	ArrayList<String> throughStepNameList = (ArrayList<String>) scenInfoList.get(i)
        	        .get(MCommonConfig.MBT_SCENARIO_THROUGH_STEP);
        	ArrayList<HashMap<String, ArrayList<String>>> routeList = 
        	        (ArrayList<HashMap<String, ArrayList<String>>>) scenInfoList.get(i).get(MCommonConfig.MBT_XML_ROUTE);
        	
        	if (routeList.size() > 0)
        	{
        	    pathAlgo = MPathConfig.PATH_ROUTE;
        	}
        	
        	scenPaths = MPathAlgoMag.getInstance().getAlgorithm(pathAlgo).
                    genPath(graph, startStateNameList, stopStateNameList, throughStateNameList, 
                            throughStepNameList, routeList);
        	
            scenariosPathsMap.put(scenName, scenPaths);
        }
        
        return scenariosPathsMap;
    }
}