/*
 * FileName: MGenDocMap.java
 * 
 * Description: create MGenDocMap class
 * 
 * History: 
 * 1.0 SHUYUFANG 2013-08-28 Create
 */


package ARH.common.generate;

import java.util.*;

import ARH.framework.basic.MBasicApi;
import ARH.framework.exception.*;
import ARH.framework.xml.*;
import ARH.framework.logger.*;
import ARH.common.config.*;
import ARH.common.data.*;
import ARH.common.view.MViewBase;

import org.dom4j.*;

/**
 * this class is used to substitute parameters and generate scripts
 * which can be expanded through "view" part
 * @author SHUYUFANG
 * @version 1.0
 */
public class MGenDocMap
{
    /** the path of MBT XML file */
    private String mbtXml;
    
    /** the step variable combination XML */
    private String stepVarCombXml;
    
    /** the name of the scenario */
    private String scenarioName;
    
    /** the data algorithm */
    private String dataAlgo;
    
    /** the list of edge path */
    private ArrayList<ArrayList<Integer>> edgePathList;
    
    /** the list of step path */
    private ArrayList<ArrayList<Integer>> stepPathList;
    
    /** the map of document object of paths */
    private HashMap<String, ArrayList<Document>> scriptDocMap;
    
    /** the map of document object name of paths */
    private HashMap<String, ArrayList<HashMap<Integer, String>>> scriptNameMap;
    
    /**
     * constructor
     * @param mbtXml MBT XML file path
     * @param stepVarCombXml step variable combination XML
     * @param scenarioName name of the scenario
     * @param dataAlgo data algorithm
     * @param edgePathList the list of edge path of the scenario
     * @param stepPathList the list of step path of the scenario
     */
    public MGenDocMap(String mbtXml, String stepVarCombXml, String scenarioName, String dataAlgo, 
            ArrayList<ArrayList<Integer>> edgePathList, ArrayList<ArrayList<Integer>> stepPathList)
    {
        this.mbtXml = mbtXml;
        this.stepVarCombXml = stepVarCombXml;
        this.scenarioName = scenarioName;
        this.dataAlgo = dataAlgo;
        this.edgePathList = edgePathList;
        this.stepPathList = stepPathList;
        this.scriptDocMap = new HashMap<String, ArrayList<Document>>();
        this.scriptNameMap = new HashMap<String, ArrayList<HashMap<Integer, String>>>();
    }
    
    
    /**
     * get the name of a path specified by its index
     * @param pathIndex index of the path in the list
     * @return the name of the path
     */
    private String getStepPathName(int pathIndex)
    {
        String pathInfo = "";
        // the name is composed of the edge number of the path
        for (int i = 0; i < this.edgePathList.get(pathIndex).size(); ++i)
        {
            pathInfo += "_" + this.edgePathList.get(pathIndex).get(i);
        }
        return this.scenarioName + pathInfo;
    }
    
    /**
     * substitute parameters in the action string to real value
     * @param paramMap
     * @param action
     * @return the substituted action string
     */
    private String substituteParam(HashMap<String, String> paramMap, String action)
    {
        // substitute parameters one by one
        for (String paramName : paramMap.keySet())
        {
            // the REGEX of parameters before substituted
            String originStr = "\\{MBT_SCEN_PARAM\\(" + paramName + "\\)\\}";
            
            String goalStr = paramMap.get(paramName);
            action = action.replaceAll(originStr, goalStr);
        }
        return action;
    }
    
