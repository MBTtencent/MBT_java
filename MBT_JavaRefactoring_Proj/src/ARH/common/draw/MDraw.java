/*
 * FileName: MDraw.java
 * 
 * Description: create MDraw class
 * 
 * History:
 * 1.0 SHUYUFANG 2013-09-05 Create
 */


package ARH.common.draw;

import java.util.*;

import ARH.framework.graph.*;
import ARH.framework.logger.*;
import ARH.framework.basic.MBasicApi;
import ARH.framework.dot.*;
import ARH.framework.exception.MException;
import ARH.common.config.MCommonConfig;
import ARH.common.generate.MGenScenParser;
import ARH.common.path.*;

/**
 * this class is used to draw graph using MDot object
 * @author SHUYUFANG
 * @version 1.0
 */
public class MDraw
{
    /**
     * draw sequence graph according to MBT XML file and graph object
     * @param mbtXmlPath the path of MBT XML file
     * @param graph the graph object
     * @param fileDir the directory path to generate files
     * @throws MException
     */
    public static void drawSeqGraph(String mbtXmlPath, MGraph graph, String fileDir) 
            throws MException
    {
        drawSeqGraph(mbtXmlPath, graph, fileDir, false);
    }
    
    /**
     * draw sequence graph according to MBT XML file and graph object
     * @param mbtXmlPath the path of MBT XML file
     * @param graph the graph object
     * @param fileDir the directory path to generate files
     * @param showEdgeName show name of edge on the graph or not
     * @throws MException
     */
    @SuppressWarnings("unchecked")
	public static void drawSeqGraph(String mbtXmlPath, MGraph graph, String fileDir, boolean showEdgeName) 
	        throws MException
    {
        if (null == graph)
        {
            MLogMag.getInstance().getLogger().warning(MBasicApi.getLineInfo() + "\n" 
                    + "Graph is null in MDraw");
            return;            
        }
        
        MDot dot = new MDot();
        dot.setFileDir(fileDir);
        
        // number of nodes of the graph
        int nodeCount = graph.getNodeCount();
        // adjacency lists of the graph
        ArrayList<ArrayList<Integer>> edgeList = graph.getEdgeList();
        
        // add edges to the dot object first
        // all scenarios have same edges
        for (int i = 0; i < nodeCount; ++i)
        {
            int neighbourCount = edgeList.get(i).size();
            String beginNode = graph.getNodeName(i);
            for (int j = 0; j < neighbourCount; ++j)
            {
                String endNode = graph.getNodeName(edgeList.get(i).get(j));
                int edgeNumber = graph.getEdgeNumberList().get(i).get(j);
                String edgeName = graph.getEdgeNameList().get(i).get(j);
                
                // add an edge to the dot object
                String edgeLabel = String.valueOf(edgeNumber);
                if (showEdgeName)
                {
                    edgeLabel += "_" + edgeName;
                }
                dot.addEdge(beginNode, endNode, edgeLabel, MDotConfig.EDGE_TYPE_DIRECTED, 
                        MDotConfig.COLOR_BLACK, MDotConfig.STYLE_BOLD);
            }
        }
        
        // get the scenario parser
        ArrayList<HashMap<String, Object>> scenInfoList = MGenScenParser.parseScenario(mbtXmlPath);
        if (null == scenInfoList)
        {
	        MLogMag.getInstance().getLogger().error("Cannot parse scenario of xml file \"" 
	        		+ mbtXmlPath + "\" in MDraw");
        	return;
        }

        int scenNumber = scenInfoList.size();
        
        for (int i = 0; i < scenNumber; ++i)
        {
        	String scenName = (String)scenInfoList.get(i).get(MCommonConfig.MBT_XML_NAME);
        	
        	ArrayList<String> startNodeNameList = (ArrayList<String>)scenInfoList.get(i).get(MCommonConfig.MBT_SCENARIO_START);
        	ArrayList<String> stopNodeNameList = (ArrayList<String>)scenInfoList.get(i).get(MCommonConfig.MBT_SCENARIO_STOP);
        	
            // add start and stop nodes
            MDraw.setStartAndStopNodes(dot, startNodeNameList, stopNodeNameList);
            
            // draw graph
            MDraw.draw(dot, scenName);
        }
    }
    
    /**
     * draw coverage graph according to MBT XML file and graph object
     * @param mbtXmlPath the path of MBT XML file
     * @param graph the graph object
     * @param fileDir the directory path to generate files
     * @param scenariosPathsMap the paths map of scenarios
     * @throws MException
     */
    public static void drawCovGraph(String mbtXmlPath, MGraph graph, String fileDir,
            HashMap<String, HashMap<String, ArrayList<ArrayList<Integer>>>> scenariosPathsMap)
            throws MException
    {
        drawCovGraph(mbtXmlPath, graph, fileDir, scenariosPathsMap, false);
    }