    /**
     * generate the map of document object of paths
     * @return the document map generated or not
     */
    public boolean genDocMap()
    {
        MGenReplacedStep stepGenner = new MGenReplacedStep(mbtXml, stepVarCombXml);
        
        for (int i = 0; i < this.stepPathList.size(); ++i)
        {
            // ignore illegal paths
        	if (null == this.edgePathList.get(i) || 0 == this.edgePathList.get(i).size())
        	{
        		continue;
        	}
        	
        	// generate parameter set of a path
            HashSet<String> pathParamSet = MGenPathParamSet.genPathParamSet(this.mbtXml, 
                    this.stepPathList.get(i));
            
            // generate combinations of parameters of a path
            HashMap<Integer, HashMap<String, String>> pathParamMap = MGenData.genData(
                    this.mbtXml, this.scenarioName, this.dataAlgo, pathParamSet);
            
            // the name of the path
            String pathName = this.getStepPathName(i);
            
            // add a new list of document object for current path
            scriptDocMap.put(pathName, new ArrayList<Document>());
            scriptNameMap.put(pathName, new ArrayList<HashMap<Integer, String>>());
            
            // if no parameter is used in current path
            // then just one null parameter map is needed
            if (0 == pathParamMap.size())
            {
                pathParamMap.put(0, new HashMap<String, String>());
            }
            
            stepGenner.genReplacedStep(stepPathList.get(i));
            
            ArrayList<HashMap<Integer, ArrayList<String>>> replacedStepList = stepGenner.getReplacedStepList();
            ArrayList<String> replacedNameList = stepGenner.getReplacedNameList();
            
            for (int j = 0; j < replacedStepList.size(); ++j)
            {
            	for (int k = 0; k < pathParamMap.size(); ++k)
            	{
            		String pathCaseName = pathName + "[" + String.valueOf(j * pathParamMap.size() + k + 1) + "]";
            		
            		String scenParamNameStr = "";
            		for (String paramName : pathParamMap.get(k).keySet())
            		{
            		    scenParamNameStr += "," + paramName + "=" + pathParamMap.get(k).get(paramName);
            		}
            		if (!scenParamNameStr.equals(""))
            		{
            		    scenParamNameStr = "{" + scenParamNameStr.substring(1) + "}";
            		}
            		
            		try
                    {
                        // the XML creator to create document object
                        MXmlCreate xmlCreator = new MXmlCreate();
                        xmlCreator.setFormat(MXmlConfig.MXML_FORMAT_PRETTY_PRINT, 
                                MXmlConfig.MXML_FORMAT_ENCODING_UTF8_TYPE);
                        
                        // create root element called "PATH"
                        Element pathElem = xmlCreator.addElement(null, MCommonConfig.MBT_XML_PATH);
                        
                        // create sub element called "NAME"
                        Element nameElem = xmlCreator.addElement(pathElem, MCommonConfig.MBT_XML_NAME);
                        xmlCreator.addElementData(nameElem, pathCaseName);
                        
                        // create sub element called "TESTSTEPS"
                        Element testStepsElem = xmlCreator.addElement(pathElem, MCommonConfig.MBT_XML_TEST_STEPS);
                        
                        for (int m = 0; m < stepPathList.get(i).size(); ++m)
                        {
                        	int curStepNumber = stepPathList.get(i).get(m);
                        	ArrayList<String> actionList = replacedStepList.get(j).get(curStepNumber);
                        	
                        	// create sub element called "TESTSTEP"
                            Element testStepElem = xmlCreator.addElement(testStepsElem, MCommonConfig.MBT_XML_TEST_STEP);
                            
                            // create sub element called "NAME"
                            Element stepNameElem = xmlCreator.addElement(testStepElem, MCommonConfig.MBT_XML_NAME);
                            String stepName = stepGenner.getStepNameMap().get(curStepNumber);
                            xmlCreator.addElementData(stepNameElem, stepName);
                            
                            for (int n = 0; n < actionList.size(); ++n)
                            {
                            	String replacedAction = substituteParam(pathParamMap.get(k), actionList.get(n));
                            	
                            	// create sub element called "ACTION"
                                Element actionElem = xmlCreator.addElement(testStepElem, MCommonConfig.MBT_XML_ACTION);
                                xmlCreator.addElementData(actionElem, replacedAction);
                            }
                        }
                     
                        // set name for current document object
                        xmlCreator.getDoc().setName(pathCaseName);
                        
                        // add current document object of current path to document map
                        scriptDocMap.get(pathName).add(xmlCreator.getDoc());
                        
                        HashMap<Integer, String> tmpNameMap = new HashMap<Integer, String>();
                        tmpNameMap.put(MViewBase.MCASE_NAME_TYPE_NUM, "[" + String.valueOf(j * pathParamMap.size() + k + 1) + "]");
                        tmpNameMap.put(MViewBase.MCASE_NAME_TYPE_DATAINFO, scenParamNameStr + replacedNameList.get(j));
                        scriptNameMap.get(pathName).add(tmpNameMap);
                    }
                    
                    catch (MException e)
                    {
                        MLogMag.getInstance().getLogger().error(MBasicApi.getLineInfo() + "\n" +
                                "Cannot generate doc map in MGenDocMap" + "\n" +
                                e.getMessage());
                        continue;
                    }
            	}
            }
        }
        
        return true;
    }
    
    /**
     * get the generated document object map
     * @return the document map
     */
    public HashMap<String, ArrayList<Document>> getScriptDocMap()
    {
        return scriptDocMap;
    }
    
    /**
     * get the generated document object name map
     * @return the name map
     */
    public HashMap<String, ArrayList<HashMap<Integer, String>>> getScriptNameMap()
    {
        return scriptNameMap;
    }
}