    /**
     * draw coverage graph according to MBT XML file and graph object
     * @param mbtXmlPath the path of MBT XML file
     * @param graph the graph object
     * @param fileDir the directory path to generate files
     * @param scenariosPathsMap the paths map of scenarios
     * @param showEdgeName show name of edge on the graph or not
     * @throws MException
     */
    @SuppressWarnings("unchecked")
	public static void drawCovGraph(String mbtXmlPath, MGraph graph, String fileDir,
            HashMap<String, HashMap<String, ArrayList<ArrayList<Integer>>>> scenariosPathsMap, 
            boolean showEdgeName)
            throws MException
    {
        if (null == graph)
        {
            MLogMag.getInstance().getLogger().warning(MBasicApi.getLineInfo());
            MLogMag.getInstance().getLogger().warning("Graph is null in MDraw");
            return;            
        }
        
        MDot dot = new MDot();
        dot.setFileDir(fileDir);
        
        int nodeCount = graph.getNodeCount();
        int edgeCount = graph.getEdgeCount();
        ArrayList<ArrayList<Integer>> edgeList = graph.getEdgeList();
        ArrayList<Boolean> isEdgeUsedList = new ArrayList<Boolean>();
        for (int i = 0; i < edgeCount; ++i)
        {
            isEdgeUsedList.add(false);
        }
        
        // get scenarios parser
        ArrayList<HashMap<String, Object>> scenInfoList = MGenScenParser.parseScenario(mbtXmlPath);
        if (null == scenInfoList)
        {
	        MLogMag.getInstance().getLogger().error("Cannot parse scenario of xml file \"" 
	        		+ mbtXmlPath + "\" in MDraw");
        	return;
        }
        
        int scenNumber = scenInfoList.size();
        
        for (int i = 0; i < scenNumber; ++i)
        {
        	String scenName = (String)scenInfoList.get(i).get(MCommonConfig.MBT_XML_NAME);
        	
        	ArrayList<String> startNodeNameList = (ArrayList<String>)scenInfoList.get(i).get(MCommonConfig.MBT_SCENARIO_START);
        	ArrayList<String> stopNodeNameList = (ArrayList<String>)scenInfoList.get(i).get(MCommonConfig.MBT_SCENARIO_STOP);
        	
            // clear nodes and edges map in dot object
            dot.clearNodeMap();
            dot.clearEdgeMap();
            
            // set all edge to be unused
            for (int j = 0; j < edgeCount; ++j)
            {
                isEdgeUsedList.set(j, false);
            }
            
            // set edges in the path to be used 
            ArrayList<ArrayList<Integer>> edgePathList = scenariosPathsMap.get(scenName).get(MPathConfig.EDGE_PATH); 
            for (int j = 0; j < edgePathList.size(); ++j)
            {
                for (int k = 0; k < edgePathList.get(j).size(); ++k)
                {
                    isEdgeUsedList.set(edgePathList.get(j).get(k) - 1, true);
                }
            }
            
            // add edges to dot object
            for (int j = 0; j < nodeCount; ++j)
            {
                int neighbourCount = edgeList.get(j).size();
                String beginNode = graph.getNodeName(j);
                for (int k = 0; k < neighbourCount; ++k)
                {
                    String endNode = graph.getNodeName(edgeList.get(j).get(k));
                    int edgeNumber = graph.getEdgeNumberList().get(j).get(k);
                    String edgeName = graph.getEdgeNameList().get(j).get(k);
                    String edgeColor;
                    // the edge is used in one or more paths
                    if (isEdgeUsedList.get(edgeNumber - 1))
                    {
                        edgeColor = MDotConfig.COLOR_BLACK;
                    }
                    // the edge is never used in any path
                    else
                    {
                        edgeColor = MDotConfig.COLOR_RED;
                    }
                    
                    // add an edge to the dot object
                    String edgeLabel = String.valueOf(edgeNumber);
                    if (showEdgeName)
                    {
                        edgeLabel += "_" + edgeName;
                    }
                    dot.addEdge(beginNode, endNode, edgeLabel, MDotConfig.EDGE_TYPE_DIRECTED, 
                            edgeColor, MDotConfig.STYLE_BOLD);
                }
            }
            
            // add start and stop nodes
            MDraw.setStartAndStopNodes(dot, startNodeNameList, stopNodeNameList);
            
            // draw graph
            MDraw.draw(dot, scenName + "_cov");
        }
    }
    
    /**
     * set start nodes and stop nodes of the dot object
     * @param dot the dot object
     * @param startNodeList the start node name list
     * @param stopNodeList the stop node name list
     */
    private static void setStartAndStopNodes(MDot dot, ArrayList<String> startNodeList, 
            ArrayList<String> stopNodeList)
    {
        // clear all nodes in the dot object
        dot.clearNodeMap();
        
        // add start nodes to the dot object
        for (int i = 0; i < startNodeList.size(); ++i)
        {
            dot.addNode(startNodeList.get(i), MDotConfig.NODE_SHAPE_ELLIPSE,
                    MDotConfig.STYLE_FILLED, MDotConfig.COLOR_YELLOW);
        }
        
        // add stop nodes to the dot object
        for (int i = 0; i < stopNodeList.size(); ++i)
        {
            dot.addNode(stopNodeList.get(i), MDotConfig.NODE_SHAPE_ELLIPSE, 
                    MDotConfig.STYLE_FILLED, MDotConfig.COLOR_GREEN);
        }
    }
    
    /**
     * draw graph
     * @param dot dot object used to draw graph
     * @param fileName name of the dot file and graph file
     */
    private static void draw(MDot dot, String fileName) throws MException
    {
        if (null == dot)
        {
            return;
        }
        
        // set graph file name
        dot.setFileName(fileName);
        
        // generate dot file
        dot.genDotFile();
        
        // generate graph file
        dot.genGraph();
        
        // delete dot file
        dot.delDotFile();
    }
}